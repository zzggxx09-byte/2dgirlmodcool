package com.example.doommod.client;

import com.example.doommod.DoomMod;
import com.example.doommod.entity.EntityDoomMob;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderDoomMob extends Render<EntityDoomMob> {

    // Порядок важливий тільки для назв файлів — сам поворот рахується нижче.
    private static final String[] DIRECTIONS = {"front", "right", "back", "left"};

    public RenderDoomMob(RenderManager manager) {
        super(manager);
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(EntityDoomMob entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        // Billboard: повертаємо квад лицем до камери по горизонталі
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

        bindTexture(getEntityTexture(entity));

        float halfWidth = 0.4F; // ширина квада в блоках, підбери під розмір
        float height = 1.8F;    // висота квада в блоках (64x128 -> співвідношення 1:2)

        GlStateManager.disableCull();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(-halfWidth, height, 0.0D).tex(0.0D, 0.0D).endVertex();
        buffer.pos(-halfWidth, 0.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
        buffer.pos(halfWidth, 0.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
        buffer.pos(halfWidth, height, 0.0D).tex(1.0D, 0.0D).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.enableCull();

        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDoomMob entity) {
        int dirIndex = getDirectionIndex(entity);
        String dirName = DIRECTIONS[dirIndex];
        String stateName = entity.getAnimState().name().toLowerCase(); // idle / walk / run
        int frame = entity.getCurrentFrame();

        String path = "textures/entity/doommob/" + stateName + "/" + dirName + "_" + frame + ".png";
        return new ResourceLocation(DoomMod.MODID, path);
    }

    /**
     * Рахує з якого боку гравець дивиться на моба (0=front,1=right,2=back,3=left),
     * враховуючи куди сам моб дивиться (rotationYaw).
     * Якщо після тесту напрямки виявляться переплутані (наприклад бачиш "спину",
     * коли дивишся моба в обличчя) — поміняй знак/порядок нижче.
     */
    private int getDirectionIndex(EntityDoomMob entity) {
        double dx = this.renderManager.renderViewEntity.posX - entity.posX;
        double dz = this.renderManager.renderViewEntity.posZ - entity.posZ;
        double angleToCamera = Math.toDegrees(Math.atan2(dz, dx)) - 90.0D;
        double relative = MathHelper.wrapDegrees(entity.rotationYaw - angleToCamera);
        relative = (relative + 360.0D) % 360.0D;

        if (relative >= 45 && relative < 135) return 3;   // left
        if (relative >= 135 && relative < 225) return 2;  // back
        if (relative >= 225 && relative < 315) return 1;  // right
        return 0; // front
    }
}
