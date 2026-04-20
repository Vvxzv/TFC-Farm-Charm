package net.vvxzv.tfc_farm_charm.compat.brewery;

import net.minecraft.world.level.block.Block;
import net.satisfy.brewery.core.registry.ObjectRegistry;

public class BreweryCompat {
    public static final Block[] FOOD_BLOCKS = new Block[]{
            ObjectRegistry.PORK_KNUCKLE_BLOCK.get(),
            ObjectRegistry.FRIED_CHICKEN_BLOCK.get(),
            ObjectRegistry.DUMPLINGS_BLOCK.get(),
            ObjectRegistry.HALF_CHICKEN_BLOCK.get(),
            ObjectRegistry.MASHED_POTATOES_BLOCK.get(),
            ObjectRegistry.POTATO_SALAD_BLOCK.get()
    };
}
