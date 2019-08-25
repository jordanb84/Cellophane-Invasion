package com.djam2.game.wave.impl;

import com.badlogic.gdx.math.Vector2;
import com.djam2.game.entity.living.impl.EntityZombie;
import com.djam2.game.map.Map;
import com.djam2.game.wave.Wave;
import com.djam2.game.wave.WaveManager;

public class WaveOne extends Wave {

    public WaveOne(Vector2 startPosition, Map map, WaveManager waveManager) {
        super(startPosition, map, waveManager);
    }

    @Override
    public void setupEnemies(Vector2 startPosition, Map map) {
        float zombieInterval = 3f;

        for(int addedZombies = 0; addedZombies < 5; addedZombies++) {
            this.addEnemy(new EntityZombie(new Vector2(startPosition), map), zombieInterval);
        }
    }

}
