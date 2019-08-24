package com.djam2.game.entity.living;

import com.badlogic.gdx.math.Vector2;
import com.djam2.game.entity.EntityType;
import com.djam2.game.map.Map;

public abstract class EntityEnemy extends LivingEntity {

    public EntityEnemy(Vector2 position, Map parentMap, float weight) {
        super(position, parentMap, weight, EntityType.ENEMY);
    }

}
