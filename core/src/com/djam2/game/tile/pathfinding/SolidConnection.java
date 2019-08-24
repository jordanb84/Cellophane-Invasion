package com.djam2.game.tile.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;

public class SolidConnection<N> implements Connection<N> {

    protected N fromNode;
    protected N toNode;

    public SolidConnection(N fromNode, N toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    @Override
    public float getCost() {
        return 100;
    }

    @Override
    public N getFromNode() {
        return fromNode;
    }

    @Override
    public N getToNode() {
        return toNode;
    }
}