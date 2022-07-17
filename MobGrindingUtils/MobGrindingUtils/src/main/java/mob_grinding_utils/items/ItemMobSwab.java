package mob_grinding_utils.items;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemMobSwab extends Item {
	public boolean used;
	public ItemMobSwab(Properties properties, boolean used) {
		super(properties);
		this.used = used;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level world, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
		if (!used)
			list.add(Component.translatable("tooltip.mobswab_1").withStyle(ChatFormatting.YELLOW));
		else if (stack.hasTag() && Objects.requireNonNull(stack.getTag()).contains("mguMobName")) {
			list.add(Component.translatable("tooltip.mobswab_2").withStyle(ChatFormatting.YELLOW));
			list.add(Component.translatable("tooltip.mobswab_3").withStyle(ChatFormatting.GREEN).append( " " + Objects.requireNonNull(stack.getTag().get("mguMobName")).getAsString() + " 'DNA'."));
		}
	}

	@Nonnull
	@Override
	public InteractionResult interactLivingEntity(@Nonnull ItemStack stack, @Nonnull Player player, @Nonnull LivingEntity target, @Nonnull InteractionHand hand) {
		if (!(target instanceof Player) && !used && !target.getType().is(MobGrindingUtils.NOSWAB)) {
			String mobName = Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(target.getType())).toString();
			ItemStack stack2 = new ItemStack(ModItems.MOB_SWAB_USED.get(), 1);
			if (!stack2.getOrCreateTag().contains("mguMobName")) {
				stack2.getTag().putString("mguMobName", mobName);
				CompoundTag nbt = new CompoundTag();
				target.addAdditionalSaveData(nbt);
			}
			player.setItemInHand(hand, stack2);
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.PASS;
		}
	}

}