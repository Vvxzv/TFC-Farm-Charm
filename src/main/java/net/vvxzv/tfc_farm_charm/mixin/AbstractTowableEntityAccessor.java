package net.vvxzv.tfc_farm_charm.mixin;

import net.satisfy.farm_and_charm.core.entity.AbstractTowableEntity;
import net.satisfy.farm_and_charm.core.util.CartWheel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractTowableEntity.class)
public interface AbstractTowableEntityAccessor {
    @Accessor("leftWheel")
    CartWheel leftWheel();

    @Accessor("rightWheel")
    CartWheel rightWheel();

    @Invoker("tickLerp")
    void invokeTickLerp();

}
