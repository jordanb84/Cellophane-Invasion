package com.djam2.game.entity.weapon.impl;

import com.badlogic.gdx.math.Vector2;
import com.djam2.game.entity.EntityType;
import com.djam2.game.entity.impl.EntityBullet;
import com.djam2.game.entity.weapon.Weapon;
import com.djam2.game.map.Map;

public class WeaponPlayerBasic extends Weapon {

    public WeaponPlayerBasic() {
        super(0.2f);
    }

    @Override
    public void fire(Vector2 position, Vector2 destination, Map parentMap) {
        float damage = 35;
        EntityBullet bullet = new EntityBullet(position, destination, parentMap, damage, EntityType.ENEMY, 3.2f, 0.8f);

        parentMap.spawnEntity(bullet);
    }

}
