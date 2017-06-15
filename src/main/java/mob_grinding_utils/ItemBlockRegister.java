package mob_grinding_utils;

import com.typesafe.config.ConfigException.Null;
import java.util.List;
import mob_grinding_utils.blocks.BlockAbsorptionHopper;
import mob_grinding_utils.blocks.BlockDarkOakStone;
import mob_grinding_utils.blocks.BlockDragonMuffler;
import mob_grinding_utils.blocks.BlockFan;
import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.blocks.BlockSpikes;
import mob_grinding_utils.blocks.BlockTank;
import mob_grinding_utils.blocks.BlockTankSink;
import mob_grinding_utils.blocks.BlockWitherMuffler;
import mob_grinding_utils.blocks.BlockXPTap;
import mob_grinding_utils.items.ItemAbsorptionUpgrade;
import mob_grinding_utils.items.ItemFanUpgrade;
import mob_grinding_utils.items.ItemGMChickenFeed;
import mob_grinding_utils.items.ItemImaginaryInvisibleNotReallyThereSword;
import mob_grinding_utils.items.ItemMobSwab;
import mob_grinding_utils.items.ItemSawUpgrade;
import mob_grinding_utils.recipe.ModRecipes;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.FMLSecurityManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * BlockItem Registry. Helps clean up TONS of code :)
 */
public class ItemBlockRegister {
    public static Boolean DebugMode = true;
    public static Block SPIKES, FAN, ABSORPTION_HOPPER, TANK, TANK_SINK, XP_TAP, WITHER_MUFFLER, DRAGON_MUFFLER, SAW, DARK_OAK_STONE;
    public static Item SPIKES_ITEM, FAN_ITEM, ABSORPTION_HOPPER_ITEM, TANK_ITEM, TANK_SINK_ITEM, FAN_UPGRADE, ABSORPTION_UPGRADE, XP_TAP_ITEM, MOB_SWAB, GM_CHICKEN_FEED, WITHER_MUFFLER_ITEM, DRAGON_MUFFLER_ITEM, SAW_ITEM, SAW_UPGRADE, DARK_OAK_STONE_ITEM;
    public static ItemSword NULL_SWORD;

    public static void preinit(){


        //Register Specials
        DRAGON_MUFFLER = new BlockDragonMuffler();
        FAN_UPGRADE = new ItemFanUpgrade();
        ABSORPTION_UPGRADE = new ItemAbsorptionUpgrade();
        SAW_UPGRADE = new ItemSawUpgrade();
        MOB_SWAB = new ItemMobSwab();
        GM_CHICKEN_FEED = new ItemGMChickenFeed();
        NULL_SWORD = new ItemImaginaryInvisibleNotReallyThereSword();
        ABSORPTION_HOPPER = new BlockAbsorptionHopper();
        SAW = new BlockSaw();
        FAN = new BlockFan();
        TANK = new BlockTank();
        SPIKES = new BlockSpikes();
        TANK_SINK = new BlockTankSink();
        XP_TAP = new BlockXPTap();
        WITHER_MUFFLER = new BlockWitherMuffler();
        DARK_OAK_STONE= new BlockDarkOakStone();
        //Register Blocks
        RegisterB(SPIKES, "spikes", "mob_grinding_utils.spikes",SPIKES_ITEM);

        RegisterB(TANK_SINK,"tank_sink","mob_grinding_utils.tank_sink",TANK_SINK_ITEM);
        RegisterB(DARK_OAK_STONE,"dark_oak_stone","mob_grinding_utils.dark_oak_stone",DARK_OAK_STONE_ITEM);
        RegisterB(TANK,"tank", "mob_grinding_utils.tank",TANK_ITEM);
        RegisterB(ABSORPTION_HOPPER,"absorption_hopper","mob_grinding_utils.absorption_hopper",ABSORPTION_HOPPER_ITEM);
        RegisterB(FAN,"fan","mob_grinding_utils.fan",FAN_ITEM);
        RegisterB(SAW,"saw","mob_grinding_utils.saw",SAW_ITEM);
        RegisterB(DRAGON_MUFFLER,"dragon_muffler","mob_grinding_utils.dragon_muffler",DRAGON_MUFFLER_ITEM);
        RegisterB(XP_TAP,"xp_tap","mob_grinding_utils.xp_tap",XP_TAP_ITEM);
        RegisterB(WITHER_MUFFLER,"wither_muffler","mob_grinding_utils.wither_muffler",WITHER_MUFFLER_ITEM);
        //Register Items
        Register(NULL_SWORD,"null_sword","mob_grinding_utils.null_sword");
        Register(FAN_UPGRADE,"fan_upgrade","mob_grinding_utils.fan_upgrade");
        Register(ABSORPTION_UPGRADE,"absorption_upgrade","mob_grinding_utils.absorption_upgrade");
        Register(SAW_UPGRADE,"saw_upgrade","mob_grinding_utils.saw_upgrade");
        Register(MOB_SWAB,"mob_swab","mob_grinding_utils.mob_swab");
        Register(GM_CHICKEN_FEED,"gm_chicken_feed","mob_grinding_utils.gm_chicken_feed");
        //Register Specials
        FAN_ITEM = new ItemBlock(FAN){
            @Override
            @SideOnly(Side.CLIENT)
            public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fan_1").getFormattedText());
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fan_2").getFormattedText());
            }
        };
        SAW_ITEM = new ItemBlock(SAW){
            @Override
            @SideOnly(Side.CLIENT)
            public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.saw_1").getFormattedText());
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.saw_2").getFormattedText());
            }
        };
        ABSORPTION_HOPPER_ITEM = new ItemBlock(ABSORPTION_HOPPER) {
            @Override
            @SideOnly(Side.CLIENT)
            public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_1").getFormattedText());
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_2").getFormattedText());
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_3").getFormattedText());
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.hopper_4").getFormattedText());
            }
        };
        SPIKES_ITEM = new ItemBlock(SPIKES) {
            @Override
            @SideOnly(Side.CLIENT)
            public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.spikes_1").getFormattedText());
            }
        };
        TANK_ITEM = new ItemBlock(TANK) {
            @Override
            @SideOnly(Side.CLIENT)
            public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
                if(stack.hasTagCompound() && !stack.getTagCompound().hasKey("Empty")) {
                    FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
                    if(fluid !=null) {
                        list.add(TextFormatting.GREEN + "Contains: "+ fluid.getFluid().getLocalizedName(fluid));
                        list.add(TextFormatting.BLUE + ""+ fluid.amount +"Mb/32000Mb");
                    }
                }
            }
        };
        TANK_SINK_ITEM = new ItemBlock(TANK_SINK) {
            @Override
            @SideOnly(Side.CLIENT)
            public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
                if(stack.hasTagCompound() && !stack.getTagCompound().hasKey("Empty")) {
                    FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
                    if(fluid !=null) {
                        list.add(TextFormatting.GREEN + "Contains: "+ fluid.getFluid().getLocalizedName(fluid));
                        list.add(TextFormatting.BLUE + ""+ fluid.amount +"Mb/32000Mb");
                    }
                }
            }
        };
        XP_TAP_ITEM = new ItemBlock(XP_TAP) {
            @Override
            @SideOnly(Side.CLIENT)
            public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.xptap_1").getFormattedText());
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.xptap_2").getFormattedText());
            }
        };
        WITHER_MUFFLER_ITEM = new ItemBlock(WITHER_MUFFLER) {
            @Override
            @SideOnly(Side.CLIENT)
            public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.withermuffler_1").getFormattedText());
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.withermuffler_2").getFormattedText());
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.withermuffler_3").getFormattedText());
            }
        };
        DRAGON_MUFFLER_ITEM = new ItemBlock(DRAGON_MUFFLER) {
            @Override
            @SideOnly(Side.CLIENT)
            public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.dragonmuffler_1").getFormattedText());
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.dragonmuffler_2").getFormattedText());
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.dragonmuffler_3").getFormattedText());
            }
        };
        DARK_OAK_STONE_ITEM = new ItemBlock(DARK_OAK_STONE) {
            @Override
            @SideOnly(Side.CLIENT)
            public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
                list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.darkoakstone").getFormattedText());
            }
        };
        //Add Recipes
        ModRecipes.addRecipes();
    }
    //Stack functions used to make code neater.
    /**
     * Registers Items
     * @param item a static item.
     * @param name the registryName
     * @param Unlocalised unlocalisedName
     */
    public static void Register(Item item, String name,String Unlocalised) {
        try {
            GameRegistry.register(item.setRegistryName("mob_grinding_utils", name).setUnlocalizedName(Unlocalised));
        }
        catch (NullPointerException e) {
            FMLLog.info(e.toString());
            FMLLog.info("item :" + item.getRegistryName().toString() + " name: " + name.toString() + " Unlocalised: " + Unlocalised.toString());
        }
    }

    /**
     * Registers Blocks
     * @param block a static block.
     * @param name the registryName
     * @param Unlocalised unlocalisedName
     */
    public static void RegisterB(Block block, String name,String Unlocalised,Item item) {
        GameRegistry.register(block.setRegistryName("mob_grinding_utils", name).setUnlocalizedName(Unlocalised));
        GameRegistry.register(item.setRegistryName(block.getRegistryName()).setUnlocalizedName(Unlocalised));
        if(DebugMode) {
            FMLLog.info("block: " + block.getRegistryName() + " name: " + name + " Unlocalised: " + Unlocalised);
        }
    }
    //GameRegistry.register(SPIKES_ITEM.setRegistryName(SPIKES.getRegistryName()).setUnlocalizedName("mob_grinding_utils.spikes"));
}
