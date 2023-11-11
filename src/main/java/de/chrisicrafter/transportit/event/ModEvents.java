package de.chrisicrafter.transportit.event;

import de.chrisicrafter.transportit.TransportIt;
import de.chrisicrafter.transportit.entity.ModEntities;
import de.chrisicrafter.transportit.entity.custom.CustomMinecartFurnace;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TransportIt.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onEntityUse(PlayerInteractEvent.EntityInteract event) {
        if(event.getLevel() instanceof ServerLevel level) {
            event.getEntity().sendSystemMessage(event.getTarget().getType().getDescription());
            TransportIt.LOGGER.info("Value Present: " + ModEntities.CUSTOM_MINECART_FURNACE.isPresent());
            TransportIt.LOGGER.info("Holder Present: " + ModEntities.CUSTOM_MINECART_FURNACE.getHolder().isPresent());
            TransportIt.LOGGER.info("Id: " + ModEntities.CUSTOM_MINECART_FURNACE.getId());
            TransportIt.LOGGER.info("Key: " + ModEntities.CUSTOM_MINECART_FURNACE.getKey().toString());
            TransportIt.LOGGER.info("Value: " + ModEntities.CUSTOM_MINECART_FURNACE.get());
        }
    }

}
