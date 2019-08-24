package com.djam2.game.entity.living;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.entity.Direction;
import com.djam2.game.entity.EntityType;
import com.djam2.game.map.Map;
import com.djam2.game.map.MapDefinition;
import com.djam2.game.tile.TileType;
import com.djam2.game.tile.pathfinding.TileGraph;
import com.djam2.game.tile.pathfinding.TileNode;

public abstract class EntityEnemy extends LivingEntity {

    private Sprite hurtSprite;

    private DefaultGraphPath<TileNode> graphPath = new DefaultGraphPath<TileNode>();

    private TileNode finalNode;
    private Rectangle finalBody;

    private int destinationTileIndex = 0;
    private Rectangle destinationBody = new Rectangle();

    private TileGraph tileGraph;

    public EntityEnemy(Vector2 position, Map parentMap, float weight) {
        super(position, parentMap, weight, EntityType.ENEMY);
        this.hurtSprite = this.getHurtSprite();

        this.tileGraph = parentMap.generateUpdatedTileGraph();

        parentMap.generatePath(TileType.Start, TileType.End, this.graphPath);

        MapDefinition mapDefinition = parentMap.getMapDefinition();

        this.finalNode = this.graphPath.nodes.get(this.graphPath.nodes.size - 1);
        this.finalBody = new Rectangle(this.finalNode.getPosition().x, this.finalNode.getPosition().y, mapDefinition.getTileWidth(), mapDefinition.getTileHeight());

        this.updateDestinationBody(mapDefinition);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.moveTowardDestinationNode();
    }

    public void moveTowardDestinationNode() {
        float x = this.getPosition().x;
        float y = this.getPosition().y;

        TileNode destinationNode = this.graphPath.get(this.destinationTileIndex);

        float destinationX = destinationNode.getPosition().x;
        float destinationY = destinationNode.getPosition().y;

        if(x > destinationX) {
            this.move(Direction.LEFT);
        }

        if(x < destinationX) {
            this.move(Direction.RIGHT);
        }

        if(y > destinationY) {
            this.move(Direction.DOWN);
        }

        if(y < destinationY) {
            this.move(Direction.UP);
        }

        if(this.getBody().overlaps(this.destinationBody)) {
            this.destinationTileIndex++;

            if(this.destinationTileIndex >= this.graphPath.nodes.size) {
                this.die();
            } else {
                this.updateDestinationBody(this.getParentMap().getMapDefinition());
            }
        }
    }

    public void updateDestinationBody(MapDefinition mapDefinition) {
        this.destinationBody.set(this.getDestinationNode().getPosition().x, this.getDestinationNode().getPosition().y, mapDefinition.getTileWidth(), mapDefinition.getTileHeight());
    }

    public TileNode getDestinationNode() {
        return this.graphPath.get(this.destinationTileIndex);
    }

    public abstract Sprite getHurtSprite();

    @Override
    public Sprite getSprite() {
        if(this.isDamaged()) {
            return this.hurtSprite;
        } else {
            return super.getSprite();
        }
    }

}
