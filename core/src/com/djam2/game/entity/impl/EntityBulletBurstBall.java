package com.djam2.game.entity.impl;

import com.badlogic.gdx.math.Vector2;
import com.djam2.game.assets.Assets;
import com.djam2.game.entity.EntityType;
import com.djam2.game.map.Map;

public class EntityBulletBurstBall extends EntityBullet {

    public EntityBulletBurstBall(Vector2 position, Vector2 destination, Map parentMap, float damage, EntityType targetType, float speed, float explosionSize) {
        super(position, destination, parentMap, damage, targetType, speed, explosionSize);
        this.setSprite(Assets.getInstance().getSprite("entity/burstball.png"));
    }

    @Override
    public void explode() {
        super.explode();
        float rotation = 0;

        while(rotation < 360) {
            Vector2 destination = this.getPositionForRotation(rotation, 100);
            EntityBulletBurst bulletBurst = new EntityBulletBurst(new Vector2(this.getPosition()), destination, this.getParentMap(), 100, EntityType.ENEMY, 3.4f, 0.8f);

            this.getParentMap().spawnEntity(bulletBurst);

            rotation += 15;
        }
    }
}
