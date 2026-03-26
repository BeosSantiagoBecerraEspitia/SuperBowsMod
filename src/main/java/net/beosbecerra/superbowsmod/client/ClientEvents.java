package net.beosbecerra.superbowsmod.client;

import net.beosbecerra.superbowsmod.SuperBowsMod;
import net.beosbecerra.superbowsmod.entity.ModEntities;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SuperBowsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.TEST_ARROW.get(), ModArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.TRAIL_ARROW.get(), TippableArrowRenderer::new);  // add TrailArrow here too if not yet done
        event.registerEntityRenderer(ModEntities.HOMING_ARROW.get(), ModArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.ORBIT_ARROW.get(), ModArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.SOY_ARROW.get(), TippableArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.LAUNCH_ARROW.get(), TippableArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.TNT_ARROW.get(), TippableArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.EXPELLER_ARROW.get(), TippableArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.FROZEN_ARROW.get(), TippableArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.FROZEN_STATUE.get(), FrozenStatueRenderer::new);
        event.registerEntityRenderer(ModEntities.BEE_ARROW.get(), TippableArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.RAY_ARROW.get(), RayArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.GIANT_ARROW.get(), GiantArrowRenderer::new);



    }
}