package com.djam2.game.tile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class TileData {

    private Body physicsBody;

    public void updatePhysicsBody(World physicsWorld, Vector2 position, TileType tileType) {
        if(this.physicsBody == null && tileType.SOLID) {
            this.setupPhysicsBody(physicsWorld, position);
        }
    }

    private void setupPhysicsBody(World physicsWorld, Vector2 position) {
        float width = TileType.Air.SPRITE.getWidth();
        float height = TileType.Air.SPRITE.getHeight();

        BodyDef physicsBodyDefinition = new BodyDef();
        physicsBodyDefinition.position.set(new Vector2(position.x + width / 2, position.y + height / 2));

        this.physicsBody = physicsWorld.createBody(physicsBodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        this.physicsBody.createFixture(shape, 0.0f);

        shape.dispose();
    }

    public Body getPhysicsBody() {
        return this.physicsBody;
    }
}
