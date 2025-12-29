package net.vvxzv.tfc_farm_charm;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vvxzv.tfc_farm_charm.api.fluid.AbstractAnimateFluidInstance;
import net.vvxzv.tfc_farm_charm.common.registry.TAB;
import net.vvxzv.tfc_farm_charm.common.registry.TFCFCBlock;
import net.vvxzv.tfc_farm_charm.common.registry.TFCFCFluid;
import net.vvxzv.tfc_farm_charm.common.registry.TFCFCItem;
import org.slf4j.Logger;

@Mod(TFCFarmCharm.MODID)
public class TFCFarmCharm {
    public static final String MODID = "tfc_farm_charm";
    private static final Logger LOGGER = LogUtils.getLogger();
    @SuppressWarnings("removal")
    public TFCFarmCharm() {
        LOGGER.info("Hello! TFC Farm Charm");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        TFCFCBlock.BLOCKS.register(modEventBus);
        TFCFCItem.ITEMS.register(modEventBus);

        //AbstractAnimateFluidInstance.ITEMS.register(modEventBus);
        //AbstractAnimateFluidInstance.BLOCKS.register(modEventBus);
        //AbstractAnimateFluidInstance.FLUIDS.register(modEventBus);
        //AbstractAnimateFluidInstance.FLUID_TYPES.register(modEventBus);

        //TFCFCFluid.generateFeatures();

        TAB.CREATIVE_MODE_TAB.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
