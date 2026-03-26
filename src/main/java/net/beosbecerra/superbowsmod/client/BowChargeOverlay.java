package net.beosbecerra.superbowsmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.beosbecerra.superbowsmod.SuperBowsMod;
import net.beosbecerra.superbowsmod.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

@Mod.EventBusSubscriber(modid = SuperBowsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BowChargeOverlay {

    // Add all your bows here — any bow in this set will show the charge ring
    private static boolean isModBow(ItemStack stack) {
        if (!(stack.getItem() instanceof BowItem)) return false;
        ResourceLocation name = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return name != null && name.getNamespace().equals(SuperBowsMod.MODID);
    }



    @SubscribeEvent
    public static void onRenderHud(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.options.hideGui) return;

        // Only show when the player is actively drawing a bow
        ItemStack using = player.getUseItem();
        if (using.isEmpty()) return;
        if (!isModBow(using)) return;

        // Calculate charge (0.0 – 1.0), same formula vanilla uses
        int ticksUsed = player.getUseItemRemainingTicks();
        int maxDuration = using.getUseDuration();
        int ticksDrawn = maxDuration - ticksUsed;
        float charge = BowItem.getPowerForTime(ticksDrawn); // 0.0 → 1.0

        GuiGraphics gui = event.getGuiGraphics();
        int screenW = event.getWindow().getGuiScaledWidth();
        int screenH = event.getWindow().getGuiScaledHeight();

        drawChargeRing(gui, screenW / 2, screenH / 2, charge);
    }

    /**
     * Draws a segmented arc ring around the crosshair.
     * cx, cy = center (crosshair position)
     * charge  = 0.0 (empty) to 1.0 (full)
     */
    private static void drawChargeRing(GuiGraphics gui, int cx, int cy, float charge) {
        int radius   = 14;   // pixels from center to ring
        int segments = 32;   // how many segments make the full circle
        int thickness = 2;   // pixel size of each dot

        // Color interpolates red → yellow → green as charge increases
        int color = interpolateColor(charge);

        for (int i = 0; i < segments; i++) {
            float segmentCharge = i / (float) segments;
            if (segmentCharge > charge) continue; // only draw filled portion

            // Angle: start at top (-90°), go clockwise
            double angle = Math.toRadians((segmentCharge * 360.0) - 90.0);
            int x = (int)(cx + Math.cos(angle) * radius) - thickness / 2;
            int y = (int)(cy + Math.sin(angle) * radius) - thickness / 2;

            gui.fill(x, y, x + thickness, y + thickness, color);
        }

        // When fully charged: draw a small pulsing dot in the center as ready indicator
        if (charge >= 1.0f) {
            int dotSize = 2;
            gui.fill(cx - dotSize, cy - dotSize, cx + dotSize, cy + dotSize, 0xFF00FF44);
        }
    }

    /**
     * Interpolates color:
     *   0.0 → red    (0xFF, 0x22, 0x22)
     *   0.5 → yellow (0xFF, 0xCC, 0x00)
     *   1.0 → green  (0x00, 0xFF, 0x44)
     */
    private static int interpolateColor(float t) {
        int r, g, b;
        if (t < 0.5f) {
            float f = t * 2.0f;
            r = 255;
            g = (int)(0x22 + f * (0xCC - 0x22));
            b = 0x22;
        } else {
            float f = (t - 0.5f) * 2.0f;
            r = (int)(255 * (1.0f - f));
            g = (int)(0xCC + f * (0xFF - 0xCC));
            b = (int)(0x44 * f);
        }
        // Pack as ARGB with full opacity
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }
}