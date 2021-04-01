package mob_grinding_utils.client.particles;

import java.awt.Color;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;

public class ParticleFluidXP extends SpriteTexturedParticle {

	@SuppressWarnings("unused")
	private final IAnimatedSprite sprites;

    public ParticleFluidXP(ClientWorld world, double x, double y, double z, double tx, double ty, double tz, int count, int color, float scale, IAnimatedSprite sprite) {
    	super(world, x, y, z, 0.0D, 0.0D, 0.0D);
    	this.sprites = sprite;
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;

        Color c = new Color(color);
        float mr = c.getRed() / 255.0F * 0.2F;
        float mg = c.getGreen() / 255.0F * 0.2F;
        float mb = c.getBlue() / 255.0F * 0.2F;
        particleRed = (c.getRed() / 255.0F - mr + rand.nextFloat() * mr);
        particleGreen = (c.getGreen() / 255.0F - mg + rand.nextFloat() * mg);
        particleBlue = (c.getBlue() / 255.0F - mb + rand.nextFloat() * mb);

        particleGravity = 0.01F;
        maxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        particleScale = ((MathHelper.sin(count / 2.0F) * 0.1F + 1.0F) * scale);
    }

		@Override
        public void tick() {
            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;
            motionY -= (double) particleGravity;
            move(motionX, motionY, motionZ);
            motionY *= 0.9800000190734863D;
            if (this.age++ >= this.maxAge)
                this.setExpired();
        }

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public int getBrightnessForRender(float partialTicks) {
		return 15728880;
	}
	
	public static class Factory implements IParticleFactory<BasicParticleType> {
		IAnimatedSprite sprites;

        public Factory(IAnimatedSprite sprite) {
        	this.sprites = sprite;
		}
 
		@Override
		public Particle makeParticle(BasicParticleType typeIn, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			ParticleFluidXP particle = new ParticleFluidXP(world, x + world.rand.nextDouble() - 0.5D * 0.05D, y + 0.125D, z + world.rand.nextDouble() - 0.5D * 0.05D, xSpeed, ySpeed, zSpeed, 20, 16776960, 0.125F, sprites);
			particle.selectSpriteRandomly(sprites);
			return particle;
		}

	}
}