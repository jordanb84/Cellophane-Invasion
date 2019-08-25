package com.djam2.game.wave;

import com.badlogic.gdx.math.Vector2;
import com.djam2.game.map.Map;
import com.djam2.game.wave.impl.WaveOne;

import java.util.ArrayList;
import java.util.List;

public class WaveManager {

    private List<Wave> waves = new ArrayList<>();

    private int waveIndex;

    private boolean finished;

    private Map map;

    public WaveManager(Vector2 startPosition, Map map) {
        this.map = map;
        this.setupWaves(startPosition, map);
    }

    private void setupWaves(Vector2 startPosition, Map map) {
        this.waves.add(new WaveOne(startPosition, map, this));
    }

    public void update() {
        if(!this.finished) {
            for (Wave wave : this.waves) {
                wave.update(this.map);
            }
        }

        //System.out.println("WAVE " + this.waveIndex);
    }

    public void nextWave() {
        if(this.waveIndex < this.waves.size()) {
            this.waveIndex++;
        } else {
            this.finish();
        }
    }

    private void finish() {
        this.finished = true; //TODO game wins at this point if theres also 0 enemies left
    }

    public int getWave() {
        return this.waveIndex;
    }

    public int getTotalWaves() {
        return this.waves.size();
    }

}
