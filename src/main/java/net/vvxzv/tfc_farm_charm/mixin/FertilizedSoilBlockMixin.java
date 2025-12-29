package net.vvxzv.tfc_farm_charm.mixin;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blockentities.IFarmland;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.farm_and_charm.core.block.FertilizedSoilBlock;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.vvxzv.tfc_farm_charm.common.registry.TFCFCBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FertilizedSoilBlock.class)
public class FertilizedSoilBlockMixin {
    @Unique
    private static final FarmlandBlockEntity.NutrientType tfc_farm_charm$N = FarmlandBlockEntity.NutrientType.NITROGEN;
    @Unique
    private static final FarmlandBlockEntity.NutrientType tfc_farm_charm$P = FarmlandBlockEntity.NutrientType.PHOSPHOROUS;
    @Unique
    private static final FarmlandBlockEntity.NutrientType tfc_farm_charm$K = FarmlandBlockEntity.NutrientType.POTASSIUM;

    /**
     * @author Vvxzv
     * @reason compat
     */
    @Overwrite
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        boolean hasSugarCaneAbove = level.getBlockState(pos.above()).is(Blocks.SUGAR_CANE);
        if (itemStack.getItem() == ObjectRegistry.PITCHFORK.get()) {
            if (hasSugarCaneAbove) {
                return InteractionResult.PASS;
            }
            else {
                int newSize = state.getValue(FertilizedSoilBlock.SIZE) - 1;
                if (newSize < 0) {
                    level.removeBlock(pos, false);
                }
                else {
                    level.setBlock(pos, state.setValue(FertilizedSoilBlock.SIZE, newSize), 3);
                    BlockPos min = pos.offset(-4, -4, -4);
                    BlockPos max = pos.offset(4, 4, 4);
                    if (level.hasChunksAt(min, max)) {
                        for(BlockPos blockPos : BlockPos.betweenClosed(min, max)) {
                            BlockEntity be = level.getBlockEntity(blockPos);
                            if (be instanceof IFarmland farmland) {
                                int which = level.random.nextInt(3);
                                float nut = level.random.nextFloat() * 0.2F;
                                this.tfc_farm_charm$receiveNutrients(farmland, 1.0F, which == 0 ? nut : 0.0F, which == 1 ? nut : 0.0F, which == 2 ? nut : 0.0F);
                            }
                        }
                    }
                }

                this.tfc_farm_charm$spawnParticles(level, pos, state, false);
                level.playSound(null, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                itemStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                return InteractionResult.SUCCESS;
            }
        } else if (itemStack.getItem() instanceof HoeItem) {
            if (hasSugarCaneAbove) {
                return InteractionResult.PASS;
            }
            else {
                int currentSize = state.getValue(FertilizedSoilBlock.SIZE);
                if (currentSize == 3) {
                    level.setBlock(pos, TFCFCBlock.FERTILIZED_FARMLAND.get().defaultBlockState(), 3);
                    if (!player.isCreative()) {
                        itemStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }

                    level.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Unique
    private void tfc_farm_charm$receiveNutrients(IFarmland farmland, float cap, float nitrogen, float phosphorous, float potassium) {
        float n = farmland.getNutrient(tfc_farm_charm$N);
        if (n < cap) {
            farmland.setNutrient(tfc_farm_charm$N, Math.min(n + nitrogen, cap));
        }

        float p = farmland.getNutrient(tfc_farm_charm$P);
        if (p < cap) {
            farmland.setNutrient(tfc_farm_charm$P, Math.min(p + phosphorous, cap));
        }

        float k = farmland.getNutrient(tfc_farm_charm$K);
        if (k < cap) {
            farmland.setNutrient(tfc_farm_charm$K, Math.min(k + potassium, cap));
        }

    }

    @Unique
    private void tfc_farm_charm$spawnParticles(Level level, BlockPos pos, BlockState state, boolean happy) {
        if (!level.isClientSide) {
            ServerLevel server = (ServerLevel)level;
            server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), (double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F, 20, 0.5F, 0.5F, 0.5F, 0.2);
            if (happy) {
                server.sendParticles(ParticleTypes.HAPPY_VILLAGER, (double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.25F, (double)pos.getZ() + (double)0.5F, 4, 0.25F, 0.1, 0.25F, 0.01);
            }
        }
    }
}
