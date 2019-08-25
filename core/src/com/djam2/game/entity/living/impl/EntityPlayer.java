package com.djam2.game.entity.living.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.animation.Animation;
import com.djam2.game.animation.DirectionalAnimation;
import com.djam2.game.entity.EntityType;
import com.djam2.game.entity.living.LivingEntity;
import com.djam2.game.entity.mind.EntityMind;
import com.djam2.game.entity.mind.impl.PlayerMind;
import com.djam2.game.entity.weapon.Weapon;
import com.djam2.game.map.Map;
import com.djam2.game.ui.impl.WeaponBar;

public class EntityPlayer extends LivingEntity {

    private WeaponBar weaponBar;

    private boolean canFire;

    public EntityPlayer(Vector2 position, Map parentMap) {
        super(position, parentMap, 5, EntityType.PLAYER);
        this.setSpeed(11, 11);
        this.addPhysicsBody();
        this.addLight(Color.WHITE, 60);
        this.setHealth(100);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        camera.position.set(this.getPosition().x + this.getWidth() / 2, this.getPosition().y + this.getHeight() / 2, 0);
        camera.update();

        this.setCanFire(true);
        this.weaponBar.update(camera);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
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
        this.weaponBar = new WeaponBar(this);
        return new PlayerMind(this, this.weaponBar);
    }

    public void resize(int width, int height) {
        this.weaponBar.resize(width, height);
    }

    public boolean canFire() {
        return this.canFire;
    }

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }

    public WeaponBar getWeaponBar() {
        return this.weaponBar;
    }

}
