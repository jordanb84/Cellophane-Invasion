package com.djam2.game.entity.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.assets.Assets;
import com.djam2.game.entity.Direction;
import com.djam2.game.entity.Entity;
import com.djam2.game.entity.EntityType;
import com.djam2.game.entity.living.EntityEnemy;
import com.djam2.game.entity.living.LivingEntity;
import com.djam2.game.entity.living.impl.EntityPlayer;
import com.djam2.game.map.Map;

public class EntityBullet extends Entity {

    private Vector2 destination;
    private Rectangle destinationBody;

    private float damage;

    private EntityType targetType;

    private float explosionSize;

    private float life;
    private float lifeMax;

    public EntityBullet(Vector2 position, Vector2 destination, Map parentMap, float damage, EntityType targetType, float speed, float explosionSize) {
        super(position, parentMap, 2, EntityType.PROJECTILE);
        this.destination = destination;
        this.setSprite(Assets.getInstance().getSprite("entity/bullet.png"));
        this.destinationBody = new Rectangle(this.destination.x, this.destination.y, this.getWidth(), this.getHeight());
        this.setSpeed(speed, speed);

        this.setRotation(((float) this.getRotationTowardPosition(this.destination)));

        //this.getAcceleration().set(this.getAcceleration().x * 0.85f, this.getAcceleration().y * 0.85f);
        this.getAcceleration().set(this.getAcceleration().x * 10, this.getAcceleration().y * 10);

        this.damage = damage;
        this.targetType = targetType;

        this.explosionSize = explosionSize;

        this.setLifeMax(0.8f);
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
            this.explode();
        }

        for(Entity entity : this.getParentMap().getEntities()) {
            if(entity.getEntityType() == this.targetType) {
                if (entity.getBody().overlaps(this.getBody())) {
                    ((LivingEntity) entity).damage(this.damage);

                    if(!(entity instanceof EntityPlayer) && !(entity instanceof EntityBullet)) {
                        entity.knockback(this.getRotation(), 2);
                    }
                    this.explode();
                }
            }
        }

        this.life += 1 * Gdx.graphics.getDeltaTime();

        if(this.targetType == EntityType.ENEMY) {
            if(this.life >= this.lifeMax) {
                this.explode();
            }

            try {
                EntityEnemy nearestEnemy = this.getParentMap().getNearestEnemy(this.getPosition());

                if (nearestEnemy != null) {
                    if (nearestEnemy.getPosition().dst(this.getPosition()) < 20) {
                        this.setRotation((float) this.getRotationTowardPosition(nearestEnemy.getPosition()));
                    }
                }
            } catch(IndexOutOfBoundsException noEnemies) {

            }
        }

        if(this.getParentMap().collisionAt(this.getBody())) {
            this.explode();
        }
    }

    @Override
    public void onCollision(Direction direction) {
        super.onCollision(direction);
        this.explode();
    }

    public void explode() {
        this.getParentMap().despawnEntity(this);
        this.getParentMap().spawnEntity(new EntityExplosion(new Vector2(this.getPosition()), this.getParentMap(), this.getRotation(), this.explosionSize));
    }

    @Override
    public void updateBody() {
        this.getBodyNoUpdate().set(this.getPosition().x - this.getWidth() / 4, this.getPosition().y - this.getHeight() / 4, this.getWidth() * 1.5f, this.getHeight() * 1.5f);
    }

    public void setLifeMax(float lifeMax) {
        this.lifeMax = lifeMax;
    }

}
