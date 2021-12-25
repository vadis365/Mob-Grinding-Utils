package mob_grinding_utils.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import mob_grinding_utils.Reference;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
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
    private final ITag<Fluid> fluidTag;

    private ItemStack[] bucketCache = null;
    private IntList matchingStacksPacked;

    private List<Fluid> getMatchingFluids() {
        if (!matchingFluids.isEmpty())
            return matchingFluids;

        if (fluidTag != null)
            matchingFluids.addAll(fluidTag.getAllElements());

        return matchingFluids;
    }


    public FluidIngredient(ITag<Fluid> tagIn, boolean advancedIn) {
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
    public boolean hasNoMatchingItems() {
        return false;
    }

    @Override
    public IntList getValidItemStacksPacked() {
        if (this.matchingStacksPacked == null) {
            this.matchingStacksPacked = new IntArrayList(this.getMatchingStacks().length);
            for(ItemStack itemstack : this.bucketCache) {
                this.matchingStacksPacked.add(RecipeItemHelper.pack(itemstack));
            }
            this.matchingStacksPacked.sort(IntComparators.NATURAL_COMPARATOR);
        }
        return this.matchingStacksPacked;
    }

    @Override
    public JsonElement serialize() {
        JsonObject json = new JsonObject();

        json.addProperty("type", Serializer.NAME.toString());

        json.addProperty("advanced", advanced);
        if (fluidTag != null) {
            json.addProperty("tag", TagCollectionManager.getManager().getFluidTags().getValidatedIdFromTag(fluidTag).toString());
        }
        else {
            json.addProperty("fluid", getMatchingFluids().get(0).getRegistryName().toString());
        }

        return json;
    }

    @Nonnull
    @Override
    public ItemStack[] getMatchingStacks() {
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
        public FluidIngredient parse(PacketBuffer buffer) {
            boolean buckets = buffer.readBoolean();
            int fluidCount = buffer.readVarInt();

            List<Fluid> fluids = new ArrayList<>();

            for (int i = 0; i < fluidCount; i++) {
                fluids.add(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(buffer.readString())));
            }

            return new FluidIngredient(fluids, buckets);
        }

        @Override
        public FluidIngredient parse(JsonObject json) {
            boolean advanced = json.has("advanced") && JSONUtils.getBoolean(json, "advanced");
            if (json.has("tag")) {
                ResourceLocation tagRes = new ResourceLocation(JSONUtils.getString(json, "tag"));
                ITag<Fluid> fluidTag = TagCollectionManager.getManager().getFluidTags().get(tagRes);
                if (fluidTag != null)
                    return new FluidIngredient(fluidTag, advanced);
                else
                    throw new JsonSyntaxException("invalid fluid tag: " + tagRes);
            }
            else if (json.has("fluid")) {
                ResourceLocation fluidRes = new ResourceLocation(JSONUtils.getString(json, "fluid"));
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
        public void write(PacketBuffer buffer, FluidIngredient ingredient) {
            buffer.writeBoolean(ingredient.advanced);
            buffer.writeVarInt(ingredient.getMatchingFluids().size());
            ingredient.getMatchingFluids().forEach((fluid -> buffer.writeString(fluid.getRegistryName().toString())));
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
