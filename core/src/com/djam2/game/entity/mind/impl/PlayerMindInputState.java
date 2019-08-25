package com.djam2.game.entity.mind.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.djam2.game.entity.Direction;
import com.djam2.game.entity.EntityType;
import com.djam2.game.entity.impl.EntityBullet;
import com.djam2.game.entity.living.LivingEntity;
import com.djam2.game.entity.living.impl.EntityPlayer;
import com.djam2.game.entity.mind.EntityMind;
import com.djam2.game.entity.mind.EntityMindState;
import com.djam2.game.entity.weapon.Weapon;
import com.djam2.game.entity.weapon.impl.WeaponPlayerBasic;
import com.djam2.game.map.Map;
import com.djam2.game.ui.impl.WeaponBar;

public class PlayerMindInputState extends EntityMindState {

    private WeaponBar weaponBar;

    private EntityPlayer player;

    public PlayerMindInputState(EntityMind parentMind, WeaponBar weaponBar) {
        super(parentMind, "input");
        this.weaponBar = weaponBar;
        this.player = ((EntityPlayer) parentMind.getParentEntity());
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera, LivingEntity parentEntity) {

    }

    @Override
    public void update(OrthographicCamera camera, LivingEntity parentEntity) {
        this.getWeapon().update();

        float rotationSpeed = 700 * Gdx.graphics.getDeltaTime();

        float targetRotation = parentEntity.getRotation();

        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            parentEntity.move(Direction.UP);

            targetRotation = 0;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if(!(targetRotation == 180)) {
                targetRotation = -180;
            }

            //System.out.println("Moving down! Parent direction is " + parentEntity.getDirection());

            if(parentEntity.getDirection() == Direction.LEFT) {
                targetRotation = 180;
            }

            parentEntity.move(Direction.DOWN);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            parentEntity.move(Direction.RIGHT);

            targetRotation = -90;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            parentEntity.move(Direction.LEFT);

            targetRotation = 90;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            targetRotation = -45;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            targetRotation = 45;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            targetRotation = -135;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            targetRotation = 135;
        }

        if(parentEntity.getRotation() >= targetRotation) {
            parentEntity.setRotation(parentEntity.getRotation() - rotationSpeed);
        }

        if(parentEntity.getRotation() <= targetRotation) {
            parentEntity.setRotation(parentEntity.getRotation() + rotationSpeed);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            parentEntity.setRotation(parentEntity.getRotation() + 1);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            parentEntity.setRotation(parentEntity.getRotation() +-1);
        }

        //add upleft upright downleft downright etc on multiple button presses

        //System.out.println(parentEntity.getRotation() + " direction " + parentEntity.getDirection().name() + " Target rotation " + targetRotation);

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            this.fireBullet(camera);
        }

    }

    private void fireBullet(OrthographicCamera camera) {
        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);

        this.getWeapon().attemptFire(new Vector2(this.getParentEntity().getPosition()), new Vector2(mousePosition.x, mousePosition.y), this.getParentMap(), this.player.canFire());
    }

    @Override
    public void onCollision(Direction collisionDirection, Vector2 collisionPosition) {

    }

    public Weapon getWeapon() {
        return this.weaponBar.getSelectedWeapon();
    }

}
