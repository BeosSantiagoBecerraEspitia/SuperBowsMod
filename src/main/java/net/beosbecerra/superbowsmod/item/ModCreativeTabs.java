package net.beosbecerra.superbowsmod.item;

import net.beosbecerra.superbowsmod.SuperBowsMod;
import net.beosbecerra.superbowsmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SuperBowsMod.MODID);

    public static final RegistryObject<CreativeModeTab> SUPER_BOWS_TAB =
            CREATIVE_MODE_TABS.register("superbows_tab", () ->
                    CreativeModeTab.builder()
                            .icon(() -> new ItemStack(ModItems.TRAIL_BOW.get()))
                            .title(Component.translatable("creativetab.superbows_tab"))
                            .displayItems((parameters, output) -> {


                                output.accept(ModBlocks.TRAIL_DIRT.get());
                                output.accept(ModItems.TRAIL_BOW.get());
                                output.accept(ModItems.TEST_BOW.get());
                                output.accept(ModItems.HOMING_BOW.get());
                                output.accept(ModItems.ORBIT_BOW.get());
                                output.accept(ModItems.SOY_BOW.get());
                                output.accept(ModItems.GOAT_BOW.get());
                                output.accept(ModItems.LAUNCH_BOW.get());
                                output.accept(ModItems.TNT_BOW.get());
                                output.accept(ModItems.EXPELLER_BOW.get());
                                output.accept(ModItems.FROZEN_BOW.get());
                                output.accept(ModItems.BEE_BOW.get());
                                output.accept(ModItems.CLUSTER_BOW.get());
                                output.accept(ModItems.RAY_BOW.get());
                                output.accept(ModItems.GIANT_BOW.get());
                                output.accept(ModItems.UPGRADE.get());
                                output.accept(ModItems.SUPERMAGICCRYSTAL.get());

                            })
                            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}