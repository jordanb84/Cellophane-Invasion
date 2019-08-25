package com.djam2.game.wave;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.entity.living.EntityEnemy;
import com.djam2.game.map.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Wave {

    private float elapsedSinceLastEnemy;

    private List<WaveSpawner> enemies = new ArrayList<>();

    private int enemyIndex;

    private WaveManager waveManager;

    public Wave(Vector2 startPosition, Map map, WaveManager waveManager) {
        this.setupEnemies(startPosition, map);
        this.waveManager = waveManager;
    }

    public abstract void setupEnemies(Vector2 startPosition, Map map);

    public void update(Map map) {
        this.elapsedSinceLastEnemy += 1 * Gdx.graphics.getDeltaTime();

        boolean done = false;

        if(this.enemyIndex >= this.enemies.size()) {
            this.waveManager.nextWave();
            done = true;
        }

        if(!done) {
            if (this.elapsedSinceLastEnemy >= this.getTimeForNextEnemy()) {
                EntityEnemy enemy = this.getNextEnemy();

                map.spawnEntity(enemy);

                this.enemyIndex++;
                
                this.elapsedSinceLastEnemy = 0;
            }
        }
    }

    public void addEnemy(EntityEnemy enemy, float timeToSpawn) {
        this.enemies.add(new WaveSpawner(enemy, timeToSpawn));
    }

    private float getTimeForNextEnemy() {
        return this.enemies.get(this.enemyIndex).getTimeToSpawn();
    }

    private EntityEnemy getNextEnemy() {
        return this.enemies.get(this.enemyIndex).getEnemy();
    }

}
