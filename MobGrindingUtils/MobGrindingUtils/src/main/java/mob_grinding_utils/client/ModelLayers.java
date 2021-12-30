package mob_grinding_utils.client;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import mob_grinding_utils.client.render.*;
import mob_grinding_utils.models.ModelAHConnect;
import mob_grinding_utils.models.ModelSawBase;
import mob_grinding_utils.models.ModelXPSolidifier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModelLayers {
    public static final ModelLayerLocation SAW = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "saw"), "saw");
    public static final ModelLayerLocation ABSORPTION_HOPPER = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "absorption_hopper"), "absorption_hopper");
    public static final ModelLayerLocation TANK = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "tank"), "tank");
    public static final ModelLayerLocation TANK_SINK = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "tank_sink"), "tank_sink");
    public static final ModelLayerLocation JUMBO_TANK = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "jumbo_tank"), "jumbo_tank");
    public static final ModelLayerLocation XPSOLIDIFIER = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "xp_solidifier"), "xp_solidifier");
    public static final ModelLayerLocation ENTITY_SPAWNER = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "entity_spawner"), "entity_spawner");

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
        event.registerLayerDefinition(SAW, ModelSawBase::createBodyLayer);
        event.registerLayerDefinition(ABSORPTION_HOPPER, ModelAHConnect::createBodyLayer);
        //event.registerLayerDefinition(TANK, fanmodel::createBodyLayer);
        //event.registerLayerDefinition(TANK_SINK, fanmodel::createBodyLayer);
        //event.registerLayerDefinition(JUMBO_TANK, fanmodel::createBodyLayer);
        event.registerLayerDefinition(XPSOLIDIFIER, ModelXPSolidifier::createBodyLayer);
        //event.registerLayerDefinition(ENTITY_SPAWNER, fanmodel::createBodyLayer);
    }
}
