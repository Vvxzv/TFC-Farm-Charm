package net.vvxzv.tfc_farm_charm.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;

import java.util.List;

public class CreativeTAB {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TFCFarmCharm.MODID);

    public static final RegistryObject<CreativeModeTab> TFC_FARM_CHARM;

    static {
        TFC_FARM_CHARM = CREATIVE_MODE_TAB.register(TFCFarmCharm.MODID, () -> CreativeModeTab.builder()
                .title(Component.translatable("tfc_farm_charm.tab.name"))
                .icon(() -> new ItemStack(TFCFCItem.UNFINISHED_APPLE_PIE.get()))
                .displayItems((parm, output) -> {
                    List<RegistryObject<Item>> modItems = List.of(
                            TFCFCItem.UNFINISHED_APPLE_PIE,
                            TFCFCItem.UNFINISHED_BAGUETTE,
                            TFCFCItem.UNFINISHED_BRAIDED_BREAD,
                            TFCFCItem.UNFINISHED_BREAD,
                            TFCFCItem.UNFINISHED_BUN,
                            TFCFCItem.UNFINISHED_BUNDT_CAKE,
                            TFCFCItem.UNFINISHED_CHOCOLATE_TART,
                            TFCFCItem.UNFINISHED_CROISSANT,
                            TFCFCItem.UNFINISHED_CRUSTY_BREAD,
                            TFCFCItem.UNFINISHED_FARMERS_BREAD,
                            TFCFCItem.UNFINISHED_GLOWBERRY_TART,
                            TFCFCItem.UNFINISHED_GRANDMOTHERS_STRAWBERRY_CAKE,
                            TFCFCItem.UNFINISHED_LINZER_TART,
                            TFCFCItem.UNFINISHED_PRETZEL,
                            TFCFCItem.UNFINISHED_TOAST,
                            TFCFCItem.UNFINISHED_WAFFLE,
                            TFCFCItem.CUP
                    );
                    modItems.forEach(item -> output.accept(item.get()));

                    TFCFCItem.BEER_FLUID_BUCKETS.values().forEach(reg -> output.accept(reg.get()));
                })
                .build()
        );
    }
}
