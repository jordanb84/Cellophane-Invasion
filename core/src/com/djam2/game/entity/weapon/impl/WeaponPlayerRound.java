package com.djam2.game.entity.weapon.impl;

import com.badlogic.gdx.math.Vector2;
import com.djam2.game.entity.EntityType;
import com.djam2.game.entity.impl.EntityBullet;
import com.djam2.game.entity.impl.EntityBulletRound;
import com.djam2.game.entity.weapon.Weapon;
import com.djam2.game.map.Map;
import com.djam2.game.sound.SfxType;

public class WeaponPlayerRound extends Weapon {

    public WeaponPlayerRound() {
        super(0.2f, 0.3f);
    }

    @Override
    public void fire(Vector2 position, Vector2 destination, Map parentMap) {
        float damage = 200;
        EntityBullet bullet = new EntityBulletRound(position, destination, parentMap, damage, EntityType.ENEMY, 3.6f, 0.8f);

        parentMap.spawnEntity(bullet);

        SfxType.playSound(SfxType.Whoosh);
    }

}
