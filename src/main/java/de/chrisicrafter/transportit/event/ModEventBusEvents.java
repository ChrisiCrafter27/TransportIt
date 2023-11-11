package de.chrisicrafter.transportit.event;

import de.chrisicrafter.transportit.TransportIt;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = TransportIt.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registryEvent(RegisterEvent event) {
        if(event.getRegistryKey().equals(ForgeRegistries.ENTITY_TYPES.getRegistryKey())) {
            TransportIt.LOGGER.info("--------------------------------------------------");
            TransportIt.LOGGER.info("Registering ENTITY TYPES");
            TransportIt.LOGGER.info("Forge Registry: " + event.getForgeRegistry());
            TransportIt.LOGGER.info("Vanilla Registry: " + event.getVanillaRegistry());
            TransportIt.LOGGER.info("--------------------------------------------------");
        }
    }
}
