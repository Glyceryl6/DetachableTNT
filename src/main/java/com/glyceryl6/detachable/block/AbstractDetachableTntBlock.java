package com.glyceryl6.detachable.block;

import com.glyceryl6.detachable.DetachableTNT;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class AbstractDetachableTntBlock extends Block {

    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, 15);

    public AbstractDetachableTntBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 15));
    }

    @Override
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item smallTnt = DetachableTNT.SMALL_TNT_ITEM.get();
        ItemStack tntStack = smallTnt.getDefaultInstance();
        if (itemstack.isEmpty() || itemstack.is(smallTnt)) {
            float pitch = 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5F, pitch);
            if (!player.level.isClientSide()) {
                if (state.getValue(LEVEL) == 1) {
                    level.removeBlock(pos, false);
                } else {
                    level.setBlockAndUpdate(pos, state.setValue(LEVEL, state.getValue(LEVEL) - 1));
                }
                player.getInventory().add(tntStack);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.use(state, level, pos, player, hand, hit);
        }
    }

    @NotNull
    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        String format = String.format("%02d", state.getValue(LEVEL));
        String name = this.getIdName() + "_" + format;
        Item item = ForgeRegistries.ITEMS.getValue(DetachableTNT.prefix(name));
        if (item != null) {
            return item.getDefaultInstance();
        }

        return super.getCloneItemStack(state, target, level, pos, player);
    }

    @Override
    @ParametersAreNonnullByDefault
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return Block.box(0.0D, 0.1D, 0.0D, 16.0D, 16.0D, 16.0D);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    protected String getIdName() {
        return "";
    }

}