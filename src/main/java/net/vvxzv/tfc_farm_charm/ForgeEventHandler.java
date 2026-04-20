package net.vvxzv.tfc_farm_charm;

import com.eerussianguy.firmalife.common.blocks.FLBlocks;
import com.eerussianguy.firmalife.common.util.Mechanics;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.satisfy.farm_and_charm.core.block.entity.StoveBlockEntity;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.vvxzv.tfc_farm_charm.common.block.entity.DecayingFoodBlockEntity;
import net.vvxzv.tfc_farm_charm.common.data.Bag;
import net.vvxzv.tfc_farm_charm.common.utils.FoodTraits;
import net.vvxzv.tfc_farm_charm.common.utils.IStoveLitAccess;

import java.util.Set;

public class ForgeEventHandler {

    public static void init() {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(ForgeEventHandler::addReloadListeners);
        bus.addListener(ForgeEventHandler::placeBagBlock);
        bus.addListener(ForgeEventHandler::onFireStart);
        bus.addListener(ForgeEventHandler::placeBlockInCellar);
        bus.addListener(ForgeEventHandler::removeCellarFoodTrait);
        bus.addListener(ForgeEventHandler::breakFoodBlock);
    }

    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(Bag.MANAGER);
    }

    public static void placeBagBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        Player player = event.getEntity();

        if (player == null || !player.isShiftKeyDown()) {
            return;
        }

        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Direction face = event.getFace();
        if (face == null) {
            return;
        }

        BlockPos clickPos = event.getPos();
        BlockState clickedState = level.getBlockState(clickPos);
        BlockPos placePos = clickedState.canBeReplaced() ? clickPos : clickPos.relative(face);

        ItemStack stack = player.getMainHandItem();
        Bag bag = Bag.get(stack);

        if (bag != null) {
            Block block = bag.getBlock();
            BlockState state = block.defaultBlockState();

            if (!level.isUnobstructed(state, placePos, CollisionContext.of(player))) {
                return;
            }

            level.setBlockAndUpdate(placePos, state);

            SoundType soundtype = state.getSoundType(level, placePos, player);
            level.playSound(
                    player,
                    placePos,
                    soundtype.getPlaceSound(),
                    SoundSource.BLOCKS,
                    (soundtype.getVolume() + 1.0F) / 2.0F,
                    soundtype.getPitch() * 0.8F
            );

            if (level.getBlockEntity(placePos) instanceof DecayingFoodBlockEntity decaying) {
                decaying.setStackWithCount(stack);
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            } else {
                stack.shrink(1);
            }

            event.setCanceled(true);
        }
    }

    public static void onFireStart(StartFireEvent event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        Block block = state.getBlock();
        if(block == ObjectRegistry.STOVE.get()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if(entity instanceof StoveBlockEntity stove) {
                if(((IStoveLitAccess) stove).setLit(true)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    public static void placeBlockInCellar(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();

        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        if(state.is(FLBlocks.CLIMATE_STATION.get())) {
            Set<BlockPos> cellarPositions = Mechanics.getCellar(level, pos, state);
            if (cellarPositions != null) {
                cellarPositions.forEach(b -> {
                    if(level.getBlockEntity(b) instanceof DecayingFoodBlockEntity decay) {
                        ItemStack stack = decay.copyStack();
                        FoodCapability.applyTrait(stack, FoodTraits.CELLAR_PRESERVED);
                        decay.setStackWithCount(stack);
                    }
                });
            }
        }
    }

    public static void removeCellarFoodTrait(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();

        if(level.getGameTime() % 20L == 0L) {
            player.getInventory().items.forEach(item -> {
                if(FoodCapability.get(item) != null) {
                    FoodCapability.removeTrait(item, FoodTraits.CELLAR_PRESERVED);
                }
            });
        }
    }

    public static void breakFoodBlock(BlockEvent.BreakEvent event) {
        if(event.getLevel().getBlockEntity(event.getPos()) instanceof DecayingFoodBlockEntity decay) {
            ItemStack stack = decay.copyStack();
            FoodCapability.removeTrait(stack, FoodTraits.CELLAR_PRESERVED);
            decay.setStackWithCount(stack);
        }
    }
}
