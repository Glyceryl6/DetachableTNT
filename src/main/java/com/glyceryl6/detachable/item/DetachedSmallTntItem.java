package com.glyceryl6.detachable.item;

import com.glyceryl6.detachable.DetachableTNT;
import com.glyceryl6.detachable.block.*;
import com.glyceryl6.detachable.entity.SmallTntEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DetachedSmallTntItem extends BlockItem {

    public DetachedSmallTntItem(Block block, Properties properties) {
        super(block, properties);
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        float pitch = 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5F, pitch);
        if (!level.isClientSide && !player.isShiftKeyDown()) {
            SmallTntEntity smallTnt = new SmallTntEntity(player, level);
            smallTnt.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(smallTnt);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        BlockState state = level.getBlockState(pos);
        if (player != null && player.isShiftKeyDown()) {
            if (state.getBlock() instanceof AbstractDetachableTntBlock) {
                if (!level.isClientSide()) {
                    if (state.getValue(AbstractDetachableTntBlock.LEVEL) == 15) {
                        level.setBlockAndUpdate(pos, Blocks.TNT.defaultBlockState());
                    } else {
                        level.setBlockAndUpdate(pos, state.setValue(AbstractDetachableTntBlock.LEVEL,
                        state.getValue(AbstractDetachableTntBlock.LEVEL) + 1));
                    }
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return super.useOn(context);
            }
        } else {
            return super.useOn(context);
        }
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        Player player = context.getPlayer();
        if (player != null && player.isShiftKeyDown()) {
            Direction direction = player.getDirection();
            Block tntEast = DetachableTNT.EAST_DETACHABLE_TNT.get();
            Block tntWest = DetachableTNT.WEST_DETACHABLE_TNT.get();
            Block tntSouth = DetachableTNT.SOUTH_DETACHABLE_TNT.get();
            Block tntNorth = DetachableTNT.NORTH_DETACHABLE_TNT.get();
            switch (direction) {
                case EAST -> {
                    return tntEast.defaultBlockState().setValue(EasternDetachableTntBlock.LEVEL, 1);
                }
                case WEST -> {
                    return tntWest.defaultBlockState().setValue(WesternDetachableTntBlock.LEVEL, 1);
                }
                case SOUTH -> {
                    return tntSouth.defaultBlockState().setValue(SouthernDetachableTntBlock.LEVEL, 1);
                }
                default -> {
                    return tntNorth.defaultBlockState().setValue(NorthernDetachableTntBlock.LEVEL, 1);
                }
            }
        } else {
            return null;
        }
    }

}