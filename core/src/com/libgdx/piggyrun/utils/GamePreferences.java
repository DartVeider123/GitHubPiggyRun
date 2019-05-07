package com.libgdx.piggyrun.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GamePreferences {
    public static final GamePreferences instance = new GamePreferences();
    private Preferences prefs;
    public boolean soundEnable;
    public boolean musicEnable;
    public boolean notificationsEnable;
    public float soundLevel;
    public float musicLevel;

    public GamePreferences() {
        prefs = Gdx.app.getPreferences("PiggyRun.prefs");
    }

    public void load(){
        soundEnable = prefs.getBoolean("soundEnable",true);
        musicEnable = prefs.getBoolean("musicEnable",true);
        notificationsEnable = prefs.getBoolean("notificationsEnable",true);
        soundLevel = prefs.getFloat("soundLevel",0.5f);
        musicLevel = prefs.getFloat("musicLevel",0.5f);
    }

    public void save(){
        prefs.putBoolean("soundEnable",soundEnable);
        prefs.putBoolean("musicEnable",musicEnable);
        prefs.putBoolean("notificationsEnable",notificationsEnable);
        prefs.putFloat("soundLevel",soundLevel);
        prefs.putFloat("musicLevel",musicLevel);
        prefs.flush();
    }
}
