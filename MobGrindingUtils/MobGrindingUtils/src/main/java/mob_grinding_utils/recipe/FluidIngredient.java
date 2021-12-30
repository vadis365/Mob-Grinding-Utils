package mob_grinding_utils.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import mob_grinding_utils.Reference;
import net.minecraft.core.Registry;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.Tag;
import net.minecraft.tags.SerializationTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FluidIngredient extends Ingredient {

    private final Boolean advanced;
    private final List<Fluid> matchingFluids = new ArrayList<>();
    private final Tag<Fluid> fluidTag;

    private ItemStack[] bucketCache = null;
    private IntList matchingStacksPacked;

    private List<Fluid> getMatchingFluids() {
        if (!matchingFluids.isEmpty())
            return matchingFluids;

        if (fluidTag != null)
            matchingFluids.addAll(fluidTag.getValues());

        return matchingFluids;
    }


    public FluidIngredient(Tag<Fluid> tagIn, boolean advancedIn) {
        super(Stream.empty());

        advanced = advancedIn;

        fluidTag = tagIn;
    }
    public FluidIngredient(Fluid fluidIn, boolean advancedIn) {
        super(Stream.empty());

        advanced = advancedIn;
        matchingFluids.add(fluidIn);
        fluidTag = null;

    }

    public FluidIngredient(List<Fluid> fluidList, boolean advancedIn) {
        super(Stream.empty());

        advanced = advancedIn;
        fluidTag = null;

        matchingFluids.addAll(fluidList);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public IntList getStackingIds() {
        if (this.matchingStacksPacked == null) {
            this.matchingStacksPacked = new IntArrayList(this.getItems().length);
            for(ItemStack itemstack : this.bucketCache) {
                this.matchingStacksPacked.add(StackedContents.getStackingIndex(itemstack));
            }
            this.matchingStacksPacked.sort(IntComparators.NATURAL_COMPARATOR);
        }
        return this.matchingStacksPacked;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();

        json.addProperty("type", Serializer.NAME.toString());

        json.addProperty("advanced", advanced);
        if (fluidTag != null) {
            json.addProperty("tag", SerializationTags.getInstance().getIdOrThrow(Registry.FLUID_REGISTRY, fluidTag, () -> new IllegalStateException("Unknown fluid tag")
            ).toString());
        }
        else {
            json.addProperty("fluid", getMatchingFluids().get(0).getRegistryName().toString());
        }

        return json;
    }

    @Nonnull
    @Override
    public ItemStack[] getItems() {
        if (bucketCache == null) {
            List<ItemStack> tmp = new ArrayList<>();
            getMatchingFluids().forEach((fluid -> {
                ItemStack newBucket = FluidUtil.getFilledBucket(new FluidStack(fluid, 1000));
                if (!newBucket.isEmpty())
                    tmp.add(newBucket);
            }));
            bucketCache = tmp.toArray(tmp.toArray(new ItemStack[0]));
        }
        return bucketCache;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return SERIALIZER;
    }
    public static Serializer SERIALIZER = new Serializer();
    public static class Serializer implements IIngredientSerializer<FluidIngredient> {
        public static ResourceLocation NAME = new ResourceLocation(Reference.MOD_ID, "fluid");
        @Override
        public FluidIngredient parse(FriendlyByteBuf buffer) {
            boolean buckets = buffer.readBoolean();
            int fluidCount = buffer.readVarInt();

            List<Fluid> fluids = new ArrayList<>();

            for (int i = 0; i < fluidCount; i++) {
                fluids.add(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(buffer.readUtf())));
            }

            return new FluidIngredient(fluids, buckets);
        }

        @Override
        public FluidIngredient parse(JsonObject json) {
            boolean advanced = json.has("advanced") && GsonHelper.getAsBoolean(json, "advanced");
            if (json.has("tag")) {
                ResourceLocation tagRes = new ResourceLocation(GsonHelper.getAsString(json, "tag"));
                Tag<Fluid> fluidTag = SerializationTags.getInstance().getTagOrThrow(Registry.FLUID_REGISTRY, tagRes, (tag) -> new IllegalStateException("Unknown fluid tag: " + tag));
                if (fluidTag != null)
                    return new FluidIngredient(fluidTag, advanced);
                else
                    throw new JsonSyntaxException("invalid fluid tag: " + tagRes);
            }
            else if (json.has("fluid")) {
                ResourceLocation fluidRes = new ResourceLocation(GsonHelper.getAsString(json, "fluid"));
                Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidRes);
                if (fluid != null) {
                    return new FluidIngredient(fluid, advanced);
                }
            }
            else {
                throw new JsonSyntaxException("invalid FluidIngredient, must specify either a fluid, or a fluid tag.");
            }
            throw new JsonParseException("FluidIngredient needs either a fluid or a fluid tag.");
        }

        @Override
        public void write(FriendlyByteBuf buffer, FluidIngredient ingredient) {
            buffer.writeBoolean(ingredient.advanced);
            buffer.writeVarInt(ingredient.getMatchingFluids().size());
            ingredient.getMatchingFluids().forEach((fluid -> buffer.writeUtf(fluid.getRegistryName().toString())));
        }
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        LazyOptional<IFluidHandlerItem> cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

        if (cap.isPresent()) {
            if (!advanced && !(stack.getItem() instanceof BucketItem))
                return false;

            FluidStack fluid = cap.resolve().get().getFluidInTank(0);
            return getMatchingFluids().contains(fluid.getFluid()) && fluid.getAmount() >= 1000;
        }

        return false;
    }
}
