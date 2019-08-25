package com.djam2.game.entity.impl;

import com.badlogic.gdx.math.Vector2;
import com.djam2.game.assets.Assets;
import com.djam2.game.entity.EntityType;
import com.djam2.game.map.Map;

public class EntityBulletBurst extends EntityBullet {

    public EntityBulletBurst(Vector2 position, Vector2 destination, Map parentMap, float damage, EntityType targetType, float speed, float explosionSize) {
        super(position, destination, parentMap, damage, targetType, speed, explosionSize);
        this.setSprite(Assets.getInstance().getSprite("entity/burst.png"));
    }

}
