package com.example.doommod.init;

import com.example.doommod.DoomMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ModSounds {

    public static final SoundEvent DOOM_AMBIENT1 = create("doommob.ambient1");
    public static final SoundEvent DOOM_AMBIENT2 = create("doommob.ambient2");
    public static final SoundEvent DOOM_HURT = create("doommob.hurt");

    private static SoundEvent create(String name) {
        ResourceLocation loc = new ResourceLocation(DoomMod.MODID, name);
        return new SoundEvent(loc).setRegistryName(loc);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(DOOM_AMBIENT1);
        event.getRegistry().register(DOOM_AMBIENT2);
        event.getRegistry().register(DOOM_HURT);
    }
}
