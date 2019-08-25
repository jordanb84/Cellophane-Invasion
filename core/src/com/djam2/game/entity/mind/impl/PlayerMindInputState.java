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
import com.djam2.game.entity.mind.EntityMind;
import com.djam2.game.entity.mind.EntityMindState;
import com.djam2.game.map.Map;

public class PlayerMindInputState extends EntityMindState {

    private float fireInterval = 0.2f;
    private float elapsedSinceFire;

    public PlayerMindInputState(EntityMind parentMind) {
        super(parentMind, "input");
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera, LivingEntity parentEntity) {

    }

    @Override
    public void update(OrthographicCamera camera, LivingEntity parentEntity) {
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
            if(this.elapsedSinceFire >= this.fireInterval) {
                this.fireBullet(camera);
            }
        }

        this.elapsedSinceFire += 1 * Gdx.graphics.getDeltaTime();
    }

    private void fireBullet(OrthographicCamera camera) {
        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);

        Vector2 bulletDestination = new Vector2(mousePosition.x, mousePosition.y);

        float damage = 35;
        EntityBullet bullet = new EntityBullet(new Vector2(this.getParentEntity().getPosition()), bulletDestination, this.getParentMap(), damage, EntityType.ENEMY, 3.2f, 0.8f);

        this.getParentMap().spawnEntity(bullet);

        this.elapsedSinceFire = 0;
    }

    @Override
    public void onCollision(Direction collisionDirection, Vector2 collisionPosition) {

    }

}
