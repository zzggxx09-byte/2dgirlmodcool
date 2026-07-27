package com.example.doommod.client;

import com.example.doommod.CommonProxy;
import com.example.doommod.entity.EntityDoomMob;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLLog;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderers() {
        FMLLog.log.info("!!! DOOMMOD: registerRenderers() called !!!");
        RenderingRegistry.registerEntityRenderingHandler(EntityDoomMob.class, RenderDoomMob::new);
        FMLLog.log.info("!!! DOOMMOD: renderer registered !!!");
    }
}
