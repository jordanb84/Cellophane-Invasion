package com.djam2.game.sound;

import com.badlogic.gdx.audio.Sound;
import com.djam2.game.assets.Assets;

public enum SfxType {
    Death0("sound/death0.mp3"), Whoosh("sound/whoosh.wav")
    ;

    SfxType(String path) {
        this.SOUND = Assets.getInstance().getSound(path);
    }

    public final Sound SOUND;

    public static void playSound(SfxType sound) {
        sound.SOUND.play(0.4f);
    }

}