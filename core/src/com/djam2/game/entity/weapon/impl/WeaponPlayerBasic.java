package com.djam2.game.entity.weapon.impl;

import com.badlogic.gdx.math.Vector2;
import com.djam2.game.entity.EntityType;
import com.djam2.game.entity.impl.EntityBullet;
import com.djam2.game.entity.weapon.Weapon;
import com.djam2.game.map.Map;
import com.djam2.game.sound.SfxType;

public class WeaponPlayerBasic extends Weapon {

    public WeaponPlayerBasic() {
        super(0.2f, 1);
        this.setRequiresCharge(false);
    }

    @Override
    public void fire(Vector2 position, Vector2 destination, Map parentMap) {
        float damage = 35;
        EntityBullet bullet = new EntityBullet(position, destination, parentMap, damage, EntityType.ENEMY, 3.4f, 0.6f);

        parentMap.spawnEntity(bullet);

        SfxType.playSound(SfxType.Whoosh);
    }

}
