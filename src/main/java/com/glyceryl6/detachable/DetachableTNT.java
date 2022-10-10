package com.glyceryl6.detachable;

import com.glyceryl6.detachable.block.DetachableTntBlock;
import com.glyceryl6.detachable.handler.PlayerInteractBlock;
import com.glyceryl6.detachable.item.SmallTntItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;

@Mod(DetachableTNT.MOD_ID)
public class DetachableTNT {

    public static final String MOD_ID = "detachable_tnt";
    public IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(ForgeRegistries.ENTITIES, MOD_ID);

    public static final RegistryObject<Item> SMALL_TNT_ITEM = ITEMS.register("small_tnt",
            () -> new SmallTntItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Block> DETACHABLE_TNT = BLOCKS.register("detachable_tnt",
            () -> new DetachableTntBlock(BlockBehaviour.Properties.copy(Blocks.TNT)));

    public static final RegistryObject<Item> DETACHABLE_TNT_ITEM = ITEMS.register("detachable_tnt",
            () -> new BlockItem(DETACHABLE_TNT.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public DetachableTNT() {
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
        ENTITY.register(eventBus);
        eventBus.addListener(this::registryProperties);
        MinecraftForge.EVENT_BUS.register(new PlayerInteractBlock());
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void registryProperties(FMLClientSetupEvent event) {

    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MOD_ID, name.toLowerCase(Locale.ROOT));
    }

}