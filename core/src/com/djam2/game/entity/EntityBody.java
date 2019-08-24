package com.djam2.game.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.map.Map;

import java.util.Random;

public class EntityBody extends Entity {

    public EntityBody(Vector2 position, Map parentMap, Sprite bodySprite, float rotation) {
        super(position, parentMap, 1, EntityType.ENVIRONMENT);
        this.setSprite(bodySprite);
        this.setRotation(rotation + new Random().nextInt(100));
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.getSprite().setPosition(this.getPosition().x, this.getPosition().y);
        this.getSprite().setRotation(this.getRotation());
        this.getSprite().setAlpha(0.5f);
        this.getSprite().draw(batch);
        this.getSprite().setRotation(0);
        this.getSprite().setAlpha(1f);
    }

}
