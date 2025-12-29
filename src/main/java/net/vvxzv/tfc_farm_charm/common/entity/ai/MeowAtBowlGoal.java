package net.vvxzv.tfc_farm_charm.common.entity.ai;

import net.dries007.tfc.common.entities.livestock.pet.TFCCat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.satisfy.farm_and_charm.core.block.entity.PetBowlBlockEntity;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;

import java.util.EnumSet;

public class MeowAtBowlGoal extends Goal {
    private final TFCCat cat;
    private BlockPos bowlPos;
    private int meowTicks;
    private long lastCheckTime;
    private boolean active;
    private static final int CHECK_INTERVAL_TICKS = 40;
    private static final int MAX_MEOW_TICKS = 300;
    private static final int MEOW_INTERVAL = 60;
    private static final int ANGRY_PARTICLE_INTERVAL = 100;
    private static final double NAVIGATION_SPEED = (double)1.0F;
    private static final double CLOSE_ENOUGH_DIST = 1.1;
    private static final double LOOK_OFFSET = (double)0.5F;
    private static final long MORNING_START = 5800L;
    private static final long MORNING_END = 6200L;
    private static final long EVENING_START = 11500L;
    private static final long EVENING_END = 12500L;

    public MeowAtBowlGoal(TFCCat cat) {
        this.cat = cat;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.meowTicks = 0;
        this.lastCheckTime = -1L;
        this.active = false;
    }

    public boolean canUse() {
        if (this.cat.getOwnerUUID() != null && !this.cat.isSitting()) {
            Level level = this.cat.level();
            if (level instanceof ServerLevel) {
                ServerLevel server = (ServerLevel)level;
                long gameTime = server.getGameTime();
                if (this.lastCheckTime != -1L && gameTime - this.lastCheckTime < 40L) {
                    return false;
                } else {
                    this.lastCheckTime = gameTime;
                    long timeOfDay = server.getDayTime() % 24000L;
                    if (timeOfDay >= MORNING_START && (timeOfDay <= MORNING_END || timeOfDay >= EVENING_START) && timeOfDay <= EVENING_END) {
                        BlockPos catPos = this.cat.blockPosition();
                        double closestDistance = Double.MAX_VALUE;
                        BlockPos closest = null;

                        for(BlockPos pos : BlockPos.betweenClosed(catPos.offset(-32, -4, -32), catPos.offset(32, 4, 32))) {
                            if (level.getBlockState(pos).is(ObjectRegistry.PET_BOWL.get()) && level.getBlockState(pos).hasBlockEntity()) {
                                BlockEntity be = level.getBlockEntity(pos);
                                if (be instanceof PetBowlBlockEntity) {
                                    PetBowlBlockEntity bowl = (PetBowlBlockEntity)be;
                                    if (bowl.isEmpty() && bowl.canBeUsedBy(this.cat)) {
                                        double dist = this.cat.position().distanceToSqr(Vec3.atCenterOf(pos));
                                        if (dist < closestDistance) {
                                            closestDistance = dist;
                                            closest = pos.immutable();
                                        }
                                    }
                                }
                            }
                        }

                        if (closest != null) {
                            this.bowlPos = closest;
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean canContinueToUse() {
        if (this.active && this.meowTicks < 300 && this.bowlPos != null) {
            Level level = this.cat.level();
            if (!(level instanceof ServerLevel)) {
                return false;
            } else {
                ServerLevel server = (ServerLevel)level;
                BlockEntity be = server.getBlockEntity(this.bowlPos);
                boolean var10000;
                if (be instanceof PetBowlBlockEntity) {
                    PetBowlBlockEntity bowl = (PetBowlBlockEntity)be;
                    if (bowl.isEmpty() && bowl.canBeUsedBy(this.cat)) {
                        var10000 = true;
                        return var10000;
                    }
                }

                var10000 = false;
                return var10000;
            }
        } else {
            return false;
        }
    }

    public boolean isInterruptable() {
        return false;
    }

    public void start() {
        if (this.bowlPos != null) {
            if (!this.isNearBowl()) {
                this.cat.getNavigation().moveTo((double)this.bowlPos.getX() + (double)0.5F, this.bowlPos.getY(), (double)this.bowlPos.getZ() + (double)0.5F, 1.0F);
            }

            this.meowTicks = 0;
            this.active = true;
        }

    }

    public void tick() {
        if (this.bowlPos == null) {
            this.stop();
        } else {
            Level level = this.cat.level();
            if (level instanceof ServerLevel) {
                ServerLevel server = (ServerLevel)level;
                BlockEntity be = server.getBlockEntity(this.bowlPos);
                if (be instanceof PetBowlBlockEntity) {
                    PetBowlBlockEntity bowl = (PetBowlBlockEntity)be;
                    if (bowl.isEmpty()) {
                        if (this.isNearBowl()) {
                            if (this.cat.getNavigation().isInProgress()) {
                                this.cat.getNavigation().stop();
                            }

                            if (!this.cat.isSitting()) {
                                this.cat.setSitting(true);
                            }

                            this.cat.getLookControl().setLookAt((double)this.bowlPos.getX() + (double)0.5F, (double)this.bowlPos.getY() + (double)0.5F, (double)this.bowlPos.getZ() + (double)0.5F);
                        } else if (!this.cat.getNavigation().isInProgress()) {
                            this.cat.getNavigation().moveTo((double)this.bowlPos.getX() + (double)0.5F, this.bowlPos.getY(), (double)this.bowlPos.getZ() + (double)0.5F, 1.0F);
                        }

                        if (this.meowTicks % 60 == 0) {
                            this.cat.playSound(SoundEvents.CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
                        }

                        if (this.meowTicks % 100 == 0) {
                            Vec3 pos = this.cat.position().add(0.0F, 0.5F, 0.0F);
                            server.sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.x, pos.y, pos.z, 6, 0.3, 0.3, 0.3, 0.01);
                        }

                        if (++this.meowTicks >= 300) {
                            this.cat.playSound(SoundEvents.CAT_HISS, 1.0F, 1.0F);
                            Vec3 pos = this.cat.position().add(0.0F, 0.5F, 0.0F);
                            server.sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.x, pos.y, pos.z, 15, 0.3, 0.3, 0.3, 0.01);
                            this.stop();
                        }

                        return;
                    }
                }

                this.stop();
            } else {
                this.stop();
            }
        }
    }

    public void stop() {
        this.bowlPos = null;
        this.meowTicks = 0;
        this.active = false;
        if (this.cat.isSitting()) {
            this.cat.setSitting(false);
        }

    }

    private boolean isNearBowl() {
        return this.bowlPos != null && this.cat.position().distanceToSqr(Vec3.atCenterOf(this.bowlPos)) < 1.2100000000000002;
    }
}
