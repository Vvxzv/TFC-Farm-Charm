package net.vvxzv.tfc_farm_charm.common.utils;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;

public class AllTags {
    public static class Blocks {
        public static final TagKey<Block> HEAT_SOURCE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(TFCFarmCharm.MODID, "heat_source"));
    }

}
