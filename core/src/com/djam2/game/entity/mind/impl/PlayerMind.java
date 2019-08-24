package com.djam2.game.entity.mind.impl;

import com.djam2.game.entity.living.LivingEntity;
import com.djam2.game.entity.mind.EntityMind;

public class PlayerMind extends EntityMind {

    public PlayerMind(LivingEntity parentEntity) {
        super(parentEntity);
        this.registerState(new PlayerMindInputState(this));
        this.setState("input");
    }

    @Override
    public void recalculateState(LivingEntity parentEntity) {
        this.setState("input");
    }

}
