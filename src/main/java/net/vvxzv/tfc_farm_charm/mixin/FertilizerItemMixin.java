package net.vvxzv.tfc_farm_charm.mixin;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blockentities.IFarmland;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.farm_and_charm.core.item.FertilizerItem;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.Random;

@Mixin(FertilizerItem.class)
public class FertilizerItemMixin extends BoneMealItem {
    @Unique
    private static final FarmlandBlockEntity.NutrientType tfc_farm_charm$N = FarmlandBlockEntity.NutrientType.NITROGEN;
    @Unique
    private static final FarmlandBlockEntity.NutrientType tfc_farm_charm$P = FarmlandBlockEntity.NutrientType.PHOSPHOROUS;
    @Unique
    private static final FarmlandBlockEntity.NutrientType tfc_farm_charm$K = FarmlandBlockEntity.NutrientType.POTASSIUM;

    public FertilizerItemMixin(Properties pProperties) {
        super(pProperties);
    }

    /**
     * @author Vvxzv
     * @reason compat
     */
    @Overwrite
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (!PlatformHelper.isFertilizerEnabled()) {
            return InteractionResult.PASS;
        } else {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            Player player = context.getPlayer();
            if (player == null) {
                return InteractionResult.PASS;
            }
            else {
                ItemStack stack = context.getItemInHand();
                boolean applied = false;
                if (!world.isClientSide && world instanceof ServerLevel) {
                    ServerLevel serverWorld = (ServerLevel)world;
                    BlockPos min = pos.offset(-3, -3, -3);
                    BlockPos max = pos.offset(3, 3, 3);
                    if (serverWorld.hasChunksAt(min, max)) {
                        for(BlockPos blockPos : BlockPos.betweenClosed(min, max)) {
                            BlockEntity be = serverWorld.getBlockEntity(blockPos);
                            if (be instanceof IFarmland farmland) {
                                int which = serverWorld.random.nextInt(3);
                                float nut = serverWorld.random.nextFloat() * 0.2F;
                                this.tfc_farm_charm$receiveNutrients(farmland, 1.0F, which == 0 ? nut : 0.0F, which == 1 ? nut : 0.0F, which == 2 ? nut : 0.0F);
                                applied = true;
                            }
                        }
                    }
                    if (applied) {
                        stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
                        return InteractionResult.sidedSuccess(world.isClientSide());
                    }
                }

                if (world.isClientSide) {
                    Random random = new Random();

                    for(int i = 0; i < 100; ++i) {
                        double offsetX = (random.nextDouble() - (double)0.5F) * (double)2.0F;
                        double offsetY = random.nextDouble();
                        double offsetZ = (random.nextDouble() - (double)0.5F) * (double)2.0F;
                        double x = (double)pos.getX() + (double)0.5F + offsetX;
                        double y = (double)pos.getY() + (double)1.0F + offsetY;
                        double z = (double)pos.getZ() + (double)0.5F + offsetZ;
                        world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0.0F, 0.1, 0.0F);
                        ItemParticleOption sowingParticle = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(ObjectRegistry.FERTILIZER.get()));
                        world.addParticle(sowingParticle, x, y, z, 0.0F, 0.1F, 0.0F);
                    }
                }

                return super.useOn(context);
            }
        }
    }

    @Unique
    private void tfc_farm_charm$receiveNutrients(IFarmland farmland, float cap, float nitrogen, float phosphorous, float potassium) {
        float n = farmland.getNutrient(tfc_farm_charm$N);
        if (n < cap) {
            farmland.setNutrient(tfc_farm_charm$N, Math.min(n + nitrogen, cap));
        }

        float p = farmland.getNutrient(tfc_farm_charm$P);
        if (p < cap) {
            farmland.setNutrient(tfc_farm_charm$P, Math.min(p + phosphorous, cap));
        }

        float k = farmland.getNutrient(tfc_farm_charm$K);
        if (k < cap) {
            farmland.setNutrient(tfc_farm_charm$K, Math.min(k + potassium, cap));
        }

    }
}
