package net.vvxzv.tfc_farm_charm.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;

@SuppressWarnings("removal")
public class BlockTags {
    public static final TagKey<Block> HEAT_SOURCE = TagKey.create(Registries.BLOCK, new ResourceLocation(TFCFarmCharm.MODID, "heat_source"));

}
