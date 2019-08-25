package com.djam2.game.ui.impl;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.djam2.game.assets.Assets;
import com.djam2.game.entity.weapon.Weapon;
import com.djam2.game.entity.weapon.impl.WeaponPlayerBasic;
import com.djam2.game.entity.weapon.impl.WeaponPlayerBurst;

public enum WeaponType {
    PlayerBasic("Primary Torpedo", new WeaponPlayerBasic()),
    PlayerBurst("Burst Torpedo", new WeaponPlayerBurst())
    ;

    WeaponType(String tooltip, Weapon weapon) {
        this.SPRITE = Assets.getInstance().getSprite("weapon/" + this.name().toLowerCase() + ".png");
        this.TOOLTIP = tooltip;
        this.WEAPON = weapon;
    }

    public final Sprite SPRITE;
    public final String TOOLTIP;
    public final Weapon WEAPON;

}
