package com.djam2.game.entity.mind.impl;

import com.djam2.game.entity.living.LivingEntity;
import com.djam2.game.entity.mind.EntityMind;

public class InvasionMind extends EntityMind {

    public InvasionMind(LivingEntity parentEntity) {
        super(parentEntity);
        this.registerState(new InvasionApproachMindState(this));
        this.setState("approach");
    }

    @Override
    public void recalculateState(LivingEntity parentEntity) {
        this.setState("approach");
        //approach (player/center base), then if near either, go to attack state
    }

}
