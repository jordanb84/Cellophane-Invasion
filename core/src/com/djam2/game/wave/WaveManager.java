package com.djam2.game.wave;

import com.badlogic.gdx.math.Vector2;
import com.djam2.game.entity.living.impl.EntityPlayer;
import com.djam2.game.map.Map;
import com.djam2.game.wave.impl.WaveFour;
import com.djam2.game.wave.impl.WaveOne;
import com.djam2.game.wave.impl.WaveThree;
import com.djam2.game.wave.impl.WaveTwo;

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
        this.waves.add(new WaveTwo(startPosition, map, this));
        this.waves.add(new WaveThree(startPosition, map, this));
        this.waves.add(new WaveFour(startPosition, map, this));
    }

    public void update() {
        if(!this.finished) {
            this.waves.get(this.waveIndex).update(this.map);
        }

        //System.out.println("WAVE " + this.waveIndex);
    }

    public void nextWave() {
        if(this.waveIndex < this.waves.size() - 1) {
            this.waveIndex++;

            EntityPlayer player = this.map.getPlayer();

            if(player.getHealth() < player.getMaxHealth()) {
                //player.setHealth(player.getHealth() + (player.getHealth() / 5));
                player.setHealth(player.getHealth() + 10);

                if(player.getHealth() > player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
            }
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

    public boolean isFinished() {
        return this.finished;
    }

}
