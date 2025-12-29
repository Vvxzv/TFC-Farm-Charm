package net.vvxzv.tfc_farm_charm.common.entity.ai;

import net.dries007.tfc.common.entities.livestock.pet.Dog;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.satisfy.farm_and_charm.core.block.entity.PetBowlBlockEntity;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;

import java.util.EnumSet;
import java.util.List;

public class WhineAtBowlGoal extends Goal {
    private final Dog dog;
    private BlockPos bowlPos;
    private Vec3 lastTargetPos;
    private int whineTicks;
    private int fadeOutTicks;
    private long lastScanTick;
    private long lastWhineSoundTick;
    private boolean active;
    private static final int SCAN_INTERVAL_TICKS = 40;
    private static final int MAX_WHINE_TICKS = 300;
    private static final int WHINE_INTERVAL = 60;
    private static final int ANGRY_PARTICLE_INTERVAL = 100;
    private static final int WHINE_PARTICLE_COUNT = 6;
    private static final int FINAL_PARTICLE_COUNT = 15;
    private static final int FADE_OUT_DURATION = 30;
    private static final double BASE_SPEED = (double)1.0F;
    private static final double EVENING_SPEED_FACTOR = 0.8;
    private static final double CLOSE_ENOUGH_DIST = 1.1;
    private static final float BASE_VOLUME = 0.4F;
    private static final float BASE_PITCH = 0.4F;
    private static final int LOOK_YAW = 10;
    private static final int LOOK_PITCH = 30;
    private static final int RANGE_XZ = 10;
    private static final int RANGE_Y = 2;
    private static final long MORNING_START = 5800L;
    private static final long MORNING_END = 6200L;
    private static final long EVENING_START = 11500L;
    private static final long EVENING_END = 12500L;
    private static final double NAVIGATION_RECALC_THRESHOLD_SQR = (double)0.5F;
    private static final List<SoundEvent> WHINE_SOUNDS = List.of(SoundEvents.WOLF_WHINE, SoundEvents.WOLF_PANT);

    public WhineAtBowlGoal(Dog dog){
        this.dog = dog;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.whineTicks = 0;
        this.lastScanTick = -40L;
        this.lastWhineSoundTick = -60L;
        this.active = false;
        this.lastTargetPos = null;
    }

    @Override
    public boolean canUse() {
        if (this.dog.isAlive() && this.dog.getOwnerUUID() != null && !this.dog.isSitting()) {
            Level level = this.dog.level();
            if (level instanceof ServerLevel) {
                ServerLevel server = (ServerLevel)level;
                if (!this.shouldScanForBowl(server.getGameTime())) {
                    return false;
                } else if (!this.isValidWhineTime(server.getDayTime())) {
                    return false;
                } else {
                    BlockPos wolfPos = this.dog.blockPosition();
                    double closestDistance = Double.MAX_VALUE;
                    BlockPos closest = null;

                    for(BlockPos pos : BlockPos.betweenClosed(wolfPos.offset(-10, -2, -10), wolfPos.offset(10, 2, 10))) {
                        if (this.dog.level().getBlockState(pos).is(ObjectRegistry.PET_BOWL.get())) {
                            BlockEntity be = this.dog.level().getBlockEntity(pos);
                            if (be instanceof PetBowlBlockEntity) {
                                PetBowlBlockEntity bowl = (PetBowlBlockEntity)be;
                                if (bowl.isEmpty() && bowl.canBeUsedBy(this.dog)) {
                                    double dist = this.dog.position().distanceToSqr(Vec3.atCenterOf(pos));
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
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean canContinueToUse() {
        if (this.active && this.dog.isAlive() && this.bowlPos != null) {
            Level var2 = this.dog.level();
            if (!(var2 instanceof ServerLevel)) {
                return false;
            } else {
                ServerLevel server = (ServerLevel)var2;
                BlockEntity be = server.getBlockEntity(this.bowlPos);
                boolean var10000;
                if (be instanceof PetBowlBlockEntity) {
                    PetBowlBlockEntity bowl = (PetBowlBlockEntity)be;
                    if (bowl.isEmpty() && bowl.canBeUsedBy(this.dog)) {
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
        Level var2 = this.dog.level();
        if (var2 instanceof ServerLevel server) {
            BlockEntity be = server.getBlockEntity(this.bowlPos);
            if (be instanceof PetBowlBlockEntity bowl) {
                if (bowl.isEmpty() && bowl.canBeUsedBy(this.dog)) {
                    this.dog.setSitting(false);
                    this.moveToBowl(server.getDayTime());
                    this.whineTicks = 0;
                    this.fadeOutTicks = 0;
                    this.lastWhineSoundTick = -60L;
                    this.active = true;
                    return;
                }
            }

            this.stop();
        }
    }

    public void tick() {
        Level var2 = this.dog.level();
        if (var2 instanceof ServerLevel server) {
            if (this.dog.isAlive()) {
                BlockEntity be = server.getBlockEntity(this.bowlPos);
                if (be instanceof PetBowlBlockEntity) {
                    PetBowlBlockEntity bowl = (PetBowlBlockEntity)be;
                    if (bowl.isEmpty()) {
                        Vec3 bowlCenter = Vec3.atCenterOf(this.bowlPos);
                        if (this.isNearBowl()) {
                            if (this.dog.getNavigation().isInProgress()) {
                                this.dog.getNavigation().stop();
                            }

                            if (!this.dog.isSitting()) {
                                this.dog.setSitting(true);
                            }

                            this.dog.getLookControl().setLookAt(bowlCenter.x, bowlCenter.y, bowlCenter.z, 10.0F, 30.0F);
                        } else {
                            if (this.dog.isSitting()) {
                                this.dog.setSitting(false);
                            }

                            if (!this.dog.getNavigation().isInProgress() || this.needsRepath(bowlCenter)) {
                                this.moveToBowl(server.getDayTime());
                            }
                        }

                        if ((long)this.whineTicks - this.lastWhineSoundTick >= 60L) {
                            this.playWhineSound();
                            this.lastWhineSoundTick = this.whineTicks;
                        }

                        if (this.whineTicks % 100 == 0) {
                            Vec3 pos = this.dog.position().add(0.0F, 0.5F, 0.0F);
                            server.sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.x, pos.y, pos.z, 6, 0.3, 0.3, 0.3, 0.01);
                        }

                        if (++this.whineTicks >= 300) {
                            if (this.fadeOutTicks < 30) {
                                ++this.fadeOutTicks;
                                return;
                            }

                            BlockEntity currentBowl = server.getBlockEntity(this.bowlPos);
                            if (currentBowl instanceof PetBowlBlockEntity) {
                                PetBowlBlockEntity finalBowl = (PetBowlBlockEntity)currentBowl;
                                if (finalBowl.isEmpty()) {
                                    Vec3 pos = this.dog.position().add(0.0F, 0.5F, 0.0F);
                                    server.sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.x, pos.y, pos.z, 15, 0.3, 0.3, 0.3, 0.01);
                                }
                            }

                            this.stop();
                        }

                        return;
                    }
                }

                this.stop();
                return;
            }
        }

        this.stop();
    }

    public void stop() {
        this.bowlPos = null;
        this.whineTicks = 0;
        this.fadeOutTicks = 0;
        this.active = false;
        this.lastTargetPos = null;
        this.dog.setSitting(false);
        if (this.dog.getNavigation().isInProgress()) {
            this.dog.getNavigation().stop();
        }

    }

    private void playWhineSound() {
        SoundEvent sound = WHINE_SOUNDS.get(this.dog.getRandom().nextInt(WHINE_SOUNDS.size()));
        float volume = 0.4F + this.dog.getRandom().nextFloat() * 0.3F;
        float pitch = 0.4F + this.dog.getRandom().nextFloat() * 0.4F;
        this.dog.playSound(sound, volume, pitch);
    }

    private boolean isNearBowl() {
        return this.bowlPos != null && this.dog.position().distanceToSqr(Vec3.atCenterOf(this.bowlPos)) < 1.2100000000000002;
    }

    private boolean shouldScanForBowl(long currentTick) {
        if (currentTick - this.lastScanTick < 40L) {
            return false;
        } else {
            this.lastScanTick = currentTick;
            return true;
        }
    }

    private boolean isValidWhineTime(long timeOfDay) {
        long dayTime = timeOfDay % 24000L;
        return dayTime >= MORNING_START && dayTime <= MORNING_END || dayTime >= EVENING_START && dayTime <= EVENING_END;
    }

    private void moveToBowl(long timeOfDay) {
        if (this.bowlPos != null) {
            Vec3 target = Vec3.atCenterOf(this.bowlPos);
            this.dog.getNavigation().moveTo(target.x, target.y, target.z, this.getSpeed(timeOfDay));
            this.lastTargetPos = target;
        }

    }

    private boolean needsRepath(Vec3 target) {
        return this.lastTargetPos == null || this.lastTargetPos.distanceToSqr(target) > (double)0.5F;
    }

    private double getSpeed(long timeOfDay) {
        long dayTime = timeOfDay % 24000L;
        return dayTime >= EVENING_START && dayTime <= EVENING_END ? 0.8 : (double)1.0F;
    }
}
