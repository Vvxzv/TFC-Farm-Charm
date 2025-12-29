package net.vvxzv.tfc_farm_charm.mixin;

import net.satisfy.farm_and_charm.core.entity.AbstractTowableEntity;
import net.satisfy.farm_and_charm.core.util.CartWheel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractTowableEntity.class)
public interface AbstractTowableEntityAccessor {
    @Accessor(value = "leftWheel", remap = false)
    CartWheel getLeftWheel();

    @Accessor(value = "rightWheel", remap = false)
    CartWheel getRightWheel();

    @Invoker(value = "tickLerp", remap = false)
    void invokeTickLerp();

}
