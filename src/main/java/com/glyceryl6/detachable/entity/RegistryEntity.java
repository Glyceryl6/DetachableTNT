package com.glyceryl6.detachable.entity;

import com.glyceryl6.detachable.DetachableTNT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = DetachableTNT.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEntity {

    public static final RegistryObject<EntityType<SmallTntEntity>> SMALL_TNT =
            build(DetachableTNT.prefix("small_tnt"), makeBuilder(SmallTntEntity::new));

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SMALL_TNT.get(), SmallTntRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SmallTntModel.LAYER_LOCATION, SmallTntModel::createBodyLayer);
    }

    private static <E extends Entity> RegistryObject<EntityType<E>> build(ResourceLocation id, EntityType.Builder<E> builder) {
        builder.clientTrackingRange(10);
        return DetachableTNT.ENTITY.register(id.getPath(), () -> builder.build(id.toString()));
    }

    private static <E extends Entity> EntityType.Builder<E> makeBuilder(EntityType.EntityFactory<E> factory) {
        return EntityType.Builder.of(factory, MobCategory.MISC).sized(0.25F, 1.0F)
                .setTrackingRange(4).setUpdateInterval(10).setShouldReceiveVelocityUpdates(true);
    }

}