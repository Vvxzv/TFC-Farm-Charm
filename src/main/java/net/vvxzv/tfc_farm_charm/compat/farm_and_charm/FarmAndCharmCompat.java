package net.vvxzv.tfc_farm_charm.compat.farm_and_charm;

import net.minecraft.world.level.block.Block;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.vvxzv.tfc_farm_charm.common.registry.Blocks;

import java.util.Map;

public class FarmAndCharmCompat {
    public static final Block[] FOOD_BLOCKS = new Block[]{
            ObjectRegistry.ROASTED_CORN_BLOCK.get(),
            ObjectRegistry.OAT_PANCAKE_BLOCK.get(),
            ObjectRegistry.FARMERS_BREAKFAST.get(),
            ObjectRegistry.BAKED_LAMB_HAM.get(),
            ObjectRegistry.POTATO_WITH_ROAST_MEAT.get(),
            ObjectRegistry.STUFFED_CHICKEN.get(),
            ObjectRegistry.STUFFED_RABBIT.get(),
            ObjectRegistry.FARMERS_BREAD.get(),
            ObjectRegistry.GRANDMOTHERS_STRAWBERRY_CAKE.get()
    };
}
