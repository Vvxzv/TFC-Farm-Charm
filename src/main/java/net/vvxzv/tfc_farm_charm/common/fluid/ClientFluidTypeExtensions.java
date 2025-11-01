package net.vvxzv.tfc_farm_charm.common.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

public class ClientFluidTypeExtensions implements IClientFluidTypeExtensions {
    private final ResourceLocation still;
    private final ResourceLocation flow;

    public ClientFluidTypeExtensions(ResourceLocation still, ResourceLocation flow) {
        this.still = still;
        this.flow = flow;
    }

    public ResourceLocation getStillTexture() {
        return this.still;
    }

    public ResourceLocation getFlowingTexture() {
        return this.flow;
    }
}
