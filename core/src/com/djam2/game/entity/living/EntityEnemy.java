package com.djam2.game.entity.living;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.entity.EntityType;
import com.djam2.game.map.Map;

public abstract class EntityEnemy extends LivingEntity {

    private Sprite hurtSprite;

    public EntityEnemy(Vector2 position, Map parentMap, float weight) {
        super(position, parentMap, weight, EntityType.ENEMY);
        this.hurtSprite = this.getHurtSprite();
    }

    public abstract Sprite getHurtSprite();

    @Override
    public Sprite getSprite() {
        if(this.isDamaged()) {
            return this.hurtSprite;
        } else {
            return super.getSprite();
        }
    }

}
