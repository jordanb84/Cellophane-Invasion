package com.djam2.game.tile.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.djam2.game.tile.TileType;

public class TileNode {

    private int tileMapIndex;

    private Array<Connection<TileNode>> connections = new Array<Connection<TileNode>>();

    private Vector2 position;

    private TileType tileType;

    private int layer;

    public TileNode(int tileMapIndex, Vector2 position, TileType tileType, int layer) {
        this.tileMapIndex = tileMapIndex;
        this.position = position;
        this.tileType = tileType;
        this.layer = layer;
    }

    public void addAdjacentTile(TileNode node) {
        if(node != null) {
            if(node.isSolid() || node.tileType == TileType.PathBlock) {
                this.connections.add(new SolidConnection<TileNode>(this, node));
            } else {
                this.connections.add(new DefaultConnection<TileNode>(this, node));
            }
        }
    }

    public Array<Connection<TileNode>> getConnections() {
        return connections;
    }

    public boolean isSolid() {
        return this.tileType.SOLID;
    }

    public int getTileMapIndex() {
        return this.tileMapIndex;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public TileType getTileType() {
        return this.tileType;
    }

    public int getLayer() {
        return this.layer;
    }

}