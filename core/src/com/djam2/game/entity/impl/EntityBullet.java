package com.djam2.game.entity.impl;

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

    private float rotation;

    public EntityBullet(Vector2 position, Vector2 destination, Map parentMap) {
        super(position, parentMap, 2);
        this.destination = destination;
        this.setSprite(Assets.getInstance().getSprite("entity/bullet.png"));
        this.destinationBody = new Rectangle(this.destination.x, this.destination.y, this.getWidth(), this.getHeight());
        this.setSpeed(2.4f, 2.4f);

        this.rotation = ((float) this.getRotationTowardPosition(this.destination));

        //this.getAcceleration().set(this.getAcceleration().x * 0.85f, this.getAcceleration().y * 0.85f);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.getSprite().setRotation(this.rotation);
        super.render(batch, camera);
        this.getSprite().setRotation(0);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.moveAlongCurrentRotation();

        if(this.getBody().overlaps(this.destinationBody)) {
            this.getParentMap().despawnEntity(this);
            this.getParentMap().spawnEntity(new EntityExplosion(new Vector2(this.getPosition()), this.getParentMap()));
        }
    }

    public void moveAlongCurrentRotation() {
        this.getPosition().add(-this.getVelocity().x * (float) Math.cos(Math.toRadians(this.rotation - 90)), 0);
        this.getPosition().add(0, -this.getVelocity().x * (float) Math.sin(Math.toRadians(this.rotation - 90)));

        this.modifyVelocity(this.getAcceleration().x, this.getAcceleration().y, true);
    }

}
