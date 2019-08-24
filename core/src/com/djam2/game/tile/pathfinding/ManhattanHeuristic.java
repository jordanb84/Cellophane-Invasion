package com.djam2.game.tile.pathfinding;


import com.badlogic.gdx.ai.pfa.Heuristic;

public class ManhattanHeuristic implements Heuristic<TileNode> {

    @Override
    public float estimate(TileNode startNode, TileNode endNode) {
        return Math.abs(endNode.getPosition().x - startNode.getPosition().x) + Math.abs(endNode.getPosition().y - startNode.getPosition().y);
    }

}