package mob_grinding_utils.mixin;

import mob_grinding_utils.util.FakePlayerHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public class ZombieMixin {
    /**
     * Skip zombie reinforcement spawning when damage comes from an MGU fake player
     * (e.g. the saw). Injects after damage has already been applied via super.hurtServer.
     */
    @Inject(
            method = "hurtServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/damagesource/DamageSource;getEntity()Lnet/minecraft/world/entity/Entity;",
                    ordinal = 0
            ),
            cancellable = true
    )
    public void hurtMixin(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getEntity() instanceof FakePlayer fakePlayer && FakePlayerHandler.isMGUFakePlayer(fakePlayer)) {
            // Damage already applied; return true and skip reinforcement logic.
            cir.setReturnValue(true);
        }
    }
}
