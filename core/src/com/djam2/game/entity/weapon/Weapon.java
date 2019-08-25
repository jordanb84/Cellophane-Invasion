package com.djam2.game.entity.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.map.Map;

public abstract class Weapon {

    private float fireInterval;
    private float elapsedSinceFire;

    private boolean charged;

    private float chargeRate;

    private float chargePercentage;

    private boolean requiresCharge = true;

    public Weapon(float fireInterval, float chargeRate) {
        this.fireInterval = fireInterval;
        this.chargeRate = chargeRate;
        this.setCharged(true);
    }

    public void update() {
        this.elapsedSinceFire += 1 * Gdx.graphics.getDeltaTime();

        if(!this.requiresCharge) {
            this.setCharged(true);
        }
    }

    public void attemptFire(Vector2 position, Vector2 destination, Map parentMap, boolean canFire) {
        if(canFire) {
            if (this.elapsedSinceFire >= this.fireInterval && this.isCharged()) {
                this.elapsedSinceFire = 0;
                this.fire(position, destination, parentMap);

                this.setCharged(false);
            }
        }
    }

    public abstract void fire(Vector2 position, Vector2 destination, Map parentMap);

    public boolean isCharged() {
        return this.charged;
    }

    public void setCharged(boolean charged) {
        this.charged = charged;
    }

    public float getChargeRate() {
        return this.chargeRate;
    }

    public float getChargePercentage() {
        return this.chargePercentage;
    }

    public void setChargePercentage(float chargePercentage) {
        this.chargePercentage = chargePercentage;
    }

    public void incrementCharge() {
        this.chargePercentage += this.getChargeRate();
    }

    public void setRequiresCharge(boolean requiresCharge) {
        this.requiresCharge = requiresCharge;
    }

    public boolean requiresCharge() {
        return this.requiresCharge;
    }

}
