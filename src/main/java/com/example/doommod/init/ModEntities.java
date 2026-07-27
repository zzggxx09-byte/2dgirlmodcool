package com.example.doommod.init;

import com.example.doommod.DoomMod;
import com.example.doommod.entity.EntityDoomMob;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
    public static void register() {
        EntityRegistry.registerModEntity(
                new ResourceLocation(DoomMod.MODID, "doommob"),
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
