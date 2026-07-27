package com.example.doommod.client;

import com.example.doommod.CommonProxy;
import com.example.doommod.entity.EntityDoomMob;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityDoomMob.class, RenderDoomMob::new);
    }
}
