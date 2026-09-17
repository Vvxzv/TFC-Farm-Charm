package net.vvxzv.tfc_farm_charm.mixin.block.entity;

import com.eerussianguy.firmalife.common.blockentities.FLBeehiveBlockEntity;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blockentities.IFarmland;
import net.vvxzv.tfc_farm_charm.common.utils.Utils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FLBeehiveBlockEntity.class)
public class FLBeehiveBlockEntityMixin {

    @Final
    @Shadow(remap = false)
    private static FarmlandBlockEntity.NutrientType N;

    @Final
    @Shadow(remap = false)
    private static FarmlandBlockEntity.NutrientType P;

    @Final
    @Shadow(remap = false)
    private static FarmlandBlockEntity.NutrientType K;

    @Inject(method = "receiveNutrients", at = @At("HEAD"), cancellable = true, remap = false)
    private void receiveNutrients(IFarmland farmland, float cap, float nitrogen, float phosphorous, float potassium, CallbackInfo ci) {
        Utils.addFarmlandNutrient(farmland, N, cap, nitrogen);
        Utils.addFarmlandNutrient(farmland, P, cap, phosphorous);
        Utils.addFarmlandNutrient(farmland, K, cap, potassium);
        ci.cancel();
    }
}
