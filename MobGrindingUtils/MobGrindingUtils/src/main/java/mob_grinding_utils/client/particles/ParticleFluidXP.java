package mob_grinding_utils.client.particles;

import java.awt.Color;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

public class ParticleFluidXP extends SpriteTexturedParticle {

    public double colorR = 0;
    public double colorG = 0;
    public double colorB = 0;
    public int textureIndex = 0;
    public int prevTextureIndex = 0;

    public ParticleFluidXP(ClientWorld world, double x, double y, double z, double tx, double ty, double tz, int count, int color, float scale) {
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
        //particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        //particleScale = ((MathHelper.sin(count / 2.0F) * 0.1F + 1.0F) * scale);

        //setParticleTexture(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TEXTURE[0].toString()));
    }
    /* //todo dunno
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
     */
/* //todo not sure what 1 means...
	@Override
	public int getFXLayer() {
		return 1;
	}
*/
    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {

    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.NO_RENDER; //todo temp
    }

    @Override
    public int getBrightnessForRender(float partialTicks) {
        return 15728880;
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {
        @Override
        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return null;
        }
    }

}