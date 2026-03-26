package net.beosbecerra.superbowsmod.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.beosbecerra.superbowsmod.entity.FrozenStatueEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class FrozenStatueRenderer extends EntityRenderer<FrozenStatueEntity> {

    private static final ResourceLocation ICE_TEXTURE =
            new ResourceLocation("minecraft", "textures/block/packed_ice.png");

    private static RenderType ICE_RENDER_TYPE = null;

    private final Map<String, EntityModel> modelCache = new HashMap<>();
    private final Map<String, Entity> entityCache = new HashMap<>();
    private final EntityRendererProvider.Context context;

    public FrozenStatueRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.context = context;

    }

    private static RenderType getIceRenderType() {
        if (ICE_RENDER_TYPE == null) {
            RenderStateShard.TextureStateShard textureState =
                    new RenderStateShard.TextureStateShard(ICE_TEXTURE, false, false);
            RenderType.CompositeState state = RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(
                            net.minecraft.client.renderer.GameRenderer::getRendertypeEntityCutoutShader))
                    .setTextureState(textureState)
                    .setTransparencyState(new RenderStateShard.TransparencyStateShard(
                            "no_transparency", () -> {}, () -> {}))
                    .setLightmapState(new RenderStateShard.LightmapStateShard(true))
                    .setOverlayState(new RenderStateShard.OverlayStateShard(true))
                    .createCompositeState(true);
            ICE_RENDER_TYPE = RenderType.create("ice_statue_type",
                    DefaultVertexFormat.NEW_ENTITY,
                    VertexFormat.Mode.QUADS,
                    256, false, true, state);
        }
        return ICE_RENDER_TYPE;
    }
    @Override
    public void render(FrozenStatueEntity statue, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        // get or build model
        EntityModel model = modelCache.get(statue.getTrappedEntityTypeString());
        if (model == null) {
            EntityRenderer renderer = Minecraft.getInstance()
                    .getEntityRenderDispatcher()
                    .renderers.get(statue.getTrappedEntityType());
            if (renderer instanceof RenderLayerParent) {
                model = ((RenderLayerParent<?, ?>) renderer).getModel();
            } else {
                model = new PigModel(context.bakeLayer(ModelLayers.PIG));
            }
            modelCache.put(statue.getTrappedEntityTypeString(), model);
        }

        // get or build fake entity for pose
        Entity fakeEntity = entityCache.get(statue.getTrappedEntityTypeString());
        if (fakeEntity == null) {
            EntityType<?> type = EntityType.byString(statue.getTrappedEntityTypeString()).orElse(null);
            if (type != null) {
                fakeEntity = type.create(Minecraft.getInstance().level);
                if (fakeEntity != null) {
                    try { fakeEntity.load(statue.getTrappedTag()); } catch (Exception ignored) {}
                    entityCache.put(statue.getTrappedEntityTypeString(), fakeEntity);
                }
            }
        }

        if (model == null) return;

        // freeze model in neutral pose
        model.setupAnim(fakeEntity != null ? fakeEntity : statue,
                0.0F, 0.0F, -0.1F, 0.0F, 0.0F);

        float scale = statue.getTrappedScale() < 0.01F ? 1.0F : statue.getTrappedScale();

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0, 1.5F, 0);
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180));
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(
                statue.yRotO + (statue.getYRot() - statue.yRotO) * partialTicks));

        // render with ice texture and slight blue tint
        VertexConsumer consumer = bufferSource.getBuffer(getIceRenderType());
        model.renderToBuffer(poseStack, consumer, packedLight,
                OverlayTexture.NO_OVERLAY, 0.6f, 0.85f, 1.0f, 1.0f);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(FrozenStatueEntity entity) {
        return ICE_TEXTURE;
    }
}