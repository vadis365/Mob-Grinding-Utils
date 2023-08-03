package mob_grinding_utils.inventory.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.ContainerXPSolidifier;
import mob_grinding_utils.network.MessageSolidifier;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

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

        addRenderableWidget(new GuiMGUButton(xOffSet + 62, yOffSet + 72, GuiMGUButton.Size.SOLIDIFIER, 0, Component.literal("Push") ,
            (button) -> MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageSolidifier(0, tile.getBlockPos()))));

        addRenderableWidget(new GuiMGUButton(xOffSet + 148, yOffSet + 8, GuiMGUButton.Size.SOLIDIFIER_ON, 0, Component.literal("") ,
            (button) -> MobGrindingUtils.NETWORK_WRAPPER.sendToServer(new MessageSolidifier(1, tile.getBlockPos()))));
    }

    @Override
    protected void renderLabels(@Nonnull GuiGraphics gg, int x, int y) {
        gg.drawString(font, Component.translatable("block.mob_grinding_utils.xpsolidifier"), 7, 6, 0x404040, false);
        gg.drawString(font, Component.translatable("container.inventory"), 8, this.imageHeight - 96 + 2, 4210752, false);
        gg.drawString(font, tile.isOn ? "On" : "Off", 158 - font.width(tile.isOn ? "On" : "Off") / 2.0f, 12, 14737632, true);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
        int xOffSet = (width - imageWidth) / 2;
        int yOffSet = (height - imageHeight) / 2;
        int zLevel = 0;
        gg.blit(GUI_TEX, xOffSet, yOffSet, 0, 0, imageWidth, imageHeight);

        gg.drawString(font, tile.outputDirection.getSerializedName(), xOffSet + 124 - font.width(tile.outputDirection.getSerializedName()) / 2.0f, yOffSet + 76, 5285857, false);

        int fluid = tile.getScaledFluid(70);

        var fluidExtensions = IClientFluidTypeExtensions.of(tile.tank.getFluid().getFluid());

        if (fluid >= 1) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidExtensions.getStillTexture());
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
        gg.blit(GUI_TEX, xOffSet + 7, yOffSet + 17 , 178, 0, 6, 71);

        gg.blit(GUI_TEX, xOffSet + 91, yOffSet + 36, 178, 73, tile.getProgressScaled(24), 17);
    }

    @Override
    public void render(@Nonnull GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, partialTicks);
        renderTooltip(gg, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(@Nonnull GuiGraphics gg, int x, int y) {
        super.renderTooltip(gg, x, y);
        int xOffSet = (width - imageWidth) / 2;
        int yOffSet = (height - imageHeight) / 2;
        if (x > xOffSet + 8 && x < xOffSet + 20 && y > yOffSet + 20 && y < yOffSet + 88) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(tile.tank.getFluid().getDisplayName());
            tooltip.add(Component.literal(tile.tank.getFluidAmount() + "/" + tile.tank.getCapacity()));
            gg.renderComponentTooltip(font, tooltip, x, y);
        }
    }
}
