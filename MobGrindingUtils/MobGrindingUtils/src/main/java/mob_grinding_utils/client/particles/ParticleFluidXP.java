package mob_grinding_utils.client.particles;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleFluidXP extends Particle {

	public double colorR = 0;
	public double colorG = 0;
	public double colorB = 0;
	public int textureIndex = 0;
	public int prevTextureIndex = 0;

	private static final ResourceLocation[] TEXTURE = new ResourceLocation[] {
			new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles0"),
			new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles1"),
			new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles2"),
			new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles3"),
			new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles4"),
			new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles5"),
			new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles6"),
			new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles7"),
			new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles8")};

	public ParticleFluidXP(World world, double x, double y, double z, double tx, double ty, double tz, int count, int color, float scale) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);

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
		particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		particleScale = ((MathHelper.sin(count / 2.0F) * 0.1F + 1.0F) * scale);

		setParticleTexture(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TEXTURE[0].toString()));
	}

	@Override
	public boolean isTransparent() {
		return true;
	}

	@Override
	public void onUpdate() {
		prevTextureIndex = textureIndex;
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		motionY -= (double) particleGravity;
		move(motionX, motionY, motionZ);
		motionY *= 0.9800000190734863D;

        if (this.particleAge++ >= this.particleMaxAge)
            this.setExpired();

        textureIndex = 0 + particleAge * 8 / particleMaxAge;

        if (textureIndex > 8)
        	textureIndex = 8;

        if(particleAge%3 == 0)
        	if(prevTextureIndex != textureIndex)
        		setParticleTexture(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TEXTURE[textureIndex].toString()));
	}

	@Override
	public int getFXLayer() {
		return 1;
	}

	@Override
    public int getBrightnessForRender(float partialTicks) {
        return 15728880;
    }
}
