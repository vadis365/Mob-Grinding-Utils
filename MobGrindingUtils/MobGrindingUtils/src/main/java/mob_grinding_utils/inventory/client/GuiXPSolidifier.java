package mob_grinding_utils.inventory.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerXPSolidifier;
import mob_grinding_utils.network.MessageSolidifier;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;

public class GuiXPSolidifier extends AbstractContainerScreen<ContainerXPSolidifier> {
    private static final ResourceLocation GUI_TEX = new ResourceLocation("mob_grinding_utils:textures/gui/solidifier_gui.png");
    protected final ContainerXPSolidifier container;
    private final TileEntityXPSolidifier tile;

    public GuiXPSolidifier(ContainerXPSolidifier screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        container = screenContainer;
        tile = container.tile;

        imageHeight = 186;
        imageWidth = 176;
    }

    @Override
    protected void init() {
        super.init();
        int xOffSet = (width - imageWidth) / 2;
        int yOffSet = (height - imageHeight) / 2;

        addRenderableWidget(new GuiMGUButton(xOffSet + 62, yOffSet + 72, GuiMGUButton.Size.SOLIDIFIER, 0, new TextComponent("Push") ,(button) -> {
            MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageSolidifier(0, tile.getBlockPos()));
        }));
        
        addRenderableWidget(new GuiMGUButton(xOffSet + 148, yOffSet + 8, GuiMGUButton.Size.SOLIDIFIER_ON, 0, new TextComponent("") ,(button) -> {
            MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageSolidifier(1, tile.getBlockPos()));
        }));
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, I18n.get("block.mob_grinding_utils.xpsolidifier"), 7, 6, 0x404040);
        this.font.draw(matrixStack, I18n.get("container.inventory"), 8, this.imageHeight - 96 + 2, 4210752);
        this.font.drawShadow(matrixStack, tile.isOn ? "On" : "Off", 158 - font.width(tile.isOn ? "On" : "Off") / 2, 12, 14737632);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
       	RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, GUI_TEX);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int xOffSet = (width - imageWidth) / 2;
        int yOffSet = (height - imageHeight) / 2;
        int zLevel = 0;
        this.blit(stack, xOffSet, yOffSet, 0, 0, imageWidth, imageHeight);

        font.draw(stack, tile.outputDirection.getSerializedName(), xOffSet + 124 - font.width(tile.outputDirection.getSerializedName()) / 2, yOffSet + 76, 5285857);

        int fluid = tile.getScaledFluid(70);

        if (fluid >= 1) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(tile.tank.getFluid().getFluid().getAttributes().getStillTexture());
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder buffer = tessellator.getBuilder();
	    	RenderSystem.setShader(GameRenderer::getPositionTexShader);
	        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
	        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            buffer.vertex(xOffSet + 8, yOffSet + 88, zLevel).uv(sprite.getU0(), sprite.getV0()).endVertex();
            buffer.vertex(xOffSet + 20, yOffSet + 88, zLevel).uv(sprite.getU1(), sprite.getV0()).endVertex();
            buffer.vertex(xOffSet + 20, yOffSet + 88 - fluid, zLevel).uv(sprite.getU1(), sprite.getV1()).endVertex();
            buffer.vertex(xOffSet + 8, yOffSet + 88 - fluid, zLevel).uv(sprite.getU0(), sprite.getV1()).endVertex();
            tessellator.end();
        }
		getMinecraft().getTextureManager().bindForSetup(GUI_TEX);
		this.blit(stack, xOffSet + 7, yOffSet + 17 , 178, 0, 6, 71);

		this.blit(stack, xOffSet + 91, yOffSet + 36, 178, 73, tile.getProgressScaled(24), 17);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(PoseStack matrixStack, int x, int y) {
        super.renderTooltip(matrixStack, x, y);
        int xOffSet = (width - imageWidth) / 2;
        int yOffSet = (height - imageHeight) / 2;
        if (x > xOffSet + 8 && x < xOffSet + 20 && y > yOffSet + 20 && y < yOffSet + 88) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(tile.tank.getFluid().getDisplayName());
            tooltip.add(new TextComponent(tile.tank.getFluidAmount() + "/" + tile.tank.getCapacity()));
            this.renderTooltip(matrixStack, tooltip, Optional.empty(), x, y, this.font);
        }
    }
}
