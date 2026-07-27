package com.example.doommod.client;

import com.example.doommod.DoomMod;
import com.example.doommod.entity.EntityDoomMob;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderDoomMob extends Render<EntityDoomMob> {

    private static final String[] DIRECTIONS = {"front", "right", "back", "left"};

    public RenderDoomMob(RenderManager manager) {
        super(manager);
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(EntityDoomMob entity, double x, double y, double z, float entityYaw, float partialTicks) {
        System.out.println("!!! DOOMMOD: doRender called !!!");

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

        GlStateManager.disableTexture2D();
GlStateManager.color(1.0F, 0.0F, 0.0F, 1.0F); // яскраво-червоний

        float halfWidth = 0.4F;
        float height = 1.8F;

        GlStateManager.disableCull();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(-halfWidth, height, 0.0D).tex(0.0D, 0.0D).endVertex();
        buffer.pos(-halfWidth, 0.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
        buffer.pos(halfWidth, 0.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
        buffer.pos(halfWidth, height, 0.0D).tex(1.0D, 0.0D).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture2D();
GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableCull();

        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDoomMob entity) {
        return new ResourceLocation(DoomMod.MODID, "textures/entity/doommob/run/back_0.png");
    }

    private int getDirectionIndex(EntityDoomMob entity) {
        double dx = this.renderManager.renderViewEntity.posX - entity.posX;
        double dz = this.renderManager.renderViewEntity.posZ - entity.posZ;
        double angleToCamera = Math.toDegrees(Math.atan2(dz, dx)) - 90.0D;
        double relative = MathHelper.wrapDegrees(entity.rotationYaw - angleToCamera);
        relative = (relative + 360.0D) % 360.0D;

        if (relative >= 45 && relative < 135) return 3;
        if (relative >= 135 && relative < 225) return 2;
        if (relative >= 225 && relative < 315) return 1;
        return 0;
    }
}
