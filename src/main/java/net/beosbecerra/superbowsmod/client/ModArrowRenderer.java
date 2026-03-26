package net.beosbecerra.superbowsmod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.beosbecerra.superbowsmod.entity.ModArrow;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModArrowRenderer<T extends ModArrow> extends ArrowRenderer<T> {

    public static final ResourceLocation ARROW_TEXTURE =
            new ResourceLocation("minecraft", "textures/entity/projectiles/arrow.png");

    public ModArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return ARROW_TEXTURE;
    }
}