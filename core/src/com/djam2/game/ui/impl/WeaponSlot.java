package com.djam2.game.ui.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.djam2.game.assets.Assets;
import com.djam2.game.entity.weapon.Weapon;
import com.djam2.game.ui.SkinType;

public class WeaponSlot extends ImageButton {

    private WeaponType weaponType;

    private boolean selected;

    private Sprite selectedSprite;

    private float selectedMaxWidth;
    private float selectedMaxHeight;

    public WeaponSlot(final WeaponBar weaponBar, WeaponType weaponType, final int slotIndex) {
        super(SkinType.Sgx.SKIN);
        this.weaponType = weaponType;
        this.setupTooltip();
        this.selectedSprite = Assets.getInstance().getSprite("weapon/selectedweapon.png");

        this.selectedMaxWidth = this.selectedSprite.getWidth();
        this.selectedMaxHeight = this.selectedSprite.getHeight();

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

    private float elapsedSinceFire;

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

            if(!this.getWeapon().isCharged() && this.getWeapon().requiresCharge()) {
                this.selectedSprite.setPosition(sprite.getX(), sprite.getY());
                this.selectedSprite.setColor(Color.RED);
                this.selectedSprite.setAlpha(0.5f);
                this.selectedSprite.setSize(this.selectedMaxWidth, this.selectedMaxHeight);
                this.selectedSprite.draw(batch);
                this.selectedSprite.setColor(Color.WHITE);
                this.selectedSprite.setAlpha(0.8f);
                this.selectedSprite.setSize(this.selectedSprite.getWidth(), this.selectedMaxHeight * this.getWeapon().getChargePercentage());
                this.selectedSprite.draw(batch);
            }
        }

        this.elapsedSinceFire += 1 * Gdx.graphics.getDeltaTime();

        if(this.elapsedSinceFire >= 1f && !this.getWeapon().isCharged()) {
            this.elapsedSinceFire = 0;

            this.getWeapon().incrementCharge();

            if(this.getWeapon().getChargePercentage() >= 1) {
                this.getWeapon().setCharged(true);
                this.getWeapon().setChargePercentage(0);
            }
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Weapon getWeapon() {
        return this.weaponType.WEAPON;
    }

}
