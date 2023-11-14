package de.chrisicrafter.transportit.event;

import com.google.common.eventbus.Subscribe;
import de.chrisicrafter.transportit.TransportIt;
import de.chrisicrafter.transportit.block.custom.CustomPoweredRailBlock;
import de.chrisicrafter.transportit.entity.ModEntities;
import de.chrisicrafter.transportit.entity.custom.CustomMinecartFurnace;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
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

        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if(event.getLevel() instanceof ServerLevel level && event.getEntity() instanceof Player player) {
            //player.sendSystemMessage(Component.literal("Is custom rail: " + (event.getPlacedBlock().getBlock() instanceof CustomPoweredRailBlock)));
        }
    }

}
