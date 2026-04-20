package net.vvxzv.tfc_farm_charm.mixin.entity;

import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.entities.livestock.TFCAnimal;
import net.dries007.tfc.common.entities.livestock.pet.Dog;
import net.dries007.tfc.common.entities.livestock.pet.TamableMammal;
import net.dries007.tfc.config.animals.MammalConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.satisfy.farm_and_charm.core.entity.BowlAccessor;
import net.satisfy.farm_and_charm.core.mixin.MobAccessor;
import net.vvxzv.tfc_farm_charm.common.entity.ai.DogEatFromBowlGoal;
import net.vvxzv.tfc_farm_charm.common.entity.ai.WhineAtBowlGoal;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Dog.class)
public abstract class DogMixin extends TamableMammal implements BowlAccessor.StayNearBowl {

    @Unique
    private boolean fedRecently = false;

    @Unique
    private BlockPos stayCenter = null;

    @Unique
    private boolean wasSittingLastTick = false;

    public DogMixin(EntityType<? extends TFCAnimal> animal, Level level, TFCSounds.EntitySound sounds, MammalConfig config) {
        super(animal, level, sounds, config);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void farmAndCharm$addFeedingGoal(CallbackInfo ci) {
        if((Mob) this instanceof Dog dog){
            ((MobAccessor)dog).farmAndCharm$getGoalSelector().addGoal(13, new DogEatFromBowlGoal((Dog)(Object)this));
            ((MobAccessor)dog).farmAndCharm$getGoalSelector().addGoal(14, new WhineAtBowlGoal((Dog)(Object)this));
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void farmAndCharm$handleStayRestriction(CallbackInfo ci) {
        if((Mob) this instanceof Dog dog) {
            boolean isSittingNow = dog.isSitting();
            if (!this.wasSittingLastTick && isSittingNow) {
                this.fedRecently = false;
                this.stayCenter = null;
            }

            this.wasSittingLastTick = isSittingNow;
            if (this.fedRecently && this.stayCenter != null && !isSittingNow) {
                double dist = this.stayCenter.distSqr(dog.blockPosition());
                if (dist > (double) 256.0F) {
                    dog.getNavigation().moveTo((double) this.stayCenter.getX() + (double) 0.5F, this.stayCenter.getY(), (double) this.stayCenter.getZ() + (double) 0.5F, 1.0F);
                }
            }
        }
    }

    @Override
    public void die(@NotNull DamageSource source) {
        this.fedRecently = false;
        this.stayCenter = null;
        super.die(source);
    }

    @Override
    public void farmAndCharm$setStayCenter(BlockPos pos) {
        this.fedRecently = true;
        this.stayCenter = pos.immutable();
    }

    @Override
    public void farmAndCharm$clearStayRestriction() {
        this.fedRecently = false;
        this.stayCenter = null;
    }

    @Override
    public boolean farmAndCharm$hasStayRestriction() {
        return this.fedRecently;
    }

    @Override
    public boolean farmAndCharm$isWithinStayRange(BlockPos pos) {
        return this.fedRecently && this.stayCenter != null && pos.distSqr(this.stayCenter) <= (double)256.0F;
    }

    @Override
    public BlockPos farmAndCharm$getStayCenter() {
        return this.stayCenter != null ? this.stayCenter : ((Dog)(Object)this).blockPosition();
    }
}
