package com.libgdx.piggyrun.states;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.libgdx.piggyrun.PiggyRunMain;
import com.libgdx.piggyrun.utils.Assets;

public abstract class State implements Screen {
    protected PiggyRunMain game;

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    protected boolean isPaused;

    public State(PiggyRunMain game) {
        this.game = game;
        isPaused = false;
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
        Assets.getInstance().init(new AssetManager());
    }

    @Override
    public void dispose() {
        Assets.getInstance().dispose();
    }
}
