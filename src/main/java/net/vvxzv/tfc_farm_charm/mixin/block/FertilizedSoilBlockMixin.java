package net.vvxzv.tfc_farm_charm.mixin.block;

import net.dries007.tfc.common.blockentities.IFarmland;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.farm_and_charm.core.block.FertilizedSoilBlock;
import net.vvxzv.tfc_farm_charm.common.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FertilizedSoilBlock.class)
public abstract class FertilizedSoilBlockMixin {

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", ordinal = 0))
    private boolean addFarmlandNutrients(Level level, BlockPos pPos, BlockState pNewState, int pFlags) {
        boolean returnValue = level.setBlock(pPos, pNewState, 3);
        BlockPos min = pPos.offset(-4, -4, -4);
        BlockPos max = pPos.offset(4, 4, 4);
        if (level.hasChunksAt(min, max)) {
            for(BlockPos blockPos : BlockPos.betweenClosed(min, max)) {
                BlockEntity be = level.getBlockEntity(blockPos);
                if (be instanceof IFarmland farmland) {
                    int which = level.random.nextInt(3);
                    float nut = level.random.nextFloat() * 0.8F;
                    Utils.receiveNutrients(farmland, 1.0F, which == 0 ? nut : 0.0F, which == 1 ? nut : 0.0F, which == 2 ? nut : 0.0F);
                }
            }
        }
        return returnValue;
    }

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/satisfy/farm_and_charm/core/block/FertilizedSoilBlock;applyBoneMealEffect(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"), remap = false)
    private void removeBoneMealEffect(FertilizedSoilBlock instance, Level level, BlockPos centerPos) {

    }
}
