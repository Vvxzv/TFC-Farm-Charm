package net.vvxzv.tfc_farm_charm.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
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
                .icon(() -> new ItemStack(TFCFCItem.CUP.get()))
                .displayItems((parm, output) -> {
                    List<RegistryObject<Item>> modItems = List.of(
                            TFCFCItem.CUP,
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
                            TFCFCItem.CRUSTY_BREAD,
                            TFCFCItem.BREAD,
                            TFCFCItem.BAGUETTE,
                            TFCFCItem.TOAST,
                            TFCFCItem.BRAIDED_BREAD,
                            TFCFCItem.BUN,
                            TFCFCItem.WAFFLE,
                            TFCFCItem.OAT_PANCAKE,
                            TFCFCItem.FARMERS_BREAKFAST,
                            TFCFCItem.BAKED_LAMB_HAM,
                            TFCFCItem.POTATO_WITH_ROAST_MEAT,
                            TFCFCItem.STUFFED_CHICKEN,
                            TFCFCItem.STUFFED_RABBIT,
                            TFCFCItem.GRANDMOTHERS_STRAWBERRY_CAKE,
                            TFCFCItem.FARMERS_BREAD,
                            TFCFCItem.ROASTED_CORN
                    );
                    modItems.forEach(item -> output.accept(item.get()));
                    List<RegistryObject<Block>> modBlockItem = List.of(
                            TFCFCBlock.BREAD_CRATE,
                            TFCFCBlock.CHOCOLATE_BOX,
                            TFCFCBlock.STRAWBERRY_JAM,
                            TFCFCBlock.SWEETBERRY_JAM,
                            TFCFCBlock.GLOWBERRY_JAM,
                            TFCFCBlock.APPLE_JAM,
                            TFCFCBlock.CHOCOLATE_JAM,
                            TFCFCBlock.STRAWBERRY_CAKE,
                            TFCFCBlock.SWEETBERRY_CAKE,
                            TFCFCBlock.CHOCOLATE_CAKE,
                            TFCFCBlock.CHOCOLATE_GATEAU,
                            TFCFCBlock.BUNDT_CAKE,
                            TFCFCBlock.LINZER_TART,
                            TFCFCBlock.APPLE_PIE,
                            TFCFCBlock.GLOWBERRY_TART,
                            TFCFCBlock.CHOCOLATE_TART,
                            TFCFCBlock.PUDDING,
                            TFCFCBlock.FERTILIZED_FARMLAND
                    );
                    modBlockItem.forEach(item -> output.accept(item.get()));

                    TFCFCItem.BEER_FLUID_BUCKETS.values().forEach(reg -> output.accept(reg.get()));

                    if(ModList.get().isLoaded("brewery")){
                        List<RegistryObject<Item>> breweryItems = List.of(
                                TFCFCItem.PORK_KNUCKLE,
                                TFCFCItem.FRIED_CHICKEN,
                                TFCFCItem.DUMPLINGS,
                                TFCFCItem.HALF_CHICKEN,
                                TFCFCItem.MASHED_POTATOES,
                                TFCFCItem.POTATO_SALAD
                        );
                        breweryItems.forEach(item -> output.accept(item.get()));
                    }
                })
                .build()
        );
    }
}
