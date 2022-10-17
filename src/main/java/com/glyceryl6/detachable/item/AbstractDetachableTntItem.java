package com.glyceryl6.detachable.item;

import com.glyceryl6.detachable.DetachableTNT;
import com.glyceryl6.detachable.entity.SmallTntEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AbstractDetachableTntItem extends BlockItem {

    public AbstractDetachableTntItem(Block block, Properties properties) {
        super(block, properties);
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getCount() == 1) {
            float pitch = 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5F, pitch);
            if (!level.isClientSide) {
                SmallTntEntity smallTnt = new SmallTntEntity(player, level);
                smallTnt.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(smallTnt);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        if (!player.getAbilities().instabuild) {
            if (this.getRegistryName() != null) {
                String path = this.getRegistryName().getPath();
                int l = Integer.parseInt(path.substring(path.length() - 2));
                String name = this.getIdName() + "_" + String.format("%02d", (l - 1));
                ResourceLocation resource = DetachableTNT.prefix(name);
                Item item = ForgeRegistries.ITEMS.getValue(resource);
                if (item != null && stack.getCount() == 1) {
                    ItemStack stack1 = item.getDefaultInstance();
                    player.setItemInHand(hand, stack1);
                }
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide() && stack.getCount() == 1);
    }

    @NotNull
    @Override
    public Component getName(ItemStack stack) {
        if (this.getRegistryName() != null) {
            String path = this.getRegistryName().getPath();
            int level = Integer.parseInt(path.substring(path.length() - 2));
            return new TranslatableComponent(this.getDescriptionId(stack), level);
        } else {
            return super.getName(stack);
        }
    }

    protected String getIdName() {
        return "";
    }

}