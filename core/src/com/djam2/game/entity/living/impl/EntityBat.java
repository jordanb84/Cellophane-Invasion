package com.djam2.game.entity.living.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.animation.Animation;
import com.djam2.game.animation.DirectionalAnimation;
import com.djam2.game.entity.living.EntityEnemy;
import com.djam2.game.entity.living.LivingEntity;
import com.djam2.game.entity.mind.EntityMind;
import com.djam2.game.entity.mind.impl.InvasionMind;
import com.djam2.game.entity.mind.impl.PlayerMind;
import com.djam2.game.map.Map;

public class EntityBat extends EntityEnemy {

    public EntityBat(Vector2 position, Map parentMap) {
        super(position, parentMap, 6);
        this.setSpeed(11, 11);
        this.addPhysicsBody();
        this.addLight(Color.WHITE, 60);
        this.setHealth(1000);
    }

    @Override
    public DirectionalAnimation setupAnimation() {
        Animation animation = new Animation(1);
        animation.addFrame("entity/bat.png");

        DirectionalAnimation directionalAnimation = new DirectionalAnimation(animation);

        return directionalAnimation;
    }

    @Override
    public EntityMind setupMind() {
        return new InvasionMind(this);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        System.out.println(this.getVelocity().x + "/" + this.getVelocity().y);
    }
}
