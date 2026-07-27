package com.example.doommod.ai;

import com.example.doommod.entity.EntityDoomMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

import java.util.UUID;

public class EntityAIRandomSprint extends EntityAIBase {

    private static final UUID SPRINT_MODIFIER_UUID = UUID.fromString("b1f1f1a0-0000-4000-8000-000000000001");
    // operation 2 = MULTIPLY_TOTAL: швидкість * (1 + amount)
    private static final AttributeModifier SPRINT_BOOST =
            new AttributeModifier(SPRINT_MODIFIER_UUID, "doommob.sprintBoost", 0.5D, 2).setSaved(false);

    private final EntityDoomMob mob;
    private final float chancePerTick;
    private int sprintTimer;

    public EntityAIRandomSprint(EntityDoomMob mob, float chancePerTick) {
        this.mob = mob;
        this.chancePerTick = chancePerTick;
        this.setMutexBits(0);
    }

    @Override
    public boolean shouldExecute() {
        return mob.getAttackTarget() != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return mob.getAttackTarget() != null;
    }

    @Override
    public void updateTask() {
        IAttributeInstance speed = mob.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (sprintTimer > 0) {
            sprintTimer--;
            if (sprintTimer == 0) {
                mob.setSprinting(false);
                if (speed.hasModifier(SPRINT_BOOST)) speed.removeModifier(SPRINT_BOOST);
            }
            return;
        }

        if (mob.getRNG().nextFloat() < chancePerTick) {
            mob.setSprinting(true);
            if (!speed.hasModifier(SPRINT_BOOST)) speed.applyModifier(SPRINT_BOOST);
            sprintTimer = 60 + mob.getRNG().nextInt(80); // ~3-7 сек бігу
        }
    }

    @Override
    public void resetTask() {
        mob.setSprinting(false);
        IAttributeInstance speed = mob.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (speed.hasModifier(SPRINT_BOOST)) speed.removeModifier(SPRINT_BOOST);
        sprintTimer = 0;
    }
}
