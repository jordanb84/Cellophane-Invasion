package com.djam2.game.wave.impl;

import com.badlogic.gdx.math.Vector2;
import com.djam2.game.entity.living.impl.EntityBat;
import com.djam2.game.entity.living.impl.EntityZombie;
import com.djam2.game.map.Map;
import com.djam2.game.wave.Wave;
import com.djam2.game.wave.WaveManager;

public class WaveTwo extends Wave {

    public WaveTwo(Vector2 startPosition, Map map, WaveManager waveManager) {
        super(startPosition, map, waveManager);
    }

    @Override
    public void setupEnemies(Vector2 startPosition, Map map) {
        //float zombieInterval = 3f;
        float zombieInterval = 1f;

        for(int addedZombies = 0; addedZombies < 3; addedZombies++) {
            this.addEnemy(new EntityZombie(new Vector2(startPosition), map), zombieInterval);
        }

        float batInterval = 5f;
        for(int addedBats = 0; addedBats < 3; addedBats++) {
            this.addEnemy(new EntityBat(new Vector2(startPosition), map), batInterval);
        }
    }

}
