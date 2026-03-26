package net.beosbecerra.superbowsmod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.beosbecerra.superbowsmod.entity.GiantArrow;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GiantArrowRenderer extends ModArrowRenderer<GiantArrow> {

    private static final float SCALE = 8.0f;

    public GiantArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(GiantArrow entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(SCALE, SCALE, SCALE);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(GiantArrow entity) {
        return ARROW_TEXTURE;
    }
}