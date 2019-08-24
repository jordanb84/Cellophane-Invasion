package com.djam2.game.entity.living.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.animation.Animation;
import com.djam2.game.animation.DirectionalAnimation;
import com.djam2.game.entity.living.LivingEntity;
import com.djam2.game.entity.mind.EntityMind;
import com.djam2.game.entity.mind.impl.PlayerMind;
import com.djam2.game.map.Map;

public class EntityPlayer extends LivingEntity {

    public EntityPlayer(Vector2 position, Map parentMap) {
        super(position, parentMap, 5);
        this.setSpeed(11, 11);
        this.addPhysicsBody();
        this.addLight(Color.WHITE, 60);
    }

    @Override
    public DirectionalAnimation setupAnimation() {
        float frameDuration = 0.3f;

        Animation upAnimation = new Animation(frameDuration);
        upAnimation.addFrames("entity/player_up0.png", "entity/player_up1.png");

        Animation downAnimation = new Animation(frameDuration);
        downAnimation.addFrames("entity/player_up0.png", "entity/player_up1.png");

        Animation rightAnimation = new Animation(frameDuration);
        rightAnimation.addFrames("entity/player_right0.png", "entity/player_right1.png");

        Animation leftAnimation = new Animation(frameDuration);
        leftAnimation.addFrames("entity/player_left0.png", "entity/player_left1.png");

        DirectionalAnimation directionalAnimation = new DirectionalAnimation(upAnimation, downAnimation, rightAnimation, leftAnimation);

        return directionalAnimation;
    }

    @Override
    public EntityMind setupMind() {
        return new PlayerMind(this);
    }

}
