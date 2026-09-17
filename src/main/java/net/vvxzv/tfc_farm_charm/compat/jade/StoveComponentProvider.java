package net.vvxzv.tfc_farm_charm.compat.jade;

import net.dries007.tfc.common.capabilities.heat.Heat;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltips;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;
import net.vvxzv.tfc_farm_charm.common.utils.IStove;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum StoveComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockEntity blockEntity = blockAccessor.getBlockEntity();
        if(blockEntity instanceof IStove stove) {
            float currentTemp = stove.getTemperature();
            Heat heatLevel = Heat.getHeat(currentTemp);
            if (heatLevel != null) {
                BlockEntityTooltips.heat(iTooltip::add, currentTemp);
            }
        }

    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(TFCFarmCharm.MODID, "stove");
    }
}
