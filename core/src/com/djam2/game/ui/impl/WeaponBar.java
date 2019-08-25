package com.djam2.game.ui.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.djam2.game.entity.living.impl.EntityPlayer;
import com.djam2.game.entity.weapon.Weapon;
import com.djam2.game.map.Map;
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

    private Window window;

    private EntityPlayer player;

    private Window informationWindow;

    private Label enemiesLabel;
    private Label spaceLabel;
    private Label waveLabel;

    private Vector2 centerPosition;

    public WeaponBar(EntityPlayer player) {
        super(SkinType.Sgx.SKIN, null);
        this.player = player;
    }

    @Override
    public void create() {
        this.centerPosition = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        this.informationWindow = new Window("Status", this.getDefaultSkin());

        this.waveLabel = new Label("Wave: ", SkinType.Arcade.SKIN);
        this.informationWindow.add(this.waveLabel);
        this.informationWindow.row();
        this.enemiesLabel = new Label("Enemies: ", SkinType.Arcade.SKIN);
        this.informationWindow.add(this.enemiesLabel);
        this.informationWindow.row();
        this.spaceLabel = new Label("Space: ", SkinType.Arcade.SKIN);
        this.informationWindow.add(this.spaceLabel);

        int informationWidth = 280;
        int informationHeight = 140;
        this.informationWindow.setSize(informationWidth, informationHeight);

        this.window = new Window("Weapons", this.getDefaultSkin());

        this.weaponSlots = new ArrayList<WeaponSlot>();
        this.weapons = new ArrayList<Weapon>();

        this.addWeapon(WeaponType.PlayerBasic, false);
        this.addWeapon(WeaponType.PlayerBurst, false);

        this.window.setPosition(Gdx.graphics.getWidth() / 2 - informationWidth / 2, 0);
        this.window.setWidth(informationWidth);
        this.getRootTable().addActor(window);

        this.informationWindow.setPosition(Gdx.graphics.getWidth() / 2 - informationWidth / 2, Gdx.graphics.getHeight() - informationHeight);
        this.getRootTable().addActor(this.informationWindow);

        this.setSelectedWeapon(0);

        this.selectorInputs = new HashMap<Integer, Integer>();

        this.selectorInputs.put(Input.Keys.NUM_1, 0);
        this.selectorInputs.put(Input.Keys.NUM_2, 1);

        //this.informationWindow.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        for(HashMap.Entry<Integer, Integer> selector : this.selectorInputs.entrySet()) {
            if(Gdx.input.isKeyJustPressed(selector.getKey())) {
                this.setSelectedWeapon(selector.getValue());
            }
        }

        Map map = this.getPlayer().getParentMap();

        this.enemiesLabel.setText("Enemies: " + map.getEnemyCount());
        this.spaceLabel.setText("Space: " + (int) map.getLowestDistanceFromBase());

        int humanWave = map.getWave() + 1;
        this.waveLabel.setText("Wave: " + humanWave + "/" + map.getTotalWaves());
    }

    public void displayWinMessage() {
        Window wonWindow = new Window("You've won!", this.getDefaultSkin());

        int wonWidth = 400;
        int wonHeight = 200;

        wonWindow.setSize(wonWidth, wonHeight);

        Label wonLabel = new Label("Congratulations!", SkinType.Arcade.SKIN);

        wonWindow.add(wonLabel).fill();

        wonWindow.setPosition(this.centerPosition.x - wonWidth / 2, this.centerPosition.y - wonHeight / 2);
        this.getRootTable().addActor(wonWindow);
    }

    public void displayLostMessage() {
        Window wonWindow = new Window("You've died!", this.getDefaultSkin());

        int wonWidth = 400;
        int wonHeight = 200;

        wonWindow.setSize(wonWidth, wonHeight);

        Label wonLabel = new Label("Game over. Restarting...", SkinType.Arcade.SKIN);

        wonWindow.add(wonLabel).fill();

        wonWindow.setPosition(this.centerPosition.x - wonWidth / 2, this.centerPosition.y - wonHeight / 2);
        this.getRootTable().addActor(wonWindow);
    }

    private void addWeapon(WeaponType weaponType, boolean pad) {
        WeaponSlot weaponSlot = new WeaponSlot(this, weaponType, this.weaponSlots.size());

        this.weaponSlots.add(weaponSlot);
        this.weapons.add(weaponType.WEAPON);

        this.window.add(weaponSlot);

        if(pad) {
            //this.getRootTable().padLeft(weaponSlot.getWidth());
        }
        //this.getRootTable().center().bottom().add(weaponSlot);
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

    public EntityPlayer getPlayer() {
        return this.player;
    }

}
