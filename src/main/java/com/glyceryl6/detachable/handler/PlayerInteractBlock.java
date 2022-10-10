package com.glyceryl6.detachable.handler;

import com.glyceryl6.detachable.DetachableTNT;
import com.glyceryl6.detachable.block.DetachableTntBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerInteractBlock {

    @SubscribeEvent
    public void onInteractWithTnt(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getWorld();
        Player player = event.getPlayer();
        BlockPos blockPos = event.getPos();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);
        BlockState state = level.getBlockState(blockPos);
        Item smallTnt = DetachableTNT.SMALL_TNT_ITEM.get();
        ItemStack tntStack = smallTnt.getDefaultInstance();
        if (state.is(Blocks.TNT) && (stack.isEmpty()
                || stack.is(tntStack.getItem()))) {
            Inventory inventory = player.getInventory();
            Block tntBlock = DetachableTNT.DETACHABLE_TNT.get();
            Direction direction = player.getDirection().getOpposite();
            level.setBlock(blockPos, tntBlock.defaultBlockState()
                    .setValue(DetachableTntBlock.FACING, direction)
                    .setValue(DetachableTntBlock.LEVEL, 15), 3);
            inventory.add(tntStack);
            player.swing(hand);
        }
    }

}
