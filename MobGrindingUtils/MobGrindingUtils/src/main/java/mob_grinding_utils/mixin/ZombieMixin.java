package mob_grinding_utils.mixin;

import mob_grinding_utils.util.FakePlayerHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Zombie;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public class ZombieMixin {
    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;getEntity()Lnet/minecraft/world/entity/Entity;", ordinal = 0), cancellable = true)
    public void hurtMixin(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getEntity() instanceof FakePlayer fakePlayer && FakePlayerHandler.isMGUFakePlayer(fakePlayer)) {
                cir.setReturnValue(false);
        }
    }
}
