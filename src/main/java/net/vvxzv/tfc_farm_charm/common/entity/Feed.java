package net.vvxzv.tfc_farm_charm.common.entity;

import net.dries007.tfc.common.entities.livestock.OviparousAnimal;
import net.dries007.tfc.common.entities.livestock.horse.TFCHorse;
import net.dries007.tfc.common.entities.livestock.pet.Dog;
import net.dries007.tfc.common.entities.livestock.pet.TFCCat;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.satisfy.farm_and_charm.core.item.CatFoodItem;
import net.satisfy.farm_and_charm.core.item.ChickenFeedItem;
import net.satisfy.farm_and_charm.core.item.DogFoodItem;
import net.satisfy.farm_and_charm.core.item.HorseFodderItem;
import net.satisfy.farm_and_charm.core.registry.MobEffectRegistry;
import net.vvxzv.tfc_farm_charm.TFCFarmCharm;

@Mod.EventBusSubscriber(modid = TFCFarmCharm.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Feed {
    @SubscribeEvent
    public static void UseItemFeed(PlayerInteractEvent.EntityInteract event){
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);

        if (!(entity instanceof LivingEntity target)) {
            return;
        }

        if(stack.getItem() instanceof CatFoodItem && target instanceof TFCCat cat){
            if (!cat.level().isClientSide) {
                if(cat.getOwnerUUID() == null){
                    cat.tame(player);
                    cat.setOwnerUUID(player.getUUID());
                }
                cat.heal(10.0F);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            else {
                Level world = cat.getCommandSenderWorld();
                world.addParticle(ParticleTypes.HEART, cat.getX(), cat.getY() + 1.0F, cat.getZ(), 0.0F, 0.0F, 0.0F);
                world.playSound(null, cat.getX(), cat.getY(), cat.getZ(), SoundEvents.FOX_EAT, cat.getSoundSource(), 1.0F, 1.0F);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
        else if(stack.getItem() instanceof DogFoodItem && target instanceof Dog dog){
            if (!dog.level().isClientSide) {
                if(dog.getOwnerUUID() == null) {
                    dog.tame(player);
                    dog.setOwnerUUID(player.getUUID());
                }
                dog.heal(10.0F);
                dog.addEffect(new MobEffectInstance(MobEffectRegistry.DOG_FOOD.get(), 3600, 0));
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            } else {
                Level world = dog.getCommandSenderWorld();
                world.addParticle(ParticleTypes.HEART, dog.getX(), dog.getY() + 1.0F, dog.getZ(), 0.0F, 0.0F, 0.0F);
                world.playSound(null, dog.getX(), dog.getY(), dog.getZ(), SoundEvents.FOX_EAT, dog.getSoundSource(), 1.0F, 1.0F);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
        else if(stack.getItem() instanceof HorseFodderItem && target instanceof TFCHorse horse){
            if (!horse.level().isClientSide) {
                horse.addEffect(new MobEffectInstance(MobEffectRegistry.HORSE_FODDER.get(), 6000, 0));
                horse.heal(10.0F);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            else {
                Level world = horse.getCommandSenderWorld();
                world.addParticle(ParticleTypes.HEART, horse.getX(), horse.getY() + 1.0F, horse.getZ(), 0.0F, 1.0F, 1.0F);
                world.playSound((Player)null, horse.getX(), horse.getY(), horse.getZ(), SoundEvents.HORSE_EAT, horse.getSoundSource(), 1.0F, 1.0F);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
        else if(stack.getItem() instanceof ChickenFeedItem && target instanceof OviparousAnimal animal){
            animal.addEffect(new MobEffectInstance(MobEffectRegistry.CLUCK.get(), 1200));
            player.level().playSound(null, animal.getX(), animal.getY(), animal.getZ(), SoundEvents.CHICKEN_AMBIENT, SoundSource.NEUTRAL, 1.0F, 1.0F);
            animal.level().addParticle(ParticleTypes.HEART, animal.getX(), animal.getY() + 0.5F, animal.getZ(), 0.0F, 0.0F, 0.0F);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }
}
