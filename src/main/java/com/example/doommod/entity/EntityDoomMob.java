package com.example.doommod.entity;

import com.example.doommod.ai.EntityAIRandomSprint;
import com.example.doommod.init.ModSounds;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityDoomMob extends EntityMob {

    public enum AnimState { IDLE, WALK, RUN }

    private static final int IDLE_FRAMES = 8;
    private static final int WALK_FRAMES = 10;
    private static final int RUN_FRAMES = 8;
    private static final int TICKS_PER_FRAME = 4; // швидкість анімації, підбери сам

    private int animTicks;
    private int currentFrame;

    public EntityDoomMob(World world) {
        super(world);
        this.setSize(0.6F, 1.8F); // хітбокс (не розмір спрайту!)
        this.experienceValue = 5;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(2, new EntityAIRandomSprint(this, 0.02F)); // шанс переходу на біг за тік
        this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        int frameCount = getFrameCountForState(getAnimState());
        this.animTicks++;
        if (this.animTicks >= TICKS_PER_FRAME) {
            this.animTicks = 0;
            this.currentFrame = (this.currentFrame + 1) % frameCount;
        }
        if (this.currentFrame >= frameCount) {
            this.currentFrame = 0;
        }
    }

    public AnimState getAnimState() {
        double speedSq = this.motionX * this.motionX + this.motionZ * this.motionZ;
        if (speedSq < 0.0025D) return AnimState.IDLE;
        return this.isSprinting() ? AnimState.RUN : AnimState.WALK;
    }

    public static int getFrameCountForState(AnimState state) {
        switch (state) {
            case WALK: return WALK_FRAMES;
            case RUN:  return RUN_FRAMES;
            default:   return IDLE_FRAMES;
        }
    }

    public int getCurrentFrame() {
        return this.currentFrame;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.rand.nextBoolean() ? ModSounds.DOOM_AMBIENT1 : ModSounds.DOOM_AMBIENT2;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.DOOM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.DOOM_HURT; // за бажанням заміни на окремий звук смерті
    }
}
