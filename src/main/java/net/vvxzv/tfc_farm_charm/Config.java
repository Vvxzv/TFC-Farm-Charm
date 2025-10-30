package net.vvxzv.tfc_farm_charm;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = TFCFarmCharm.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.DoubleValue FERTILIZER_ON_FERTILIZED_FARMLAND =BUILDER.comment("对肥沃耕地施肥的养分加成倍率（默认是原养分的2.5倍）").defineInRange("fertilizerOnFertilizedFarmland", 2.5, 1, 10);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double fertilizerOnFertilizedFarmland;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event){
        fertilizerOnFertilizedFarmland = FERTILIZER_ON_FERTILIZED_FARMLAND.get();
    }
}
