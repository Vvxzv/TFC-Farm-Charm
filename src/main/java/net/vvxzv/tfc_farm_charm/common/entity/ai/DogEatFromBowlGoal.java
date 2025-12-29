package net.vvxzv.tfc_farm_charm.common.entity.ai;

import net.dries007.tfc.common.entities.livestock.pet.Dog;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.farm_and_charm.core.block.PetBowlBlock;
import net.satisfy.farm_and_charm.core.block.entity.PetBowlBlockEntity;
import net.satisfy.farm_and_charm.core.entity.BowlAccessor;
import net.satisfy.farm_and_charm.core.registry.MobEffectRegistry;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import org.joml.Vector3f;

public class DogEatFromBowlGoal extends Goal {
    private final Dog dog;
    private BlockPos targetBowl;
    private Vector3f targetVec;
    private int eatTicks;
    private ItemStack foodStack;

    public DogEatFromBowlGoal(Dog dog) {
        this.dog = dog;
    }

    public boolean canUse() {
        if (this.dog.getOwnerUUID() != null && this.dog.isSitting()) {
            Level level = this.dog.level();
            BlockPos dogPos = this.dog.blockPosition();
            double closestDist = Double.MAX_VALUE;
            BlockPos closest = null;
            ItemStack candidateFood = ItemStack.EMPTY;

            for(BlockPos pos : BlockPos.betweenClosed(dogPos.offset(-16, -4, -16), dogPos.offset(16, 4, 16))) {
                BlockState state = level.getBlockState(pos);
                if (state.is(ObjectRegistry.PET_BOWL.get()) && state.hasProperty(PetBowlBlock.FOOD_TYPE) && state.getValue(PetBowlBlock.FOOD_TYPE) == GeneralUtil.FoodType.DOG) {
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof PetBowlBlockEntity bowl) {
                        if (!bowl.isEmpty() && bowl.canBeUsedBy(this.dog)) {
                            double dist = pos.distSqr(dogPos);
                            if (dist < closestDist) {
                                closestDist = dist;
                                closest = pos.immutable();
                                candidateFood = bowl.getItem(0).copy();
                            }
                        }
                    }
                }
            }

            if (closest != null) {
                this.targetBowl = closest;
                this.targetVec = new Vector3f((float)closest.getX() + 0.5F, (float)closest.getY(), (float)closest.getZ() + 0.5F);
                this.foodStack = candidateFood;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void start() {
        this.dog.getNavigation().moveTo(this.targetVec.x(), this.targetVec.y(), this.targetVec.z(), 1.0F);
        this.eatTicks = 0;
    }

    public boolean canContinueToUse() {
        if (this.targetBowl == null) {
            return false;
        } else {
            Level level = this.dog.level();
            BlockState state = level.getBlockState(this.targetBowl);
            if (state.getBlock() instanceof PetBowlBlock && state.hasProperty(PetBowlBlock.FOOD_TYPE)) {
                BlockEntity be = level.getBlockEntity(this.targetBowl);
                if (be instanceof PetBowlBlockEntity bowl) {
                    if (!bowl.isEmpty() && bowl.canBeUsedBy(this.dog)) {
                        float distSqr = this.targetVec.distanceSquared((float)this.dog.getX(), (float)this.dog.getY(), (float)this.dog.getZ());
                        return distSqr > 4.0F || this.eatTicks < 60;
                    }
                }

                return false;
            } else {
                return false;
            }
        }
    }

    public void tick() {
        if (this.targetBowl == null) {
            this.stop();
        } else {
            Level level = this.dog.level();
            BlockEntity be = level.getBlockEntity(this.targetBowl);
            if (be instanceof PetBowlBlockEntity bowl) {
                if (!bowl.isEmpty()) {
                    float distSqr = this.targetVec.distanceSquared((float)this.dog.getX(), (float)this.dog.getY(), (float)this.dog.getZ());
                    if (distSqr <= 4.0F && this.dog.isSitting()) {
                        ++this.eatTicks;
                        this.dog.getLookControl().setLookAt(this.targetVec.x(), this.targetVec.y(), this.targetVec.z());
                        if (!level.isClientSide && this.eatTicks <= 40) {
                            ParticleOptions particle = this.getParticleFromFood();
                            if (particle != null) {
                                ((ServerLevel)level).sendParticles(particle, this.targetVec.x(), this.targetVec.y() + 0.09375F, this.targetVec.z(), 3, 0.2, 0.2, 0.2, 0.05);
                            }

                            if (this.eatTicks % 10 == 0) {
                                level.playSound(null, this.dog.blockPosition(), SoundEvents.WOLF_GROWL, SoundSource.NEUTRAL, 0.4F, 0.4F);
                            }
                        }

                        if (this.eatTicks == 40) {
                            bowl.decreaseFood();
                            ((BowlAccessor.StayNearBowl)this.dog).farmAndCharm$setStayCenter(this.targetBowl);
                            if (!level.isClientSide) {
                                ((ServerLevel)level).sendParticles(ParticleTypes.HEART, this.dog.getX(), this.dog.getY() + (double)0.5F, this.dog.getZ(), 3, 0.3, 0.3, 0.3, 0.01);
                                level.playSound(null, this.dog.blockPosition(), SoundEvents.WOLF_HOWL, this.dog.getSoundSource(), 0.4F, 0.4F);
                                BlockState old = level.getBlockState(this.targetBowl);
                                if (old.getBlock() instanceof PetBowlBlock && old.hasProperty(PetBowlBlock.FOOD_TYPE)) {
                                    level.setBlockAndUpdate(this.targetBowl, old.setValue(PetBowlBlock.FOOD_TYPE, GeneralUtil.FoodType.NONE));
                                    this.dog.heal(20.0F);
                                    this.dog.addEffect(new MobEffectInstance(MobEffectRegistry.DOG_FOOD.get(), 3600, 0));
                                }
                            }
                        }
                    }
                    return;
                }
            }

            this.stop();
        }
    }

    public void stop() {
        this.targetBowl = null;
        this.targetVec = null;
        this.eatTicks = 0;
        this.foodStack = null;
        this.dog.setSitting(true);
    }

    private ParticleOptions getParticleFromFood() {
        return this.foodStack != null && !this.foodStack.isEmpty() ? new ItemParticleOption(ParticleTypes.ITEM, this.foodStack) : null;
    }
}
