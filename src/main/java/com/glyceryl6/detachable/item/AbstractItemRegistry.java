package com.glyceryl6.detachable.item;

import com.glyceryl6.detachable.DetachableTNT;
import com.glyceryl6.detachable.block.EasternDetachableTntBlock;
import com.glyceryl6.detachable.block.NorthernDetachableTntBlock;
import com.glyceryl6.detachable.block.SouthernDetachableTntBlock;
import com.glyceryl6.detachable.block.WesternDetachableTntBlock;
import net.minecraft.world.item.Item;

public class AbstractItemRegistry {

    public static void register() {
        registerNorthernFacing(1);
        registerNorthernFacing(2);
        registerNorthernFacing(3);
        registerNorthernFacing(4);
        registerNorthernFacing(5);
        registerNorthernFacing(6);
        registerNorthernFacing(7);
        registerNorthernFacing(8);
        registerNorthernFacing(9);
        registerNorthernFacing(10);
        registerNorthernFacing(11);
        registerNorthernFacing(12);
        registerNorthernFacing(13);
        registerNorthernFacing(14);
        registerNorthernFacing(15);
        registerSouthernFacing(1);
        registerSouthernFacing(2);
        registerSouthernFacing(3);
        registerSouthernFacing(4);
        registerSouthernFacing(5);
        registerSouthernFacing(6);
        registerSouthernFacing(7);
        registerSouthernFacing(8);
        registerSouthernFacing(9);
        registerSouthernFacing(10);
        registerSouthernFacing(11);
        registerSouthernFacing(12);
        registerSouthernFacing(13);
        registerSouthernFacing(14);
        registerSouthernFacing(15);
        registerEasternFacing(1);
        registerEasternFacing(2);
        registerEasternFacing(3);
        registerEasternFacing(4);
        registerEasternFacing(5);
        registerEasternFacing(6);
        registerEasternFacing(7);
        registerEasternFacing(8);
        registerEasternFacing(9);
        registerEasternFacing(10);
        registerEasternFacing(11);
        registerEasternFacing(12);
        registerEasternFacing(13);
        registerEasternFacing(14);
        registerEasternFacing(15);
        registerWesternFacing(1);
        registerWesternFacing(2);
        registerWesternFacing(3);
        registerWesternFacing(4);
        registerWesternFacing(5);
        registerWesternFacing(6);
        registerWesternFacing(7);
        registerWesternFacing(8);
        registerWesternFacing(9);
        registerWesternFacing(10);
        registerWesternFacing(11);
        registerWesternFacing(12);
        registerWesternFacing(13);
        registerWesternFacing(14);
        registerWesternFacing(15);
    }

    private static void registerNorthernFacing(int level) {
        DetachableTNT.ITEMS.register("northern_detachable_tnt_" + String.format("%02d", level),
        () -> new NorthernDetachableTntItem(DetachableTNT.NORTH_DETACHABLE_TNT.get().defaultBlockState()
        .setValue(NorthernDetachableTntBlock.LEVEL, level).getBlock(), new Item.Properties()));
    }

    private static void registerSouthernFacing(int level) {
        DetachableTNT.ITEMS.register("southern_detachable_tnt_" + String.format("%02d", level),
        () -> new SouthernDetachableTntItem(DetachableTNT.SOUTH_DETACHABLE_TNT.get().defaultBlockState()
        .setValue(SouthernDetachableTntBlock.LEVEL, level).getBlock(), new Item.Properties()));
    }

    private static void registerEasternFacing(int level) {
        DetachableTNT.ITEMS.register("eastern_detachable_tnt_" + String.format("%02d", level),
        () -> new EasternDetachableTntItem(DetachableTNT.EAST_DETACHABLE_TNT.get().defaultBlockState()
        .setValue(EasternDetachableTntBlock.LEVEL, level).getBlock(), new Item.Properties()));
    }

    private static void registerWesternFacing(int level) {
        DetachableTNT.ITEMS.register("western_detachable_tnt_" + String.format("%02d", level),
        () -> new WesternDetachableTntItem(DetachableTNT.WEST_DETACHABLE_TNT.get().defaultBlockState()
        .setValue(WesternDetachableTntBlock.LEVEL, level).getBlock(), new Item.Properties()));
    }

}