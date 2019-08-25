package com.djam2.game.sound;

import com.badlogic.gdx.audio.Music;
import com.djam2.game.assets.Assets;

public enum MusicType {
    Background("music/bg.mp3")
    ;

    MusicType(String path) {
        this.MUSIC = Assets.getInstance().getMusic(path);
    }

    public final Music MUSIC;

    public static void loop(MusicType music) {
        music.MUSIC.play();
        music.MUSIC.setLooping(true);
    }

}