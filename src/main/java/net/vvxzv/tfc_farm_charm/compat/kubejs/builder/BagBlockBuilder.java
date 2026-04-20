package net.vvxzv.tfc_farm_charm.compat.kubejs.builder;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.vvxzv.tfc_farm_charm.common.block.BagBlock;

import java.util.function.Consumer;

public class BagBlockBuilder extends BlockBuilder {
    private static final Consumer<LootBuilder> EMPTY = (loot) -> {};

    public BagBlockBuilder(ResourceLocation id) {
        super(id);
        this.lootTable = EMPTY;
    }

    @Override
    public Block createObject() {
        return new BagBlock();
    }
}
