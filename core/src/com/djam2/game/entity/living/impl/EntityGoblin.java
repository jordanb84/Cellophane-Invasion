package com.djam2.game.entity.living.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.animation.Animation;
import com.djam2.game.animation.DirectionalAnimation;
import com.djam2.game.assets.Assets;
import com.djam2.game.entity.living.EntityEnemy;
import com.djam2.game.entity.mind.EntityMind;
import com.djam2.game.entity.mind.impl.InvasionMind;
import com.djam2.game.map.Map;

public class EntityGoblin extends EntityEnemy {

    public EntityGoblin(Vector2 position, Map parentMap) {
        super(position, parentMap, 6);
        this.setSpeed(4, 4);
        this.addPhysicsBody();
        this.addLight(Color.WHITE, 40); //TODO green maybe?
        this.setHealth(140);
    }

    @Override
    public DirectionalAnimation setupAnimation() {
        Animation animation = new Animation(1);
        animation.addFrame("entity/goblin.png");

        DirectionalAnimation directionalAnimation = new DirectionalAnimation(animation);

        return directionalAnimation;
    }

    @Override
    public EntityMind setupMind() {
        return new InvasionMind(this);
    }

    @Override
    public Sprite getHurtSprite() { //TODO getHurtSprite(int stage), returns getStage(0) if null
        return Assets.getInstance().getSprite("entity/goblin_blood0.png");
    }
}
