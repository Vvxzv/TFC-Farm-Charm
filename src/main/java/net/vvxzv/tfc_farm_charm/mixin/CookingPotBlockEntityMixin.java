package net.vvxzv.tfc_farm_charm.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.satisfy.farm_and_charm.core.block.entity.CookingPotBlockEntity;
import net.vvxzv.tfc_farm_charm.common.registry.BlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CookingPotBlockEntity.class)
public class CookingPotBlockEntityMixin {
    @Inject(method = "isBeingBurned", at = @At("RETURN"), remap = false, cancellable = true)
    public void isBeingBurned(CallbackInfoReturnable<Boolean> cir) {
        Level level = (Level)((CookingPotBlockEntity)(Object)this).getLevel();
        BlockPos pos = ((CookingPotBlockEntity)(Object)this).getBlockPos();
        BlockState belowState = level.getBlockState(pos.below());
        if (belowState.is(BlockTags.HEAT_SOURCE)) {
            cir.setReturnValue(true);
            return;
        }
        boolean hasLitProperty = belowState.hasProperty(BlockStateProperties.LIT);
        boolean returnValue = hasLitProperty?belowState.getValue(BlockStateProperties.LIT):false;
        cir.setReturnValue(returnValue);
    }
}
