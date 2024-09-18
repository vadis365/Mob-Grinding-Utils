package mob_grinding_utils.items;

import mob_grinding_utils.ModItems;
import mob_grinding_utils.ModTags;
import mob_grinding_utils.components.MGUComponents;
import mob_grinding_utils.util.RL;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemMobSwab extends Item {
	public boolean used;
	public ItemMobSwab(Properties properties, boolean used) {
		super(properties);
		this.used = used;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
		if (!used)
			list.add(Component.translatable("tooltip.mobswab_1").withStyle(ChatFormatting.YELLOW));
		else if (stack.has(MGUComponents.MOB_DNA)) {
			list.add(Component.translatable("tooltip.mobswab_2").withStyle(ChatFormatting.YELLOW));
			list.add(Component.translatable("tooltip.mobswab_3").withStyle(ChatFormatting.GREEN).append(stack.getOrDefault(MGUComponents.MOB_DNA, RL.mc("nobody")).toString() + " 'DNA'."));
		}
	}

	@Nonnull
	@Override
	public InteractionResult interactLivingEntity(@Nonnull ItemStack stack, @Nonnull Player player, @Nonnull LivingEntity target, @Nonnull InteractionHand hand) {
		if (!(target instanceof Player) && !used && !target.getType().is(ModTags.Entities.NO_SWAB)) {
			ResourceLocation mobTypeKey = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
			ItemStack stack2 = new ItemStack(ModItems.MOB_SWAB_USED.get(), 1);

			stack2.set(MGUComponents.MOB_DNA, mobTypeKey);
			player.setItemInHand(hand, stack2);
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.PASS;
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		if (used || pContext.getPlayer() == null)
			return InteractionResult.PASS;
		Level level = pContext.getLevel();
		BlockPos blockPos = pContext.getClickedPos();
		var block = level.getBlockState(blockPos);

		if(block.getBlock() instanceof SpawnerBlock) {
			BlockEntity blockEntity = level.getBlockEntity(blockPos);
			if(blockEntity instanceof SpawnerBlockEntity spawnerBlockEntity) {
				Entity entity = spawnerBlockEntity.getSpawner().getOrCreateDisplayEntity(level, blockPos);
				if (entity != null && !entity.getType().is(ModTags.Entities.NO_SWAB)) {
					ResourceLocation mobTypeKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
					ItemStack stack2 = new ItemStack(ModItems.MOB_SWAB_USED.get(), 1);

					stack2.set(MGUComponents.MOB_DNA, mobTypeKey);
					pContext.getPlayer().setItemInHand(pContext.getHand(), stack2);
					return InteractionResult.SUCCESS;
				}
			}
		}


		return super.useOn(pContext);
	}
}