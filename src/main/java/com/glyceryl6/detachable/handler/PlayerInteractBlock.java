package com.glyceryl6.detachable.handler;

import com.glyceryl6.detachable.DetachableTNT;
import com.glyceryl6.detachable.entity.SmallTntEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class PlayerInteractBlock {

    @SubscribeEvent
    public void onRightClickTntBlock(PlayerInteractEvent.RightClickBlock event) {
        //当玩家空手或用小TNT右键TNT方块时，会从中逐个取出小TNT
        BlockPos pos = event.getPos();
        Level level = event.getWorld();
        Player player = event.getPlayer();
        InteractionHand hand = event.getHand();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = player.getItemInHand(hand);
        Item smallTnt = DetachableTNT.SMALL_TNT_ITEM.get();
        ItemStack tntStack = smallTnt.getDefaultInstance();
        if ((stack.isEmpty() || stack.is(tntStack.getItem())) && state.is(Blocks.TNT)) {
            Inventory inventory = player.getInventory();
            Direction direction = player.getDirection();
            Block tntEast = DetachableTNT.EAST_DETACHABLE_TNT.get();
            Block tntWest = DetachableTNT.WEST_DETACHABLE_TNT.get();
            Block tntSouth = DetachableTNT.SOUTH_DETACHABLE_TNT.get();
            Block tntNorth = DetachableTNT.NORTH_DETACHABLE_TNT.get();
            switch (direction) {
                case EAST -> level.setBlockAndUpdate(pos, tntEast.defaultBlockState());
                case WEST -> level.setBlockAndUpdate(pos, tntWest.defaultBlockState());
                case SOUTH -> level.setBlockAndUpdate(pos, tntSouth.defaultBlockState());
                default -> level.setBlockAndUpdate(pos, tntNorth.defaultBlockState());
            }
            inventory.add(tntStack);
            player.swing(hand);
        }
    }

    @SubscribeEvent
    public void onUseTntItem(PlayerInteractEvent.RightClickItem event) {
        //拿着普通的TNT右键，可以扔出一个小TNT，并根据投掷方向返回一个x15的TNT
        Level level = event.getWorld();
        Player player = event.getPlayer();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.TNT) && stack.getCount() == 1) {
            float pitch = 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5F, pitch);
            if (!level.isClientSide) {
                SmallTntEntity smallTnt = new SmallTntEntity(player, level);
                smallTnt.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(smallTnt);
            }
            player.swing(hand);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            if (!player.getAbilities().instabuild) {
                Direction direction = player.getDirection();
                ResourceLocation eastern = DetachableTNT.prefix("eastern_detachable_tnt_15");
                ResourceLocation western = DetachableTNT.prefix("western_detachable_tnt_15");
                ResourceLocation southern = DetachableTNT.prefix("southern_detachable_tnt_15");
                ResourceLocation northern = DetachableTNT.prefix("northern_detachable_tnt_15");
                Item easternTnt = ForgeRegistries.ITEMS.getValue(eastern);
                Item westernTnt = ForgeRegistries.ITEMS.getValue(western);
                Item southernTnt = ForgeRegistries.ITEMS.getValue(southern);
                Item northernTnt = ForgeRegistries.ITEMS.getValue(northern);
                if (northernTnt != null && southernTnt != null && easternTnt != null && westernTnt != null) {
                    switch (direction) {
                        case EAST -> player.setItemInHand(hand, easternTnt.getDefaultInstance());
                        case WEST -> player.setItemInHand(hand, westernTnt.getDefaultInstance());
                        case SOUTH -> player.setItemInHand(hand, southernTnt.getDefaultInstance());
                        default -> player.setItemInHand(hand, northernTnt.getDefaultInstance());
                    }
                }
            }
        }
    }

}