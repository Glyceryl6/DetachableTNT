package com.glyceryl6.detachable.entity;

import com.glyceryl6.detachable.DetachableTNT;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SmallTntRenderer extends EntityRenderer<SmallTntEntity> {

    public static final ResourceLocation LOCATION = DetachableTNT.prefix("textures/entity/small_tnt.png");
    public final SmallTntModel model;
    public final ItemRenderer itemRenderer;

    public SmallTntRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.model = new SmallTntModel(context.bakeLayer(SmallTntModel.LAYER_LOCATION));
    }

    @Override
    public void render(SmallTntEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        ItemStack itemStack = DetachableTNT.SMALL_TNT_ITEM.get().getDefaultInstance();
        BakedModel bakedModel = this.itemRenderer.getModel(itemStack, entity.level, null, 1);
        if (!entity.isInGround()) {
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(Vector3f.XP.rotation((entity.tickCount + partialTicks) * 0.7f));
        }
        itemRenderer.render(itemStack, ItemTransforms.TransformType.NONE, false, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, bakedModel);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SmallTntEntity entity) {
        return LOCATION;
    }

}
