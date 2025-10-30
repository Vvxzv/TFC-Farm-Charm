package net.vvxzv.tfc_farm_charm.mixin;

import net.dries007.tfc.common.entities.livestock.pet.TFCCat;
import net.dries007.tfc.common.entities.livestock.pet.TamableMammal;
import net.minecraft.world.entity.Mob;
import net.satisfy.farm_and_charm.core.entity.BowlAccessor;
import net.satisfy.farm_and_charm.core.mixin.MobAccessor;
import net.vvxzv.tfc_farm_charm.common.entity.ai.CatEatFromBowlGoal;
import net.vvxzv.tfc_farm_charm.common.entity.ai.MeowAtBowlGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TamableMammal.class)
public class TFCCatMixin implements BowlAccessor.FedTracker{
    @Unique
    private boolean farmAndCharm$fedFromBowl = false;
    @Unique
    private int farmAndCharm$fedTimer = 0;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void farmAndCharm$addFeedingGoal(CallbackInfo ci) {
        if((Mob)(Object)this instanceof TFCCat cat) {
            ((MobAccessor) cat).farmAndCharm$getGoalSelector().addGoal(13, new CatEatFromBowlGoal((TFCCat)(Object)this));
            ((MobAccessor) cat).farmAndCharm$getGoalSelector().addGoal(14, new MeowAtBowlGoal((TFCCat)(Object)this));
        }
    }

    @Inject(
            method = {"tick"},
            at = {@At("HEAD")}
    )
    private void farmAndCharm$tickFedStatus(CallbackInfo ci) {
        if (this.farmAndCharm$fedFromBowl) {
            ++this.farmAndCharm$fedTimer;
            if (this.farmAndCharm$fedTimer >= 12000) {
                this.farmAndCharm$fedFromBowl = false;
                this.farmAndCharm$fedTimer = 0;
            }
        }

    }

    public void farmAndCharm$$markAsFed() {
        this.farmAndCharm$fedFromBowl = true;
        this.farmAndCharm$fedTimer = 0;
    }

    public void farmAndCharm$$resetFed() {
        this.farmAndCharm$fedFromBowl = false;
        this.farmAndCharm$fedTimer = 0;
    }

    public boolean farmAndCharm$$isFed() {
        return this.farmAndCharm$fedFromBowl;
    }
}
