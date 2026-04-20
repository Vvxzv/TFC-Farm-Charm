package net.vvxzv.tfc_farm_charm.mixin.block.food;

import net.dries007.tfc.util.Helpers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.farm_and_charm.core.block.StackableEatableBlock;
import net.vvxzv.tfc_farm_charm.common.block.entity.DecayingFoodBlockEntity;
import net.vvxzv.tfc_farm_charm.common.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StackableEatableBlock.class)
public class StackableEatableBlockMixin extends Block implements EntityBlock {

    @Final
    @Shadow(remap = false)
    private static IntegerProperty STACK_PROPERTY;

    public StackableEatableBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new DecayingFoodBlockEntity(blockPos, blockState);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void useRottenBlock(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if(level.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decaying && decaying.isRotten()) {
            player.displayClientMessage(Component.translatable("tfc_farm_charm.eat.rotten_block").withStyle(ChatFormatting.GRAY), true);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"))
    private void removeEat(FoodData instance, int pFoodLevelModifier, float pSaturationLevelModifier, BlockState state, Level level, BlockPos pos, Player player) {
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;", ordinal = 0))
    private void eat(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if(level.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decaying) {
            ItemStack stack = decaying.copyStack();
            player.eat(level, stack);
        }
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", ordinal = 1))
    private void addStack(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if(level.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decaying) {
            ItemStack stack = player.getItemInHand(hand);
            ItemStack setItem = Utils.mergeFoodDecay(stack, decaying.copyStack());
            if (setItem != null) {
                decaying.setStack(setItem);
            }
        }
    }

    @Redirect(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/satisfy/farm_and_charm/core/util/GeneralUtil;spawnSlice(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;DDDDDD)V"
            ),
            remap = false
    )
    private void dropStack(
            Level level,
            ItemStack stack,
            double x, double y, double z,
            double xMotion, double yMotion, double zMotion,
            BlockState state,
            Level world,
            BlockPos pos
    ) {
        if(level.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decaying) {
            ItemStack dropItem = decaying.copyStack();
            Helpers.spawnItem(level, pos, dropItem);
        }
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
    private void lastEatRemoveBlock(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if(level.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decaying) {
            decaying.setStack(ItemStack.EMPTY);
        }
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DecayingFoodBlockEntity decaying) {
            decaying.setStack(stack);
        }
    }

    @Override
    public boolean onDestroyedByPlayer(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, boolean willHarvest, @NotNull FluidState fluid) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DecayingFoodBlockEntity decaying) {
            if (player.isCreative()) {
                decaying.setStack(ItemStack.EMPTY);
            }
        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public void onRemove(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (level.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decaying) {
            if (!Helpers.isBlock(state, newState.getBlock())) {
                ItemStack stack = decaying.getStack();
                stack.setCount(state.getValue(STACK_PROPERTY));
                Helpers.spawnItem(level, pos, stack);
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }
}
