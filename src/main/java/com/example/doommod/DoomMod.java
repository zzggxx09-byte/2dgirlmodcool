package com.example.doommod;

import com.example.doommod.init.ModEntities;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = DoomMod.MODID, name = DoomMod.NAME, version = DoomMod.VERSION)
public class DoomMod {

    public static final String MODID = "doommod";
    public static final String NAME = "Doom Mob Mod";
    public static final String VERSION = "1.0";

    @Mod.Instance(MODID)
    public static DoomMod instance;

    @SidedProxy(clientSide = "com.example.doommod.client.ClientProxy",
                serverSide = "com.example.doommod.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModEntities.register();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerRenderers();
    }
}
