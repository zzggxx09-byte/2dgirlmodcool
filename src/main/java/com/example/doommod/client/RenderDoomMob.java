package com.example.doommod.client;

import com.example.doommod.DoomMod;
import com.example.doommod.entity.EntityDoomMob;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderDoomMob extends Render<EntityDoomMob> {

    // Напрямки огляду
    private static final String[] DIRECTIONS = {"front", "right", "back", "left"};

    public RenderDoomMob(RenderManager manager) {
        super(manager);
        this.shadowSize = 0.5F; // Розмір тіні під мобом
    }

    @Override
    public void doRender(EntityDoomMob entity, double x, double y, double z, float entityYaw, float partialTicks) {
        System.out.println("!!! DOOMMOD: doRender called !!!");

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        // 1. Умикаємо відображення 2D-текстур та скидаємо колірний фільтр у білий
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // 2. Налаштовуємо прозорість (щоб навколо спрайту не було чорної/білої рамки)
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F); // Відсікаємо повністю прозорі пікселі
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        // 3. Біллбординг: повертаємо площину обличчям до камери по горизонталі (Y)
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

        // 4. Прив'язуємо поточну текстуру (залежно від стану, напрямку та кадру)
        this.bindTexture(getEntityTexture(entity));

        // Розміри площини (Quad) у блоках
        float halfWidth = 0.4F; // Ширина (загальна буде 0.8 блока)
        float height = 1.8F;    // Висота моба

        // 5. Вимикаємо Culling, щоб спрайт було видно з обох боків
        GlStateManager.disableCull();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // Будуємо 2D-квад із тексурою
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(-halfWidth, height, 0.0D).tex(0.0D, 0.0D).endVertex();
        buffer.pos(-halfWidth, 0.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
        buffer.pos(halfWidth, 0.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
        buffer.pos(halfWidth, height, 0.0D).tex(1.0D, 0.0D).endVertex();
        tessellator.draw();

        // Відновлюємо початковий стан OpenGL
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDoomMob entity) {
        int dirIndex = getDirectionIndex(entity);
        String dirName = DIRECTIONS[dirIndex];
        
        // Отримуємо стан (idle / walking / run) і поточний кадр з класу Entity
        String stateName = entity.getAnimState().name().toLowerCase(); 
        int frame = entity.getCurrentFrame();

        // Формуємо шлях до PNG-файлу
        String path = "textures/entity/doommob/" + stateName + "/" + dirName + "_" + frame + ".png";
        return new ResourceLocation(DoomMod.MODID, path);
    }

    /**
     * Розрахунок кута огляду (0=front, 1=right, 2=back, 3=left)
     */
    private int getDirectionIndex(EntityDoomMob entity) {
        if (this.renderManager.renderViewEntity == null) return 0;

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
