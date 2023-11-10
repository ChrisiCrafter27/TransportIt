package de.chrisicrafter.transportit.event;

import de.chrisicrafter.transportit.TransportIt;
import de.chrisicrafter.transportit.entity.ModEntities;
import de.chrisicrafter.transportit.entity.custom.CustomMinecartFurnace;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TransportIt.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onBlockMine(BlockEvent.BreakEvent event) {
        if(event.getLevel() instanceof ServerLevel level) {
            //To test if the entity can be spawned. The error occurs here.
            CustomMinecartFurnace entity = new CustomMinecartFurnace(ModEntities.CUSTOM_MINECART_FURNACE.get(), level);
            entity.setPos(event.getPos().getCenter());
            level.addFreshEntity(entity);
        }
    }

}
