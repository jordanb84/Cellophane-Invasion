package com.djam2.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.djam2.game.assets.Assets;

public enum SkinType {
    Sgx("skin/sgxui/sgx-ui.json"), Arcade("skin/arcade/arcade-ui.json"), Clean_Crispy("skin/clean-crispy/clean-crispy-ui.json")
    ;

    SkinType(String path) {
        this.SKIN = Assets.getInstance().getSkin(path);
    }

    public final Skin SKIN;

}