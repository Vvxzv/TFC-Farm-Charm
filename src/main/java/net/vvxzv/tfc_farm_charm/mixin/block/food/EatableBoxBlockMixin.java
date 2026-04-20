package net.vvxzv.tfc_farm_charm.mixin.block.food;

import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.common.capabilities.food.TFCFoodData;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.farm_and_charm.core.block.EatableBoxBlock;
import net.satisfy.farm_and_charm.core.block.FacingBlock;
import net.vvxzv.tfc_farm_charm.common.block.entity.DecayingFoodBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EatableBoxBlock.class)
public class EatableBoxBlockMixin extends FacingBlock implements EntityBlock {

    @Final
    @Shadow(remap = false)
    public static IntegerProperty CUTS;

    public EatableBoxBlockMixin(Properties settings) {
        super(settings);
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

    @Redirect(method = "tryEat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"))
    private void eat(FoodData instance, int pFoodLevelModifier, float pSaturationLevelModifier, Level level, BlockPos pos, BlockState state, Player player) {
        if(level.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decaying) {
            if(player.getFoodData() instanceof TFCFoodData foodData) {
                IFood food = FoodCapability.get(decaying.copyStack());
                if (food != null) {
                    foodData.eat(food);
                }
            }
        }
    }

    @ModifyArg(method = "tryEat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V"), index = 4)
    private ItemStack dropPaper(ItemStack pItemStack) {
        return new ItemStack(Items.PAPER);
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
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingFoodBlockEntity decaying) {
            if (!Helpers.isBlock(state, newState.getBlock()) && state.getValue(CUTS) == 0) {
                Helpers.spawnItem(level, pos, decaying.getStack());
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }
}
