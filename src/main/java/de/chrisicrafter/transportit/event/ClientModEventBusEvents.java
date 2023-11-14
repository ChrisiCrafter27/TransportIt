package de.chrisicrafter.transportit.event;

import de.chrisicrafter.transportit.TransportIt;
import de.chrisicrafter.transportit.screen.CustomMinecartCommandBlockEditScreen;
import net.minecraft.client.gui.screens.inventory.MinecartCommandBlockEditScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TransportIt.MOD_ID, value = Dist.CLIENT)
public class ClientModEventBusEvents {

    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        if(event.getNewScreen() instanceof MinecartCommandBlockEditScreen screen) event.setNewScreen(new CustomMinecartCommandBlockEditScreen(screen.getCommandBlock()));
    }
}
