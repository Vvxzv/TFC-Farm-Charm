package net.vvxzv.tfc_farm_charm.mixin;

import net.dries007.tfc.util.Fuel;
import net.minecraft.world.item.ItemStack;
import net.satisfy.farm_and_charm.client.gui.handler.StoveGuiHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StoveGuiHandler.class)
public class StoveGuiHandlerMixin {

    @Inject(method = "isFuel", at = @At("RETURN"), cancellable = true, remap = false)
    private static void isFuel(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(Fuel.get(stack) != null);
    }
}
