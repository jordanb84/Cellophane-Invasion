package com.djam2.game.entity.impl;

import com.badlogic.gdx.math.Vector2;
import com.djam2.game.entity.EntityType;
import com.djam2.game.map.Map;

public class EntityBulletBurstBall extends EntityBullet {

    public EntityBulletBurstBall(Vector2 position, Vector2 destination, Map parentMap, float damage, EntityType targetType, float speed, float explosionSize) {
        super(position, destination, parentMap, damage, targetType, speed, explosionSize);
    }

    @Override
    public void explode() {
        super.explode();

    }
}
