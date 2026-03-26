package net.beosbecerra.superbowsmod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.beosbecerra.superbowsmod.SuperBowsMod;
import net.beosbecerra.superbowsmod.entity.RayArrow;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RayArrowRenderer extends EntityRenderer<RayArrow> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(SuperBowsMod.MODID, "textures/entity/ray_arrow.png");

    // Half-size of the beam cross-section — increase for a thicker beam
    private static final float H = 0.3f;  // 0.6 blocks total width/height

    public RayArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(RayArrow entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        Vec3 dir = entity.getSyncedDirection();
        if (dir.lengthSqr() < 0.0001) return;

        float len = (float) RayArrow.RAY_LENGTH;

        poseStack.pushPose();

        // Align the box along the synced direction using a rotation quaternion
        // We rotate from +Z (default forward) to the actual ray direction
        Vector3f forward = new Vector3f(0, 0, 1);
        Vector3f target  = new Vector3f((float) dir.x, (float) dir.y, (float) dir.z).normalize();
        Quaternionf rotation = new Quaternionf().rotationTo(forward, target);
        poseStack.mulPose(rotation);

        // Full bright — ignores world lighting
        int fullBright = 0xF000F0;

        // Red color, slightly transparent for glow
        float r = 1.0f, g = 0.04f, b = 0.04f, a = 0.93f;

        VertexConsumer buf = bufferSource.getBuffer(RenderType.beaconBeam(TEXTURE, true));

        drawBox(poseStack, buf, fullBright, len, H, r, g, b, a);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    /**
     * Draws a full 6-faced box (cuboid) extending from Z=0 to Z=length.
     * Cross-section is a square from -H to +H on both X and Y.
     *
     *   Y
     *   |
     *   +--X
     *  /
     * Z (forward = beam direction)
     *
     * Vertices of the cross-section square:
     *   v0 = (-H, -H)   v1 = (+H, -H)
     *   v2 = (+H, +H)   v3 = (-H, +H)
     */
    private void drawBox(PoseStack poseStack, VertexConsumer buf, int light,
                         float len, float h,
                         float r, float g, float b, float a) {

        Matrix4f m  = poseStack.last().pose();
        Matrix3f nm = poseStack.last().normal();

        // near face  (z = 0)
        quad(buf, m, nm, light, r, g, b, a,
                -h, -h, 0,   h, -h, 0,   h,  h, 0,  -h,  h, 0,
                0,  0, -1);

        // far face   (z = len)
        quad(buf, m, nm, light, r, g, b, a,
                h, -h, len,  -h, -h, len,  -h,  h, len,   h,  h, len,
                0,  0,  1);

        // bottom face (y = -h)
        quad(buf, m, nm, light, r, g, b, a,
                -h, -h, len,   h, -h, len,   h, -h, 0,   -h, -h, 0,
                0, -1,  0);

        // top face    (y = +h)
        quad(buf, m, nm, light, r, g, b, a,
                -h,  h, 0,    h,  h, 0,    h,  h, len,  -h,  h, len,
                0,  1,  0);

        // left face   (x = -h)
        quad(buf, m, nm, light, r, g, b, a,
                -h, -h, 0,   -h, -h, len,  -h,  h, len,  -h,  h, 0,
                -1,  0,  0);

        // right face  (x = +h)
        quad(buf, m, nm, light, r, g, b, a,
                h, -h, len,   h, -h, 0,    h,  h, 0,    h,  h, len,
                1,  0,  0);
    }

    /** Emits one quad (4 vertices, CCW winding). */
    private void quad(VertexConsumer buf, Matrix4f m, Matrix3f nm, int light,
                      float r, float g, float b, float a,
                      float x0, float y0, float z0,
                      float x1, float y1, float z1,
                      float x2, float y2, float z2,
                      float x3, float y3, float z3,
                      float nx, float ny, float nz) {

        buf.vertex(m, x0, y0, z0).color(r, g, b, a)
                .uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light).normal(nm, nx, ny, nz).endVertex();

        buf.vertex(m, x1, y1, z1).color(r, g, b, a)
                .uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light).normal(nm, nx, ny, nz).endVertex();

        buf.vertex(m, x2, y2, z2).color(r, g, b, a)
                .uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light).normal(nm, nx, ny, nz).endVertex();

        buf.vertex(m, x3, y3, z3).color(r, g, b, a)
                .uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light).normal(nm, nx, ny, nz).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(RayArrow entity) {
        return TEXTURE;
    }
}