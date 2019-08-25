package com.djam2.game.wave;

import com.djam2.game.entity.living.EntityEnemy;

public class WaveSpawner {

    private EntityEnemy enemy;

    private float timeToSpawn;

    public WaveSpawner(EntityEnemy enemy, float timeToSpawn) {
        this.enemy = enemy;
        this.timeToSpawn = timeToSpawn;
    }

    public EntityEnemy getEnemy() {
        return this.enemy;
    }

    public float getTimeToSpawn() {
        return this.timeToSpawn;
    }

}
