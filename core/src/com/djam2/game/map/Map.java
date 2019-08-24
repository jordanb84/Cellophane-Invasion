package com.djam2.game.map;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.djam2.game.entity.Entity;
import com.djam2.game.entity.living.impl.EntityPlayer;
import com.djam2.game.tile.TileType;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private MapDefinition mapDefinition;

    private List<MapLayer> mapLayers;

    private List<Entity> entities = new ArrayList<Entity>();
    private List<Entity> entitySpawnQueue = new ArrayList<Entity>();
    private List<Entity> entityDespawnQueue = new ArrayList<Entity>();

    private float worldFriction = 2;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private RayHandler rayHandler;

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

        this.spawnInitialEntities();
    }

    private void spawnInitialEntities() {
        this.spawnEntity(new EntityPlayer(new Vector2(128, 128), this));
    }

    private void setupPhysicsWorld() {
        this.world = new World(new Vector2(0, -2), true);

        this.debugRenderer = new Box2DDebugRenderer();
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        for(MapLayer mapLayer : this.mapLayers) {
            mapLayer.render(this, batch, camera);
        }

        for(Entity entity : this.getEntities()) {
            entity.render(batch, camera);
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
    }

    public void spawnEntity(Entity entity) {
        this.entitySpawnQueue.add(entity);
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
        if(entity.getVelocity().x > 0) {
            entity.getVelocity().x -= this.worldFriction;
        }

        if(entity.getVelocity().x < 0) {
            entity.getVelocity().x += this.worldFriction;
        }

        if(entity.getVelocity().y > 0) {
            entity.getVelocity().y -= this.worldFriction;
        }

        if(entity.getVelocity().y < 0) {
            entity.getVelocity().y += this.worldFriction;
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

}