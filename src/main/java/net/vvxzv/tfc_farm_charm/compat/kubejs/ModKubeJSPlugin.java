package net.vvxzv.tfc_farm_charm.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.vvxzv.tfc_farm_charm.compat.kubejs.builder.BagBlockBuilder;

public class ModKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void init(){
        RegistryInfo.BLOCK.addType("tfc_farm_charm:bag", BagBlockBuilder.class, BagBlockBuilder::new);
    }
}