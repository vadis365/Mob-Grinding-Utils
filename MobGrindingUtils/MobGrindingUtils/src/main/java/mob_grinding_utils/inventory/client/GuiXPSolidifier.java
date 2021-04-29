package mob_grinding_utils.inventory.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerXPSolidifier;
import mob_grinding_utils.network.MessageSolidifier;
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
import net.minecraft.util.text.StringTextComponent;

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
    protected void init() {
        super.init();
        int xOffSet = (width - xSize) / 2;
        int yOffSet = (height - ySize) / 2;

        addButton(new GuiMGUButton(xOffSet + 62, yOffSet + 72, GuiMGUButton.Size.SOLIDIFIER, 0, new StringTextComponent("Push") ,(button) -> {
            MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageSolidifier(0, tile.getPos()));
        }));
        
        addButton(new GuiMGUButton(xOffSet + 148, yOffSet + 8, GuiMGUButton.Size.SOLIDIFIER_ON, 0, new StringTextComponent("") ,(button) -> {
            MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageSolidifier(1, tile.getPos()));
        }));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.drawString(matrixStack, I18n.format("block.mob_grinding_utils.xpsolidifier"), 7, 6, 0x404040);
        this.font.drawString(matrixStack, I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
        this.font.drawStringWithShadow(matrixStack, tile.isOn ? "On" : "Off", 158 - font.getStringWidth(tile.isOn ? "On" : "Off") / 2, 12, 14737632);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        getMinecraft().getTextureManager().bindTexture(GUI_TEX);
        int xOffSet = (width - xSize) / 2;
        int yOffSet = (height - ySize) / 2;
        int zLevel = 0;
        this.blit(stack, xOffSet, yOffSet, 0, 0, xSize, ySize);

        font.drawString(stack, tile.outputDirection.getString(), xOffSet + 124 - font.getStringWidth(tile.outputDirection.getString()) / 2, yOffSet + 76, 5285857);

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
		getMinecraft().getTextureManager().bindTexture(GUI_TEX);
		this.blit(stack, xOffSet + 7, yOffSet + 17 , 178, 0, 6, 71);

		this.blit(stack, xOffSet + 91, yOffSet + 36, 178, 73, tile.getProgressScaled(24), 17);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y) {
        super.renderHoveredTooltip(matrixStack, x, y);
        int xOffSet = (width - xSize) / 2;
        int yOffSet = (height - ySize) / 2;
        if (x > xOffSet + 8 && x < xOffSet + 20 && y > yOffSet + 20 && y < yOffSet + 88) {
            List<ITextComponent> tooltip = new ArrayList<>();
            tooltip.add(tile.tank.getFluid().getDisplayName());
            tooltip.add(new StringTextComponent(tile.tank.getFluidAmount() + "/" + tile.tank.getCapacity()));
            this.renderWrappedToolTip(matrixStack,  tooltip, x, y, this.font);
        }
    }
}
