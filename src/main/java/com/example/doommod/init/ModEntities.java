package com.example.doommod.init;

import com.example.doommod.DoomMod;
import com.example.doommod.entity.EntityDoomMob;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
    public static void register() {
        EntityRegistry.registerModEntity(
                EntityDoomMob.class,
                "doommob",
                1,                 // унікальний id ентіті в межах мода
                DoomMod.instance,
                64,                // tracking range
                3,                 // update frequency
                true               // sendsVelocityUpdates
        );
    }
}
