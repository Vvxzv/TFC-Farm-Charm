package net.vvxzv.tfc_farm_charm.mixin.block.food;

import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.common.capabilities.food.TFCFoodData;
import net.dries007.tfc.util.Helpers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.bakery.core.block.BreadBasketBlock;
import net.satisfy.farm_and_charm.core.block.FacingBlock;
import net.vvxzv.tfc_farm_charm.common.block.entity.DecayingFoodBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BreadBasketBlock.class)
public class BreadBasketBlockMixin extends FacingBlock {

    @Final
    @Shadow(remap = false)
    public static IntegerProperty BITES;

    public BreadBasketBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void useRottenBlock(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if(level.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decaying && decaying.isRotten()) {
            player.displayClientMessage(Component.translatable("tfc_farm_charm.eat.rotten_block").withStyle(ChatFormatting.GRAY), true);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    @Redirect(method = "tryEat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"))
    private void removeEat(FoodData instance, int pFoodLevelModifier, float pSaturationLevelModifier) {

    }

    @Redirect(method = "tryEat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private void removeEatSound(Level instance, Player pPlayer, BlockPos pPos, SoundEvent pSound, SoundSource pCategory, float pVolume, float pPitch) {

    }

    @Inject(method = "tryEat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private void eat(Level level, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<InteractionResult> cir) {
        level.playSound(
                null,
                pos,
                SoundEvents.FOX_EAT,
                SoundSource.PLAYERS,
                0.5f,
                level.getRandom().nextFloat() * 0.1f + 0.9f
        );
        if(level.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decaying) {
            if(player.getFoodData() instanceof TFCFoodData foodData) {
                IFood food = FoodCapability.get(decaying.copyStack());
                if (food != null) {
                    foodData.eat(food);
                }
            }
        }
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingFoodBlockEntity decaying) {
            if (!Helpers.isBlock(state, newState.getBlock()) && state.getValue(BITES) == 0) {
                Helpers.spawnItem(level, pos, decaying.getStack());
            }
        }

        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
            level.removeBlockEntity(pos);
        }
    }
}
