package mob_grinding_utils.client;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import mob_grinding_utils.client.render.TileEntityAbsorptionRenderer;
import mob_grinding_utils.client.render.TileEntityFanRenderer;
import mob_grinding_utils.client.render.TileEntityMGUSpawnerRenderer;
import mob_grinding_utils.client.render.TileEntitySawRenderer;
import mob_grinding_utils.client.render.TileEntityTankRenderer;
import mob_grinding_utils.client.render.TileEntityXPSolidifierRenderer;
import mob_grinding_utils.models.ModelAHConnect;
import mob_grinding_utils.models.ModelSawBase;
import mob_grinding_utils.models.ModelSawBlade;
import mob_grinding_utils.models.ModelTankBlock;
import mob_grinding_utils.models.ModelXPSolidifier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModelLayers {
    public static final ModelLayerLocation SAW_BASE = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "saw_base"), "saw_base");
    public static final ModelLayerLocation SAW_BLADE = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "saw_blade"), "saw_blade");
    public static final ModelLayerLocation ABSORPTION_HOPPER = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "absorption_hopper"), "absorption_hopper");
    public static final ModelLayerLocation TANK = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "tank"), "tank");
    public static final ModelLayerLocation XPSOLIDIFIER = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "xp_solidifier"), "xp_solidifier");

    public static void init(IEventBus bus) {
        bus.addListener(ModelLayers::registerEntityRenderers);
        bus.addListener(ModelLayers::registerLayerDefinitions);
    }
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlocks.FAN.getTileEntityType(), TileEntityFanRenderer::new);
        event.registerBlockEntityRenderer(ModBlocks.SAW.getTileEntityType(), TileEntitySawRenderer::new);
        event.registerBlockEntityRenderer(ModBlocks.ABSORPTION_HOPPER.getTileEntityType(), TileEntityAbsorptionRenderer::new);
        event.registerBlockEntityRenderer(ModBlocks.TANK.getTileEntityType(), TileEntityTankRenderer::new);
        event.registerBlockEntityRenderer(ModBlocks.TANK_SINK.getTileEntityType(), TileEntityTankRenderer::new);
        event.registerBlockEntityRenderer(ModBlocks.JUMBO_TANK.getTileEntityType(), TileEntityTankRenderer::new);
        event.registerBlockEntityRenderer(ModBlocks.XPSOLIDIFIER.getTileEntityType(), TileEntityXPSolidifierRenderer::new);
        event.registerBlockEntityRenderer(ModBlocks.ENTITY_SPAWNER.getTileEntityType(), TileEntityMGUSpawnerRenderer::new);
    }
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SAW_BASE, ModelSawBase::createBodyLayer);
        event.registerLayerDefinition(SAW_BLADE, ModelSawBlade::createBodyLayer);
        event.registerLayerDefinition(ABSORPTION_HOPPER, ModelAHConnect::createBodyLayer);
        event.registerLayerDefinition(TANK, ModelTankBlock::createBodyLayer);
        event.registerLayerDefinition(XPSOLIDIFIER, ModelXPSolidifier::createBodyLayer);
    }
}
