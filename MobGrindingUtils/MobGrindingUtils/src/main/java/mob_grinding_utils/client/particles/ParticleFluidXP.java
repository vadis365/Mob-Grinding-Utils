package mob_grinding_utils.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;
import java.awt.*;

public class ParticleFluidXP extends TextureSheetParticle {

    @SuppressWarnings("unused")
    private final SpriteSet sprites;

    public ParticleFluidXP(ClientLevel world, double x, double y, double z, double tx, double ty, double tz, int count, int color, float scale, SpriteSet sprite) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.sprites = sprite;
        xd = 0.0D;
        yd = 0.0D;
        zd = 0.0D;

        Color c = new Color(color);
        float mr = c.getRed() / 255.0F * 0.2F;
        float mg = c.getGreen() / 255.0F * 0.2F;
        float mb = c.getBlue() / 255.0F * 0.2F;
        rCol = (c.getRed() / 255.0F - mr + random.nextFloat() * mr);
        gCol = (c.getGreen() / 255.0F - mg + random.nextFloat() * mg);
        bCol = (c.getBlue() / 255.0F - mb + random.nextFloat() * mb);

        gravity = 0.01F;
        lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        xd = 0.0D;
        yd = 0.0D;
        zd = 0.0D;
        quadSize = ((Mth.sin(count / 2.0F) * 0.1F + 1.0F) * scale);
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        yd -= (double) gravity;
        move(xd, yd, zd);
        yd *= 0.9800000190734863D;
        if (this.age++ >= this.lifetime)
            this.remove();
    }

    @Nonnull
    @Override
    public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

    @Override
    public int getLightColor(float partialTicks) {
		return 15728880;
	}
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        SpriteSet sprites;

        public Factory(SpriteSet sprite) {
        	this.sprites = sprite;
		}

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleFluidXP particle = new ParticleFluidXP(world, x + world.random.nextDouble() - 0.5D * 0.05D, y + 0.125D, z + world.random.nextDouble() - 0.5D * 0.05D, xSpeed, ySpeed, zSpeed, 20, 16776960, 0.125F, sprites);
            particle.pickSprite(sprites);
            return particle;
        }
    }
}