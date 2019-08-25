package com.djam2.game.ui.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.djam2.game.assets.Assets;
import com.djam2.game.ui.SkinType;

public class WeaponSlot extends ImageButton {

    private WeaponType weaponType;

    private boolean selected;

    private Sprite selectedSprite;

    public WeaponSlot(final WeaponBar weaponBar, WeaponType weaponType, final int slotIndex) {
        super(SkinType.Sgx.SKIN);
        this.weaponType = weaponType;
        this.setupTooltip();
        this.selectedSprite = Assets.getInstance().getSprite("weapon/selectedweapon.png");

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                weaponBar.setSelectedWeapon(slotIndex);
            }
        });
    }

    private void setupTooltip() {
        TextTooltip tooltip = new TextTooltip(this.weaponType.TOOLTIP, SkinType.Arcade.SKIN);
        tooltip.setInstant(true);
        tooltip.getActor().setWrap(false);
        tooltip.getActor().setColor(Color.WHITE);

        this.addListener(tooltip);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Sprite sprite = this.weaponType.SPRITE;

        sprite.setAlpha(0.8f);
        sprite.setPosition(this.getX() + sprite.getWidth() / 2.5f, this.getY() + sprite.getHeight() / 2.5f);
        sprite.draw(batch);

        if(this.selected) {
            sprite.setAlpha(0.4f);
            sprite.setColor(Color.GREEN);
            sprite.draw(batch);
            sprite.setColor(Color.WHITE);
            //this.selectedSprite.setPosition(sprite.getX(), sprite.getY());
            //this.selectedSprite.draw(batch);
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
