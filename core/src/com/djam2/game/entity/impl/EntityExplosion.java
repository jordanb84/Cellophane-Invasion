package com.djam2.game.entity.impl;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.animation.Animation;
import com.djam2.game.assets.Assets;
import com.djam2.game.entity.Entity;
import com.djam2.game.entity.EntityType;
import com.djam2.game.map.Map;

public class EntityExplosion extends Entity {

    private Animation animation;

    private float explosionSize;

    public EntityExplosion(Vector2 position, Map parentMap, float rotation, float explosionSize) {
        super(position, parentMap, 2, EntityType.ENVIRONMENT);
        this.setRotation(rotation);
        this.animation = new Animation(0.12f);
        this.animation.addFrames("entity/explosion0.png", "entity/explosion1.png", "entity/explosion2.png", "entity/explosion3.png");
        this.animation.addFrames("entity/explosion4.png", "entity/explosion5.png", "entity/explosion6.png", "entity/explosion7.png");
        this.animation.addFrames("entity/explosion8.png", "entity/explosion9.png", "entity/explosion10.png", "entity/explosion11.png");

        this.explosionSize = explosionSize;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.getSprite().setPosition(this.getPosition().x - this.getWidth() / 2, this.getPosition().y - this.getHeight() / 2);
        this.getSprite().setRotation(this.getRotation());
        this.getSprite().setAlpha(0.8f);
        this.getSprite().setScale(this.explosionSize);
        this.getSprite().draw(batch);
        this.getSprite().setRotation(0);
        this.getSprite().setAlpha(1f);
        this.getSprite().setScale(1);
    }

    @Override
    public void update(OrthographicCamera camera) {
        this.setSprite(this.animation.getCurrentFrame().getSprite());
        super.update(camera);
        this.animation.update();

        if(this.animation.isCompleted()) {
            this.getParentMap().despawnEntity(this);
        }
    }

}
