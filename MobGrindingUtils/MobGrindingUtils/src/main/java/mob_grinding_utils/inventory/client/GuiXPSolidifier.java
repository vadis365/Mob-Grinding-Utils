package mob_grinding_utils.inventory.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mob_grinding_utils.inventory.server.ContainerXPSolidifier;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

public class GuiXPSolidifier extends ContainerScreen<ContainerXPSolidifier> {
    private static final ResourceLocation GUI_TEX = new ResourceLocation("mob_grinding_utils:textures/gui/solidifier_gui.png");
    protected final ContainerXPSolidifier container;
    private final TileEntityXPSolidifier tile;

    public GuiXPSolidifier(ContainerXPSolidifier screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        container = screenContainer;
        tile = container.tile;

        ySize = 186;
        xSize = 176;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.drawString(matrixStack, I18n.format("block.mob_grinding_utils.xpsolidifier"), 7,6,0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        getMinecraft().getTextureManager().bindTexture(GUI_TEX);
        int xOffSet = (width - xSize) / 2;
        int yOffSet = (height - ySize) / 2;
        int zLevel = 0;
        this.blit(stack, xOffSet, yOffSet, 0, 0, xSize, ySize);

        int fluid = tile.getScaledFluid(70);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (fluid >= 1) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(tile.tank.getFluid().getFluid().getAttributes().getStillTexture());
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            Minecraft.getInstance().textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE); // dunno if needed now
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(xOffSet + 8, yOffSet + 88, zLevel).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
            buffer.pos(xOffSet + 20, yOffSet + 88, zLevel).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
            buffer.pos(xOffSet + 20, yOffSet + 88 - fluid, zLevel).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            buffer.pos(xOffSet + 8, yOffSet + 88 - fluid, zLevel).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            tessellator.draw();
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(stack, mouseX, mouseY);
    }
}
