package com.djam2.game.ui.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.djam2.game.entity.weapon.Weapon;
import com.djam2.game.state.StateManager;
import com.djam2.game.ui.SkinType;
import com.djam2.game.ui.UiContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeaponBar extends UiContainer {

    private List<WeaponSlot> weaponSlots;

    private List<Weapon> weapons;

    private int selectedWeapon;

    private HashMap<Integer, Integer> selectorInputs;

    public WeaponBar() {
        super(SkinType.Sgx.SKIN, null);
    }

    @Override
    public void create() {
        this.weaponSlots = new ArrayList<WeaponSlot>();
        this.weapons = new ArrayList<Weapon>();

        this.addWeapon(WeaponType.PlayerBasic, false);
        this.addWeapon(WeaponType.PlayerBurst, false);

        this.setSelectedWeapon(0);

        this.selectorInputs = new HashMap<Integer, Integer>();

        this.selectorInputs.put(Input.Keys.NUM_1, 0);
        this.selectorInputs.put(Input.Keys.NUM_2, 1);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        for(HashMap.Entry<Integer, Integer> selector : this.selectorInputs.entrySet()) {
            if(Gdx.input.isKeyJustPressed(selector.getKey())) {
                this.setSelectedWeapon(selector.getValue());
            }
        }
    }

    private void addWeapon(WeaponType weaponType, boolean pad) {
        WeaponSlot weaponSlot = new WeaponSlot(this, weaponType, this.weaponSlots.size());

        this.weaponSlots.add(weaponSlot);
        this.weapons.add(weaponType.WEAPON);

        if(pad) {
            this.getRootTable().padLeft(weaponSlot.getWidth());
        }
        this.getRootTable().center().bottom().add(weaponSlot);
    }

    public Weapon getSelectedWeapon() {
        return this.weapons.get(this.selectedWeapon);
    }

    public void setSelectedWeapon(int index) {
        for(WeaponSlot weaponSlot : this.weaponSlots) {
            weaponSlot.setSelected(false);
        }

        this.selectedWeapon = index;
        this.weaponSlots.get(index).setSelected(true);
    }

}
