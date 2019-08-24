package com.djam2.game.entity.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.assets.Assets;
import com.djam2.game.entity.Entity;
import com.djam2.game.map.Map;

public class EntityBullet extends Entity {

    private Vector2 destination;
    private Rectangle destinationBody;

    public EntityBullet(Vector2 position, Vector2 destination, Map parentMap) {
        super(position, parentMap, 2);
        this.destination = destination;
        this.setSprite(Assets.getInstance().getSprite("entity/bullet.png"));
        this.destinationBody = new Rectangle(this.destination.x, this.destination.y, this.getWidth(), this.getHeight());
        this.setSpeed(2.8f, 2.8f);

        this.setRotation(((float) this.getRotationTowardPosition(this.destination)));

        //this.getAcceleration().set(this.getAcceleration().x * 0.85f, this.getAcceleration().y * 0.85f);
        this.getAcceleration().set(this.getAcceleration().x * 10, this.getAcceleration().y * 10);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.setRotation(((float) this.getRotationTowardPosition(this.destination)));

        this.moveAlongCurrentRotation();

        if(this.getBody().overlaps(this.destinationBody)) {
            this.getParentMap().despawnEntity(this);
            this.getParentMap().spawnEntity(new EntityExplosion(new Vector2(this.getPosition()), this.getParentMap(), this.getRotation()));
        }
    }

    public void moveAlongCurrentRotation() {
        float speed = this.getVelocity().x;
        speed = speed * 10 * Gdx.graphics.getDeltaTime();

        float xRotationMovement = -speed * (float) Math.cos(Math.toRadians(this.getRotation() - 90));
        float yRotationMovement = -speed * (float) Math.sin(Math.toRadians(this.getRotation() - 90));

        float delta = Gdx.graphics.getDeltaTime();

        this.getPosition().add(xRotationMovement, 0);
        this.getPosition().add(0, yRotationMovement);

        this.modifyVelocity(this.getAcceleration().x * 5 * delta, this.getAcceleration().y * 5 * delta, true);
    }

}
