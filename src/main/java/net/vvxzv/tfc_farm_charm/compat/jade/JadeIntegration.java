package net.vvxzv.tfc_farm_charm.compat.jade;

import net.satisfy.bakery.core.block.cake.PieBlock;
import net.satisfy.farm_and_charm.core.block.EatableBoxBlock;
import net.satisfy.farm_and_charm.core.block.FoodBlock;
import net.satisfy.farm_and_charm.core.block.StackableBlock;
import net.satisfy.farm_and_charm.core.block.StackableEatableBlock;
import net.vvxzv.tfc_farm_charm.common.block.BagBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadeIntegration implements IWailaPlugin {
    public void registerClient(IWailaClientRegistration reg) {
        reg.registerBlockComponent(DecayingBlockComponentProvider.INSTANCE, EatableBoxBlock.class);
        reg.registerBlockComponent(DecayingBlockComponentProvider.INSTANCE, FoodBlock.class);
        reg.registerBlockComponent(DecayingBlockComponentProvider.INSTANCE, PieBlock.class);
        reg.registerBlockComponent(DecayingBlockComponentProvider.INSTANCE, StackableBlock.class);
        reg.registerBlockComponent(DecayingBlockComponentProvider.INSTANCE, StackableEatableBlock.class);

        reg.registerBlockComponent(BagBlockComponentProvider.INSTANCE, BagBlock.class);
    }
}
