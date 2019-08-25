package com.djam2.game.entity.mind.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.entity.Direction;
import com.djam2.game.entity.Entity;
import com.djam2.game.entity.EntityType;
import com.djam2.game.entity.impl.EntityBullet;
import com.djam2.game.entity.living.LivingEntity;
import com.djam2.game.entity.living.impl.EntityPlayer;
import com.djam2.game.entity.mind.EntityMind;
import com.djam2.game.entity.mind.EntityMindState;

import java.util.Random;

public class InvasionApproachMindState extends EntityMindState {

    private float minimumFireInterval = 0.4f;
    private float fireInterval = minimumFireInterval;
    private float elapsedSinceFire;

    private Random fireRandom = new Random();

    private float fireRange = 100;

    public InvasionApproachMindState(EntityMind parentMind) {
        super(parentMind, "approach");
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera, LivingEntity parentEntity) {

    }

    @Override
    public void update(OrthographicCamera camera, LivingEntity parentEntity) {
        if(this.elapsedSinceFire >= this.fireInterval) {
            EntityPlayer player = parentEntity.getParentMap().getPlayer();

            float distance = parentEntity.getPosition().dst(player.getPosition());

            if(distance <= this.fireRange) {
                this.fire(player);
            }
        }

        this.elapsedSinceFire += 1 * Gdx.graphics.getDeltaTime();
    }

    private void fire(EntityPlayer player) {
        this.elapsedSinceFire = 0;

        Vector2 firePosition = new Vector2(this.getParentEntity().getPosition());
        Vector2 fireTarget = new Vector2(player.getPosition());

        EntityBullet bullet = new EntityBullet(firePosition, fireTarget, this.getParentMap(), 5, EntityType.PLAYER, 3f, 0.5f);

        this.getParentEntity().getParentMap().spawnEntity(bullet);

        this.fireInterval = this.fireRandom.nextFloat() / 2;

        if(this.fireInterval < this.minimumFireInterval) {
            this.fireInterval = this.minimumFireInterval;
        }
    }

    @Override
    public void onCollision(Direction collisionDirection, Vector2 collisionPosition) {

    }

}
