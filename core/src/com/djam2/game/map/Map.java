package com.djam2.game.map;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.djam2.game.entity.Entity;
import com.djam2.game.entity.living.impl.EntityBat;
import com.djam2.game.entity.living.impl.EntityPlayer;
import com.djam2.game.tile.TileType;
import com.djam2.game.tile.pathfinding.ManhattanHeuristic;
import com.djam2.game.tile.pathfinding.TileGraph;
import com.djam2.game.tile.pathfinding.TileNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {

    private MapDefinition mapDefinition;

    private List<MapLayer> mapLayers;

    private List<Entity> entities = new ArrayList<Entity>();
    private List<Entity> entitySpawnQueue = new ArrayList<Entity>();
    private List<Entity> entityDespawnQueue = new ArrayList<Entity>();

    private float worldFriction = 140;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private RayHandler rayHandler;

    private ShapeRenderer shapeRenderer;

    private boolean renderDebugBodies = false;

    private IndexedAStarPathFinder pathFinder;
    private ManhattanHeuristic heuristic = new ManhattanHeuristic();

    //test path, will move to per-entity later
    private DefaultGraphPath<TileNode> graphPath = new DefaultGraphPath<TileNode>();

    private boolean drawPath;
    private Array<TileNode> pathTiles;

    private EntityPlayer player;

    public Map(MapDefinition mapDefinition, List<MapLayer> mapLayers) {
        this.mapDefinition = mapDefinition;
        this.mapLayers = mapLayers;

        this.setup();
    }

    public Map(MapDefinition mapDefinition, TileType groundType) {
        this.mapDefinition = mapDefinition;
        this.mapLayers = new ArrayList<MapLayer>();

        for(int layersGenerated = 0; layersGenerated < this.mapDefinition.getMapLayers(); layersGenerated++) {
            if(layersGenerated == 0) {
                this.mapLayers.add(new MapLayer(this.mapDefinition, groundType));
            } else {
                this.mapLayers.add(new MapLayer(this.mapDefinition, TileType.Air));
            }
        }

        this.setup();
    }

    private void setup() {
        this.setupPhysicsWorld();

        this.rayHandler = new RayHandler(this.world);
        this.rayHandler.setAmbientLight(Color.LIGHT_GRAY);
        RayHandler.useDiffuseLight(true);

        this.setupPathfindingMap();

        this.generatePath(TileType.Start, TileType.End, this.graphPath);

        this.spawnInitialEntities();

        this.shapeRenderer = new ShapeRenderer();
        this.shapeRenderer.setAutoShapeType(true);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        for(MapLayer mapLayer : this.mapLayers) {
            mapLayer.render(this, batch, camera);
        }

        for(Entity entity : this.getEntities()) {
            entity.render(batch, camera);
        }

        if(this.renderDebugBodies) {
            batch.end();
            this.shapeRenderer.setProjectionMatrix(camera.combined);
            this.shapeRenderer.begin();
            for(Entity entity : this.getEntities()) {
                this.shapeRenderer.rect(entity.getBody().x, entity.getBody().y, entity.getBody().getWidth(), entity.getBody().getHeight());
            }

            if(this.drawPath) {
                for(TileNode tileNode : this.pathTiles) {
                    this.shapeRenderer.setColor(Color.GREEN);
                    this.shapeRenderer.rect(tileNode.getPosition().x, tileNode.getPosition().y, 16, 16);
                    this.shapeRenderer.setColor(Color.WHITE);
                }
            }

            this.shapeRenderer.end();
            batch.begin();
        }

        batch.end();
        //this.debugRenderer.render(this.getPhysicsWorld(), camera.combined);
        this.rayHandler.setCombinedMatrix(camera);
        this.rayHandler.updateAndRender();
        batch.begin();
    }

    public void update(OrthographicCamera camera) {
        this.world.step(1/60f, 6, 2);

        for(MapLayer mapLayer : this.mapLayers) {
            mapLayer.update(this, camera);
        }

        this.entities.addAll(this.entitySpawnQueue);
        this.entitySpawnQueue.clear();

        this.entities.removeAll(this.entityDespawnQueue);
        this.entityDespawnQueue.clear();

        for(Entity entity : this.getEntities()) {
            entity.update(camera);
            this.applyEntityFriction(entity);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            this.renderDebugBodies = !this.renderDebugBodies;
        }

    }

    private TileNode[][] collisionLayerNodes;

    private Array<TileNode> allNodes;

    private TileGraph tileGraph;

    private void setupPathfindingMap() {
        this.collisionLayerNodes = new TileNode[this.mapDefinition.getMapHeight()][this.mapDefinition.getMapWidth()];

        Array<TileNode> allNodes = new Array<TileNode>();

        List<TileType> baseNodes = this.getMapLayer(1).getLayerTiles();

        int tileIndex = 0;

        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int column = 0; column < this.mapDefinition.getMapWidth(); column++) {
                TileType tileType = baseNodes.get(tileIndex);

                Vector2 tilePosition = new Vector2(column * this.mapDefinition.getTileWidth(), row * this.mapDefinition.getTileHeight());

                TileNode tileNode = new TileNode(tileIndex, tilePosition, tileType, 1);

                allNodes.add(tileNode);

                this.collisionLayerNodes[row][column] = tileNode;

                tileIndex++;
            }
        }

        this.allNodes = allNodes;

        this.tileGraph = this.generateUpdatedTileGraph();

        int totalCheckedAdjacentTiles = 0;

        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int column = 0; column < this.mapDefinition.getMapWidth(); column++) {
                TileNode node = this.collisionLayerNodes[row][column];

                if(node != null) {
                    this.addAdjacentNode(node, column, row - 1);
                    this.addAdjacentNode(node, column, row + 1);
                    this.addAdjacentNode(node, column - 1, row);
                    this.addAdjacentNode(node, column + 1, row);
                }

                totalCheckedAdjacentTiles++;
            }
        }

        this.pathFinder = new IndexedAStarPathFinder(this.tileGraph, true);
    }

    public TileGraph generateUpdatedTileGraph() {
        return new TileGraph(this.allNodes);
    }

    public void addAdjacentNode(TileNode node, int x, int y) {
        if(x >= 0 && x < this.mapDefinition.getMapWidth() && y >= 0 && y < this.mapDefinition.getMapHeight()) {
            node.addAdjacentTile(this.collisionLayerNodes[y][x]);
        }
    }

    public void generatePath(TileType startType, TileType endType, DefaultGraphPath<TileNode> graphPath) {
        int startNodeIndex = 0;
        int endNodeIndex = 0;

        for(TileNode tileNode : this.tileGraph.getNodes()) {
            if(tileNode.getTileType() == startType) {
                startNodeIndex = tileNode.getTileMapIndex();
            }

            if(tileNode.getTileType() == endType) {
                endNodeIndex = tileNode.getTileMapIndex();
            }

        }

        graphPath.clear();

        this.pathFinder.searchNodePath(this.allNodes.get(startNodeIndex), this.allNodes.get(endNodeIndex), heuristic, graphPath);

        boolean hasPath = this.graphPath.nodes.size > 0;

        if(hasPath) {
            this.drawPath = true;
            this.pathTiles = this.graphPath.nodes;
        }
    }

    private void spawnInitialEntities() {
        Vector2 startPosition = this.getPositionOfFirstTile(TileType.Start, 1);

        this.spawnEntity(new EntityPlayer(new Vector2(startPosition.x + 64, startPosition.y + 64), this));

        this.spawnEntity(new EntityBat(startPosition, this));

        Random batPositionRandom = new Random();

        for(int bats = 0; bats < 15; bats++) {
            //this.spawnEntity(new EntityBat(new Vector2(batPositionRandom.nextInt(200), batPositionRandom.nextInt(200)), this));
        }
    }

    public Vector2 getPositionOfFirstTile(TileType tileType, int layer) {
        MapLayer mapLayer = this.getMapLayer(layer);

        for(int row = 0; row < this.getMapDefinition().getMapHeight(); row++) {
            for(int column = 0; column < this.getMapDefinition().getMapWidth(); column++) {
                int index = row * this.getMapDefinition().getMapWidth() + column;

                if(mapLayer.getLayerTiles().get(index) == tileType) {
                    return new Vector2(column * this.getMapDefinition().getTileWidth(), row * this.getMapDefinition().getTileHeight());
                }
            }
        }

        return new Vector2(128, 128);
    }

    private void setupPhysicsWorld() {
        this.world = new World(new Vector2(0, -2), true);

        this.debugRenderer = new Box2DDebugRenderer();
    }

    public void spawnEntity(Entity entity) {
        this.entitySpawnQueue.add(entity);

        if(entity instanceof EntityPlayer) {
            this.player = ((EntityPlayer) entity);
        }
    }

    public void despawnEntity(Entity entity) {
        this.entityDespawnQueue.add(entity);
    }

    public List<Entity> getEntities() {
        return this.entities;
    }

    public boolean collisionAt(Rectangle body) {
        for(MapLayer mapLayer : this.mapLayers) {
            if(mapLayer.collisionAt(body)) {
                return true;
            }
        }

        return false;
    }

    public void applyEntityFriction(Entity entity) {
        float relativeFriction = this.worldFriction;

        float delta = Gdx.graphics.getDeltaTime();

        //System.out.println("Friction: " + this.worldFriction * delta);

        if(relativeFriction > entity.getVelocity().x) {
            //relativeFriction = entity.getVelocity().x;
        }

        if(entity.getVelocity().x > 0) {
            entity.getVelocity().x -= relativeFriction * delta;
        }

        if(entity.getVelocity().x < 0) {
            entity.getVelocity().x += relativeFriction * delta;
        }

        if(entity.getVelocity().y > 0) {
            entity.getVelocity().y -= this.worldFriction * delta;
        }

        if(entity.getVelocity().y < 0) {
            entity.getVelocity().y += this.worldFriction * delta;
        }

        if(Math.abs(entity.getVelocity().x) < 1) {
            //entity.getVelocity().set(0, entity.getVelocity().y);
        }

        if(Math.abs(entity.getVelocity().y) < 1) {
            //entity.getVelocity().set(entity.getVelocity().x, 0);
        }
    }

    public MapDefinition getMapDefinition() {
        return this.mapDefinition;
    }

    public MapLayer getMapLayer(int index) {
        return this.mapLayers.get(index);
    }

    public List<MapLayer> getMapLayers() {
        return this.mapLayers;
    }

    public World getPhysicsWorld() {
        return this.world;
    }

    public RayHandler getRayHandler() {
        return this.rayHandler;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }

}