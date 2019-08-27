package com.djam2.game.map;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.djam2.game.assets.Assets;
import com.djam2.game.entity.Entity;
import com.djam2.game.entity.living.EntityEnemy;
import com.djam2.game.entity.living.impl.EntityBat;
import com.djam2.game.entity.living.impl.EntityPlayer;
import com.djam2.game.entity.living.impl.EntityZombie;
import com.djam2.game.state.StateManager;
import com.djam2.game.tile.TileType;
import com.djam2.game.tile.pathfinding.ManhattanHeuristic;
import com.djam2.game.tile.pathfinding.TileGraph;
import com.djam2.game.tile.pathfinding.TileNode;
import com.djam2.game.wave.WaveManager;

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

    private Vector2 startPosition;
    private Vector2 endPosition;

    private StateManager stateManager;

    private WaveManager waveManager;

    private boolean won;

    private Sprite arrowSprite;

    private boolean lost;
    private float elaspedSinceLost;
    private boolean shownLostMessage;

    private float elapsedSinceWon;

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

    private TextureRegion tileMap;

    private void setup() {
        this.setupPhysicsWorld();

        this.rayHandler = new RayHandler(this.world);
        this.rayHandler.setAmbientLight(Color.LIGHT_GRAY);
        RayHandler.useDiffuseLight(true);
        this.rayHandler.setCulling(true);

        this.setupPathfindingMap();

        this.generatePath(TileType.Start, TileType.Govern0, this.graphPath);

        this.spawnInitialEntities();

        this.shapeRenderer = new ShapeRenderer();
        this.shapeRenderer.setAutoShapeType(true);

        this.waveManager = new WaveManager(new Vector2(this.startPosition), this);

        this.arrowSprite = Assets.getInstance().getSprite("arrow.png");

        this.regenerateTilemapFrameBuffer();
    }

    private void regenerateTilemapFrameBuffer() {
        int pixelsWidth = this.mapDefinition.getMapWidth() * this.mapDefinition.getTileWidth();
        int pixelsHeight = this.mapDefinition.getMapHeight() * this.mapDefinition.getTileHeight();

        FrameBuffer frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, pixelsWidth, pixelsHeight, false);

        SpriteBatch spriteBatch = new SpriteBatch();

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, pixelsWidth, pixelsHeight);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        frameBuffer.begin();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        for(MapLayer mapLayer : this.mapLayers) {
            mapLayer.render(this, spriteBatch, camera);
        }

        spriteBatch.end();
        frameBuffer.end();

        frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        this.tileMap = new TextureRegion(frameBuffer.getColorBufferTexture());
        this.tileMap.flip(false, true);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.draw(this.tileMap, 0, 0);

        for(Entity entity : this.getEntities()) {
            if(!(entity instanceof EntityPlayer)) {
                entity.render(batch, camera);
            }
        }

        if(this.getPlayer() != null) {
            this.getPlayer().render(batch, camera);
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

            for(MapLayer mapLayer : this.mapLayers) {
                mapLayer.renderDebugBodies(this.shapeRenderer);
            }

            this.shapeRenderer.setColor(Color.GREEN);
            this.shapeRenderer.rect(camera.position.x - camera.viewportWidth, camera.position.y - camera.viewportHeight, camera.viewportWidth / 2, camera.viewportHeight / 2);

            this.shapeRenderer.end();
            batch.begin();
        }

        batch.end();
        //this.debugRenderer.render(this.getPhysicsWorld(), camera.combined);
        this.rayHandler.setCombinedMatrix(camera);
        this.rayHandler.updateAndRender();
        batch.begin();

        if(this.getPlayer() != null) {
            this.getPlayer().getWeaponBar().render(batch);
        }

        boolean drawArrow = true;

        try {
            //System.out.println("Within sight: " + this.enemyWithinSight(camera));
            if (!this.enemyWithinSight(camera) || drawArrow) {
                EntityEnemy nearestEnemy = this.getNearestEnemy();

                if(nearestEnemy.getPosition().dst(this.getPlayer().getPosition()) > 200) {
                    //System.out.println("No enemies within sight!");

                    this.arrowSprite.setAlpha(0.6f);
                    // Vector2 spritePosition = new Vector2(camera.position.x - camera.viewportWidth / 2, camera.position.y);
                    Vector2 spritePosition = new Vector2(player.getPosition().x - this.getPlayer().getWidth() / 2, player.getPosition().y - this.arrowSprite.getHeight() / 2 + 100);

                    //System.out.println("Drawing at " + spritePosition.x + "/" + spritePosition.y);

                    double rotation = this.getRotationTowardPosition(nearestEnemy.getPosition(), this.getPlayer().getPosition());

                    this.arrowSprite.setRotation((float) rotation);
                    this.arrowSprite.setPosition(spritePosition.x, spritePosition.y);
                    this.arrowSprite.draw(batch);
                }
            }
        } catch(IndexOutOfBoundsException noEnemies) {

        }
    }

    public void update(OrthographicCamera camera) {
        if(!this.lost) {
            this.world.step(1 / 60f, 6, 2);

            for (MapLayer mapLayer : this.mapLayers) {
                mapLayer.update(this, camera);
            }

            this.entities.addAll(this.entitySpawnQueue);
            this.entitySpawnQueue.clear();

            this.entities.removeAll(this.entityDespawnQueue);
            this.entityDespawnQueue.clear();

            for (Entity entity : this.getEntities()) {
                entity.update(camera);
                this.applyEntityFriction(entity);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                //this.renderDebugBodies = !this.renderDebugBodies;
            }

            this.waveManager.update();

            if (this.waveManager.isFinished() && this.getEnemyCount() == 0 && !this.won) {
                this.win();
            }
        }

        if(this.lost) {
            this.elaspedSinceLost += 1 * Gdx.graphics.getDeltaTime();

            if(this.elaspedSinceLost > 6) {
                this.stateManager.getActiveState().reset();
            }
        }

        if(this.getPlayer() != null) {
            if(this.getPlayer().getHealth() <= 0) {
                this.lose();
            }
        }

        if(this.won) {
            //System.out.println("Elapsed " + this.elapsedSinceWon);
            this.elapsedSinceWon += 1 * Gdx.graphics.getDeltaTime();

            if(this.elapsedSinceWon >= 5) {
                System.out.println("RESET");
                this.stateManager.getActiveState().reset();
            }
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

    public float getLowestDistanceFromBase() {
        float lowestDistance = this.startPosition.dst(this.endPosition);

        for(Entity entity : this.getEntities()) {
            if(entity instanceof EntityEnemy) {
                float distance = this.endPosition.dst(entity.getPosition());

                if(distance < lowestDistance) {
                    lowestDistance = distance;
                }
            }
        }

        return lowestDistance;
    }

    private void spawnInitialEntities() {
        this.startPosition = this.getPositionOfFirstTile(TileType.Start, 1);
        this.endPosition = this.getPositionOfFirstTile(TileType.Govern0, 1);

        Vector2 playerStartPosition = new Vector2(this.getPositionOfFirstTile(TileType.Govern0, 1));

        this.spawnEntity(new EntityPlayer(new Vector2(playerStartPosition.x - 64, playerStartPosition.y + 48), this));

        /**this.spawnEntity(new EntityBat(new Vector2(startPosition), this));
        this.spawnEntity(new EntityZombie(new Vector2(startPosition), this));

        Random batPositionRandom = new Random();

        for(int bats = 0; bats < 15; bats++) {
            //this.spawnEntity(new EntityBat(new Vector2(batPositionRandom.nextInt(200), batPositionRandom.nextInt(200)), this));
        }**/
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

    public void resize(int width, int height) {
        this.player.resize(width, height);
    }

    public int getEnemyCount() {
        int enemyCount = 0;

        for(Entity entity : this.getEntities()) {
            if(entity instanceof EntityEnemy) {
                enemyCount++;
            }
        }

        return enemyCount;
    }

    public int getWave() {
        return this.waveManager.getWave();
    }

    public int getTotalWaves() {
        return this.waveManager.getTotalWaves();
    }

    public void win() {
        this.won = true;
        this.getPlayer().getWeaponBar().displayWinMessage();
    }

    public void lose() {
        this.lost = true;
        this.getPlayer().getWeaponBar().displayLostMessage();
        this.shownLostMessage = true;
    }

    public boolean enemyWithinSight(OrthographicCamera camera) {
        Rectangle cameraBody = new Rectangle(camera.position.x - camera.viewportWidth, camera.position.y - camera.viewportHeight, camera.viewportWidth / 2, camera.viewportHeight / 2);

        for(Entity entity : this.getEntities()) {
            if(entity instanceof EntityEnemy) {
                if(cameraBody.overlaps(entity.getBody())) {
                    return true;
                }
            }
        }

        return false;
    }

    public EntityEnemy getNearestEnemy(Vector2 from) {
        List<EntityEnemy> enemies = this.getEnemies();

        EntityEnemy closestEnemy = enemies.get(0);

        for(EntityEnemy enemy : enemies) {
            if(enemy.getPosition().dst(from) < closestEnemy.getPosition().dst(from)) {
                closestEnemy = enemy;
            }
        }

        return closestEnemy;
    }

    public EntityEnemy getNearestEnemy() {
        Vector2 playerPosition = this.getPlayer().getPosition();

        return this.getNearestEnemy(playerPosition);
    }

    public List<EntityEnemy> getEnemies() {
        List<EntityEnemy> enemies = new ArrayList<>();

        for(Entity entity : this.getEntities()) {
            if(entity instanceof EntityEnemy) {
                enemies.add(((EntityEnemy) entity));
            }
        }

        return enemies;
    }

    public double getRotationTowardPosition(Vector2 position, Vector2 sourcePosition) {
        double angle = Math.atan2(position.y - sourcePosition.y, position.x - sourcePosition.x);

        angle = angle * (180 / Math.PI);

        angle -= 90;

        return angle;
    }

    public void setStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

}