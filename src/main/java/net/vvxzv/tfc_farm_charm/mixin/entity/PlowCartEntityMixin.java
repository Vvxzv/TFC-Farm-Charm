package net.vvxzv.tfc_farm_charm.mixin.entity;

import net.dries007.tfc.common.blocks.soil.ConnectedGrassBlock;
import net.dries007.tfc.common.blocks.soil.DirtBlock;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.farm_and_charm.core.entity.AbstractTowableEntity;
import net.satisfy.farm_and_charm.core.entity.PlowCartEntity;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlowCartEntity.class)
public abstract class PlowCartEntityMixin extends AbstractTowableEntity {

    public PlowCartEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(method = "handleClientSide", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 1))
    private BlockState setClientSoilFarmland(Block instance) {
        return net.vvxzv.tfc_farm_charm.common.registry.Blocks.FERTILIZED_FARMLAND.get().defaultBlockState();
    }

    /**
     * @author Vvxzv
     * @reason Add farmland compat
     */
    @Overwrite(remap = false)
    private void handleServerSide() {
        BlockPos currentPos = this.blockPosition();
        BlockPos[] positions = new BlockPos[]{currentPos.below(), currentPos.below().east()};

        for (BlockPos pos : positions) {
            BlockState blockState = this.level().getBlockState(pos);
            BlockState newBlockState = null;
            if (blockState.is(Blocks.GRASS_BLOCK) || blockState.is(Blocks.DIRT)) {
                newBlockState = Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, 0);
            } else if (blockState.is(ObjectRegistry.FERTILIZED_SOIL_BLOCK.get())) {
                newBlockState = net.vvxzv.tfc_farm_charm.common.registry.Blocks.FERTILIZED_FARMLAND.get().defaultBlockState();
            } else if (blockState.getBlock() instanceof DirtBlock || blockState.getBlock() instanceof ConnectedGrassBlock) {
                SoilBlockType.Variant[] variants = SoilBlockType.Variant.values();
                SoilBlockType.Variant variant = null;
                for (SoilBlockType.Variant v : variants) {
                    Block dirtBlock = v.getBlock(SoilBlockType.DIRT).get();
                    Block grassBlock = v.getBlock(SoilBlockType.GRASS).get();
                    if (blockState.is(dirtBlock) || blockState.is(grassBlock)) {
                        variant = v;
                        break;
                    }
                }

                if (variant != null) {
                    newBlockState = variant.getBlock(SoilBlockType.FARMLAND).get().defaultBlockState();
                }
            }

            if (newBlockState != null) {
                this.level().setBlock(pos, newBlockState, 3);
            }

            BlockPos cropPos = pos.above();
            BlockState cropState = this.level().getBlockState(cropPos);
            if (cropState.getBlock() instanceof CropBlock cropBlock) {
                if (cropBlock.isMaxAge(cropState)) {
                    this.level().destroyBlock(cropPos, true);
                }
            }
        }
    }
}
