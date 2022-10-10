package com.glyceryl6.detachable.item;

import com.glyceryl6.detachable.DetachableTNT;
import com.glyceryl6.detachable.block.DetachableTntBlock;
import com.glyceryl6.detachable.entity.SmallTntEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class SmallTntItem extends Item {

    public SmallTntItem(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
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
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        BlockState state = level.getBlockState(pos);
        if (state.is(DetachableTNT.DETACHABLE_TNT.get())) {
            if (!level.isClientSide && player != null && player.isShiftKeyDown()) {
                if (state.getValue(DetachableTntBlock.LEVEL) < 16) {
                    level.setBlock(pos, state.setValue(DetachableTntBlock.LEVEL,
                    state.getValue(DetachableTntBlock.LEVEL) + 1), 3);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (!level.isClientSide && player != null && player.isShiftKeyDown()) {
            InteractionResult interactionResult = this.place(new BlockPlaceContext(context));
            if (!interactionResult.consumesAction() && this.isEdible() && context.getPlayer() != null) {
                InteractionResult interactionResult1 = this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
                return interactionResult1 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : interactionResult1;
            } else {
                return interactionResult;
            }
        }
        return super.useOn(context);
    }


    public InteractionResult place(BlockPlaceContext context) {
        if (!context.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext blockPlaceContext = this.updatePlacementContext(context);
            if (blockPlaceContext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState blockState = this.getPlacementState(blockPlaceContext);
                if (blockState == null) {
                    return InteractionResult.FAIL;
                } else if (!this.placeBlock(blockPlaceContext, blockState)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos blockPos = blockPlaceContext.getClickedPos();
                    Level level = blockPlaceContext.getLevel();
                    Player player = blockPlaceContext.getPlayer();
                    ItemStack itemStack = blockPlaceContext.getItemInHand();
                    BlockState blockState1 = level.getBlockState(blockPos);
                    if (blockState1.is(blockState.getBlock())) {
                        blockState1 = this.updateBlockStateFromTag(blockPos, level, itemStack, blockState1);
                        this.updateCustomBlockEntityTag(blockPos, level, player, itemStack);
                        blockState1.getBlock().setPlacedBy(level, blockPos, blockState1, player, itemStack);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockPos, itemStack);
                        }
                    }

                    level.gameEvent(player, GameEvent.BLOCK_PLACE, blockPos);
                    SoundType soundType = blockState1.getSoundType(level, blockPos, context.getPlayer());
                    float volume = (soundType.getVolume() + 1.0F) / 2.0F;
                    float pitch = soundType.getPitch() * 0.8F;
                    level.playSound(player, blockPos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, volume, pitch);
                    if (player == null || !player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
    }

    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        return context;
    }

    protected void updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack) {
        updateCustomBlockEntityTag(level, player, pos, stack);
    }

    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        Player player = context.getPlayer();
        BlockState state = this.getBlock().getStateForPlacement(context);
        return state != null && player != null && this.canPlace(context, state) ?
                state.setValue(DetachableTntBlock.LEVEL, 1)
                .setValue(DetachableTntBlock.FACING, player.getDirection()): null;
    }

    private BlockState updateBlockStateFromTag(BlockPos pos, Level level, ItemStack stack, BlockState state) {
        BlockState blockState = state;
        CompoundTag compoundTag = stack.getTag();
        if (compoundTag != null) {
            CompoundTag compoundTag1 = compoundTag.getCompound("BlockStateTag");
            StateDefinition<Block, BlockState> stateDefinition = state.getBlock().getStateDefinition();
            for(String s : compoundTag1.getAllKeys()) {
                Property<?> property = stateDefinition.getProperty(s);
                if (property != null) {
                    String s1 = Objects.requireNonNull(compoundTag1.get(s)).getAsString();
                    blockState = updateState(blockState, property, s1);
                }
            }
        }

        if (blockState != state) {
            level.setBlock(pos, blockState, 2);
        }

        return blockState;
    }

    private static <T extends Comparable<T>> BlockState updateState(BlockState state, Property<T> property, String valueIdentifier) {
        return property.getValue(valueIdentifier).map((t) -> state.setValue(property, t)).orElse(state);
    }

    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        Player player = context.getPlayer();
        CollisionContext collisionContext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
        return state.canSurvive(context.getLevel(), context.getClickedPos()) &&
                context.getLevel().isUnobstructed(state, context.getClickedPos(), collisionContext);
    }

    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        return context.getLevel().setBlock(context.getClickedPos(), state, 11);
    }

    public static void updateCustomBlockEntityTag(Level level, @Nullable Player player, BlockPos pos, ItemStack stack) {
        MinecraftServer server = level.getServer();
        if (server != null) {
            CompoundTag compoundTag = getBlockEntityData(stack);
            if (compoundTag != null) {
                BlockEntity blockentity = level.getBlockEntity(pos);
                if (blockentity != null) {
                    if (!level.isClientSide && blockentity.onlyOpCanSetNbt()
                            && (player == null || !player.canUseGameMasterBlocks())) {
                        return;
                    }

                    CompoundTag compoundTag1 = blockentity.saveWithoutMetadata();
                    CompoundTag compoundTag2 = compoundTag1.copy();
                    compoundTag1.merge(compoundTag);
                    if (!compoundTag1.equals(compoundTag2)) {
                        blockentity.load(compoundTag1);
                        blockentity.setChanged();
                    }
                }
            }
        }
    }

    public Block getBlock() {
        return DetachableTNT.DETACHABLE_TNT.get().delegate.get();
    }

    @Nullable
    public static CompoundTag getBlockEntityData(ItemStack itemStack) {
        return itemStack.getTagElement("BlockEntityTag");
    }

}