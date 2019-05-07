package com.libgdx.piggyrun.utils;

import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    public static final AudioManager instance = new AudioManager();

    public void play(Sound sound){
        play(sound, 1);
    }

    public void play(Sound sound, float volume) {
        play(sound,volume,1);
    }

    public void play(Sound sound, float volume, float pitch) {
        play(sound,volume,pitch,0);
    }

    public void play(Sound sound, float volume, float pitch, float pan) {
        if(GamePreferences.instance.soundEnable)
        sound.play(volume * GamePreferences.instance.soundLevel, pitch, pan);
    }
}
