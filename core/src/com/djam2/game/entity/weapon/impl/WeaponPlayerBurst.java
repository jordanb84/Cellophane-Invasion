package com.djam2.game.entity.weapon.impl;

import com.badlogic.gdx.math.Vector2;
import com.djam2.game.entity.EntityType;
import com.djam2.game.entity.impl.EntityBullet;
import com.djam2.game.entity.impl.EntityBulletBurstBall;
import com.djam2.game.entity.weapon.Weapon;
import com.djam2.game.map.Map;

public class WeaponPlayerBurst extends Weapon {

    public WeaponPlayerBurst() {
        super(0.2f);
    }

    @Override
    public void fire(Vector2 position, Vector2 destination, Map parentMap) {
        float damage = 15;
        EntityBulletBurstBall bullet = new EntityBulletBurstBall(position, destination, parentMap, damage, EntityType.ENEMY, 2.8f, 0.4f);

        parentMap.spawnEntity(bullet);
    }

}
