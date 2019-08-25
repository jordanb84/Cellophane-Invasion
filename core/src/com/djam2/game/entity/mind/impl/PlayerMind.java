package com.djam2.game.entity.mind.impl;

import com.djam2.game.entity.living.LivingEntity;
import com.djam2.game.entity.mind.EntityMind;
import com.djam2.game.ui.impl.WeaponBar;

public class PlayerMind extends EntityMind {

    public PlayerMind(LivingEntity parentEntity, WeaponBar weaponBar) {
        super(parentEntity);
        this.registerState(new PlayerMindInputState(this, weaponBar));
        this.setState("input");
    }

    @Override
    public void recalculateState(LivingEntity parentEntity) {
        this.setState("input");
    }

}
