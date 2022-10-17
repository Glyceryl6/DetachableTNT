package com.glyceryl6.detachable.item;

import com.glyceryl6.detachable.DetachableTNT;
import com.glyceryl6.detachable.block.WesternDetachableTntBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class WesternDetachableTntItem extends AbstractDetachableTntItem {

    public WesternDetachableTntItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        if (this.getRegistryName() != null) {
            String path = this.getRegistryName().getPath();
            int level = Integer.parseInt(path.substring(path.length() - 2));
            BlockState state = DetachableTNT.WEST_DETACHABLE_TNT.get().defaultBlockState();
            state = state.setValue(WesternDetachableTntBlock.LEVEL, level);
            return state;
        } else {
            return null;
        }
    }

    @Override
    protected String getIdName() {
        return "western_detachable_tnt";
    }

}