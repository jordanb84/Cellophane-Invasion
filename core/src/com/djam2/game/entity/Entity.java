package com.djam2.game.entity;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.map.Map;
import org.w3c.dom.css.Rect;

public abstract class Entity {

    private Vector2 position;

    private Direction direction;

    private Sprite sprite;

    private Map parentMap;

    private float weight;
    private Vector2 velocity = new Vector2();
    private Vector2 maxVelocity = new Vector2();
    private Vector2 acceleration = new Vector2();

    private boolean colliding;

    private Rectangle body;

    private PointLight light;

    private float rotation;

    private EntityType entityType;

    private boolean applyingKnockback;
    private float knockbackAngle;
    private float knockbackDuration;
    private float knockbackElapsed;

    public Entity(Vector2 position, Map parentMap, float weight, EntityType entityType) {
        this.position = position;
        this.parentMap = parentMap;
        this.direction = Direction.RIGHT;
        this.weight = weight;
        this.body = new Rectangle();
        this.entityType = entityType;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.getSprite().setPosition(this.getPosition().x, this.getPosition().y);
        this.getSprite().setRotation(this.getRotation());
        this.getSprite().draw(batch);
        this.getSprite().setRotation(0);

        this.renderShadow(batch);
    }

    public void renderShadow(SpriteBatch batch) {
        this.getSprite().setColor(Color.BLACK);
        this.getSprite().setPosition(this.getPosition().x - this.getWidth(), this.getPosition().y - this.getHeight());
        this.getSprite().setRotation(this.getRotation());
        this.getSprite().setAlpha(0.5f);
        this.getSprite().draw(batch);
        this.getSprite().setColor(Color.WHITE);

        this.getSprite().setAlpha(1);
        this.getSprite().setRotation(0);
    }

    public void update(OrthographicCamera camera) {
        this.updateBody();

        this.applyVelocity(true);

        if(this.hasLight()) {
            this.getLight().setPosition(this.getPosition().x + this.getWidth() / 2, this.getPosition().y + this.getHeight() / 2);
        }

        this.applyActiveKnockback();
    }

    public void knockback(float knockbackAngle, float knockbackDuration) {
        this.applyingKnockback = true;
        this.knockbackAngle = knockbackAngle;
        this.knockbackDuration = knockbackDuration;
    }

    private void applyActiveKnockback() {
        if(this.applyingKnockback) {
            //System.out.println(this.getVelocity().x / 8);
            this.moveAlongRotation(this.knockbackAngle, this.getVelocity().x / 8);

            this.knockbackElapsed += 1 * Gdx.graphics.getDeltaTime();

            if(this.knockbackElapsed >= this.knockbackDuration) {
                this.knockbackElapsed = 0;
                this.knockbackDuration = 0;
                this.applyingKnockback = false;
            }
        }
    }

    public void addLight(Color color, float distance) {
        this.light = new PointLight(this.getParentMap().getRayHandler(), 100, color, distance, this.getPosition().x, this.getPosition().y);
    }

    public void move(Direction direction, Vector2 acceleration, boolean updateDirection) {
        switch(direction) {
            case UP:
                this.modifyVelocity(0, acceleration.y, true);
                break;
            case DOWN:
                this.modifyVelocity(0, -acceleration.y, true);
                break;
            case RIGHT:
                this.modifyVelocity(acceleration.x, 0, true);
                break;
            case LEFT:
                this.modifyVelocity(-acceleration.x, 0, true);
                break;
        }

        if(updateDirection) {
            this.setDirection(direction);
        }
    }

    public void move(Direction direction) {
        this.move(direction, this.getAcceleration(), true);
    }

    public boolean moveDirectly(Vector2 newPosition, boolean stopAtSolids) {
        if(stopAtSolids) {
            boolean canMoveToPosition = false;

            Vector2 resultPosition = new Vector2(this.getPosition());

            if(this.getVelocity().y != 0) {
                if(this.canMoveToY(newPosition.y)) {
                    resultPosition.y = newPosition.y;
                    canMoveToPosition = true;
                } else {
                    Direction collisionDirection = (this.getVelocity().y > 0 ? Direction.UP : Direction.DOWN);

                    this.onCollision(collisionDirection);
                }
            }

            if(this.getVelocity().x != 0) {
                if(this.canMoveToX(newPosition.x)) {
                    resultPosition.set(newPosition.x, resultPosition.y);
                    canMoveToPosition = true;
                } else {
                    Direction collisionDirection = (this.getVelocity().x > 0 ? Direction.RIGHT : Direction.LEFT);

                    this.onCollision(collisionDirection);
                }
            }

            if (canMoveToPosition) {
                this.getPosition().set(resultPosition);
            }

            return canMoveToPosition;
        } else {
            this.getPosition().set(newPosition);
            return true;
        }
    }

    public boolean isMoving() {
        return this.getVelocity().x != 0 || this.getVelocity().y != 0;
    }

    public void applyVelocity(boolean stopAtSolids) {
        float delta = Gdx.graphics.getDeltaTime();

        Vector2 newPosition = new Vector2(this.getPosition());
        newPosition.add(this.getVelocity().x * delta, this.getVelocity().y * delta);

        boolean stoppedAtSolid = !this.moveDirectly(newPosition, stopAtSolids);

        if(stoppedAtSolid) {
            if(this.getVelocity().x != 0) {
                this.getVelocity().set(0, this.getVelocity().y);
            }

            if(this.getVelocity().y != 0) {
                this.getVelocity().set(this.getVelocity().x, 0);
            }
        }
    }

    public boolean canMoveToPosition(float x, float y) {
        return !this.getParentMap().collisionAt(new Rectangle(x, y, this.getSprite().getWidth(), this.getSprite().getHeight()));
    }

    public boolean canMoveToX(float x) {
        return this.canMoveToPosition(x, this.getPosition().y);
    }


    public boolean canMoveToY(float y) {
        return this.canMoveToPosition(this.getPosition().x, y);
    }

    public void updateBody() {
        this.body.set(this.getPosition().x, this.getPosition().y, this.getSprite().getWidth(), this.getSprite().getHeight());
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public Map getParentMap() {
        return this.parentMap;
    }

    public Rectangle getBody() {
        this.updateBody();
        return this.body;
    }

    public Rectangle getBodyNoUpdate() {
        return this.body;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void setSpeed(float x, float y) {
        this.getAcceleration().set(x, y);
        this.getMaxVelocity().set(x * 8, y * 8);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void modifyVelocity(float changeX, float changeY, boolean limitVelocity) {
        float maxX = this.getMaxVelocity().x;
        float maxY = this.getMaxVelocity().y ;

        float resultX = this.getVelocity().x + changeX;
        float resultY = this.getVelocity().y + changeY;

        boolean canApplyX = true;
        boolean canApplyY = true;

        if(limitVelocity) {
            if(Math.abs(resultX) > maxX) {
                canApplyX = false;
            }

            if(Math.abs(resultY) > maxY) {
                canApplyY = false;
            }
        }

        if(canApplyX) {
            this.getVelocity().add(changeX, 0);
        }

        if(canApplyY) {
            this.getVelocity().add(0, changeY);
        }
    }

    public void onCollision(Direction direction) {
        this.colliding = true;
    }

    public void setMaxVelocity(Vector2 maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public Vector2 getMaxVelocity() {
        return this.maxVelocity;
    }

    public float getWidth() {
        return this.getSprite().getWidth();
    }

    public float getHeight() {
        return this.getSprite().getHeight();
    }

    public PointLight getLight() {
        return this.light;
    }

    public boolean hasLight() {
        return this.light != null;
    }

    public double getRotationTowardPosition(Vector2 position) {
        double angle = Math.atan2(position.y - this.getPosition().y, position.x - this.getPosition().x);

        angle = angle * (180 / Math.PI);

        angle -= 90;

        return angle;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public void moveAlongRotation(float rotation) {
        this.moveAlongRotation(rotation, this.getVelocity().x);
    }

    public void moveAlongRotation(float rotation, float speed) {
        speed = speed * 10 * Gdx.graphics.getDeltaTime();

        float xRotationMovement = -speed * (float) Math.cos(Math.toRadians(rotation - 90));
        float yRotationMovement = -speed * (float) Math.sin(Math.toRadians(rotation - 90));

        float delta = Gdx.graphics.getDeltaTime();

        Rectangle newBody = new Rectangle(this.getPosition().x + xRotationMovement, this.getPosition().y + yRotationMovement, this.getWidth(), this.getHeight());

        if(!(this.getParentMap().collisionAt(newBody))) {
            this.getPosition().add(xRotationMovement, 0);
            this.getPosition().add(0, yRotationMovement);

            this.modifyVelocity(this.getAcceleration().x * 5 * delta, this.getAcceleration().y * 5 * delta, true);
        }
    }

    public Vector2 getPositionForRotation(float rotation, float distance) {
        float xRotationMovement = -distance * (float) Math.cos(Math.toRadians(rotation - 90));
        float yRotationMovement = -distance * (float) Math.sin(Math.toRadians(rotation - 90));

        return new Vector2(this.getPosition().x + xRotationMovement, this.getPosition().y + yRotationMovement);
    }

    public void moveAlongCurrentRotation() {
        this.moveAlongRotation(this.getRotation());
    }

}
