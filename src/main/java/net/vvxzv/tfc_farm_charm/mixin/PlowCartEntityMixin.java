package net.vvxzv.tfc_farm_charm.mixin;

import net.dries007.tfc.common.blocks.soil.ConnectedGrassBlock;
import net.dries007.tfc.common.blocks.soil.DirtBlock;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.farm_and_charm.core.entity.AbstractTowableEntity;
import net.satisfy.farm_and_charm.core.entity.PlowCartEntity;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.vvxzv.tfc_farm_charm.common.registry.TFCFCBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlowCartEntity.class)
public abstract class PlowCartEntityMixin extends AbstractTowableEntity {

    public PlowCartEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @author Vvxzv
     * @reason Compat
     */
    @Overwrite
    public void tick() {
        PlowCartEntity pce = (PlowCartEntity)(Object) this;
        AbstractTowableEntityAccessor accessor = (AbstractTowableEntityAccessor) pce;
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0F, -0.08F, 0.0F));
        }

        super.tick();
        accessor.invokeTickLerp();
        if (this.driver != null) {
            this.pulledTick();
        }

        accessor.getLeftWheel().tick();
        accessor.getRightWheel().tick();
        this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.level().isClientSide()) {
            tfc_farm_charm$handleClientSide();
        }
        else {
            tfc_farm_charm$handleServerSide();
        }
    }

    @Unique
    private void tfc_farm_charm$handleClientSide() {
        BlockPos currentPos = this.blockPosition();
        BlockPos[] positions = new BlockPos[]{currentPos.below(), currentPos.below().east()};

        for(BlockPos pos : positions) {
            BlockState blockState = this.level().getBlockState(pos);
            BlockState newBlockState = null;
            if (blockState.is(ObjectRegistry.FERTILIZED_SOIL_BLOCK.get())) {
                newBlockState = TFCFCBlock.FERTILIZED_FARMLAND.get().defaultBlockState();
            }
            else if (blockState.is(ObjectRegistry.FERTILIZED_FARM_BLOCK.get())) {
                newBlockState = TFCFCBlock.FERTILIZED_FARMLAND.get().defaultBlockState();
            }
            else if (blockState.is(Blocks.GRASS_BLOCK) || blockState.is(Blocks.DIRT)) {
                newBlockState = Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, 0);
            }
            else if (blockState.getBlock() instanceof DirtBlock || blockState.getBlock() instanceof ConnectedGrassBlock) {
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
                for(int i = 0; i < 200; ++i) {
                    double x = (double)pos.getX() + this.level().random.nextDouble();
                    double y = (double)pos.getY() + this.level().random.nextDouble();
                    double z = (double)pos.getZ() + this.level().random.nextDouble();
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, newBlockState), x, y, z, 0.0F, 0.0F, 0.0F);
                }
            }
        }

    }

    @Unique
    private void tfc_farm_charm$handleServerSide() {
        BlockPos currentPos = this.blockPosition();
        BlockPos[] positions = new BlockPos[]{currentPos.below(), currentPos.below().east()};

        for(BlockPos pos : positions) {
            if(!this.level().getBlockState(pos.above()).is(Blocks.AIR)){
                return;
            }
            BlockState blockState = this.level().getBlockState(pos);
            BlockState newBlockState = null;

            if (blockState.is(ObjectRegistry.FERTILIZED_SOIL_BLOCK.get())) {
                newBlockState = TFCFCBlock.FERTILIZED_FARMLAND.get().defaultBlockState();
            }
            else if (blockState.is(ObjectRegistry.FERTILIZED_FARM_BLOCK.get())) {
                newBlockState = TFCFCBlock.FERTILIZED_FARMLAND.get().defaultBlockState();
            }
            else if (blockState.is(Blocks.GRASS_BLOCK) || blockState.is(Blocks.DIRT)) {
                newBlockState = Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, 0);
            }
            else if (blockState.getBlock() instanceof DirtBlock || blockState.getBlock() instanceof ConnectedGrassBlock) {
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
            Block var12 = cropState.getBlock();
            if (var12 instanceof CropBlock cropBlock) {
                if (cropBlock.isMaxAge(cropState)) {
                    BlockState newCropState = cropBlock.getStateForAge(0);
                    this.level().setBlock(cropPos, newCropState, 3);
                    this.level().updateNeighborsAt(cropPos, newCropState.getBlock());
                    if (this.level() instanceof ServerLevel serverLevel) {

                        for(ItemStack drop : Block.getDrops(cropState, serverLevel, cropPos, null)) {
                            if (!drop.isEmpty()) {
                                double dropX = (double)cropPos.getX() + (double)0.5F + (this.level().random.nextDouble() - (double)0.5F) * (double)0.5F;
                                double dropY = (double)cropPos.getY() + (double)1.0F;
                                double dropZ = (double)cropPos.getZ() + (double)0.5F + (this.level().random.nextDouble() - (double)0.5F) * (double)0.5F;
                                ItemEntity itemEntity = new ItemEntity(this.level(), dropX, dropY, dropZ, drop);
                                this.level().addFreshEntity(itemEntity);
                            }
                        }
                    }
                }
            }
        }

    }
}
