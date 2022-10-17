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
import net.minecraft.util.Mth;
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
        ItemTransforms.TransformType none = ItemTransforms.TransformType.NONE;
        ItemStack itemStack = DetachableTNT.SMALL_TNT_ITEM.get().getDefaultInstance();
        BakedModel bakedModel = this.itemRenderer.getModel(itemStack, entity.level, null, 1);
        if (!entity.isInGround()) {
            int i = OverlayTexture.NO_OVERLAY;
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(Vector3f.XP.rotation((entity.tickCount + partialTicks) * 0.7f));
            itemRenderer.render(itemStack, none, false, poseStack, buffer, packedLight, i, bakedModel);
        } else {
            int t = entity.getInGroundTime();
            if ((float)t - partialTicks + 1.0F < 10.0F) {
                float f = 1.0F - ((float)t - partialTicks + 1.0F) / 10.0F;
                f = Mth.clamp(f, 0.0F, 1.0F);
                f *= f;
                f *= f;
                float f1 = 1.0F + f * 0.3F;
                poseStack.scale(f1, f1, f1);
            }
            if (t / 5 % 2 == 0) {
                int i = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
                itemRenderer.render(itemStack, none, false, poseStack, buffer, packedLight, i, bakedModel);
            } else {
                int j = OverlayTexture.NO_OVERLAY;
                itemRenderer.render(itemStack, none, false, poseStack, buffer, packedLight, j, bakedModel);
            }
        }
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SmallTntEntity entity) {
        return LOCATION;
    }

}