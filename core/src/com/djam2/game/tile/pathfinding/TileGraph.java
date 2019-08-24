package com.djam2.game.tile.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;

public class TileGraph implements IndexedGraph<TileNode> {

    private Array<TileNode> nodes;

    public TileGraph(Array<TileNode> nodes) {
        this.nodes = nodes;
    }

    public Array<TileNode> getNodes() {
        return nodes;
    }

    @Override
    public int getIndex(TileNode node) {
        return node.getTileMapIndex();
    }

    @Override
    public int getNodeCount() {
        return this.nodes.size;
    }

    @Override
    public Array<Connection<TileNode>> getConnections(TileNode fromNode) {
        return fromNode.getConnections();
    }
}