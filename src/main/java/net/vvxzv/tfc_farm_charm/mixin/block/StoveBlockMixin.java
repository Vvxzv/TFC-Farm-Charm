package net.vvxzv.tfc_farm_charm.mixin.block;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.farm_and_charm.core.block.StoveBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StoveBlock.class)
public class StoveBlockMixin extends Block {

    public StoveBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    private void getStateForPlacement(BlockPlaceContext ctx, CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(this.defaultBlockState().setValue(StoveBlock.FACING, ctx.getHorizontalDirection().getOpposite()));
    }
}
