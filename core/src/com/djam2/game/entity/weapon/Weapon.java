package com.djam2.game.entity.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.map.Map;

public abstract class Weapon {

    private float fireInterval;
    private float elapsedSinceFire;

    public Weapon(float fireInterval) {
        this.fireInterval = fireInterval;
    }

    public void update() {
        this.elapsedSinceFire += 1 * Gdx.graphics.getDeltaTime();
    }

    public void attemptFire(Vector2 position, Vector2 destination, Map parentMap) {
        if(this.elapsedSinceFire >= this.fireInterval) {
            this.elapsedSinceFire = 0;
            this.fire(position, destination, parentMap);
        }
    }

    public abstract void fire(Vector2 position, Vector2 destination, Map parentMap);

}
