package com.libgdx.piggyrun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.libgdx.piggyrun.states.MenuState;
import com.libgdx.piggyrun.utils.Assets;
import com.libgdx.piggyrun.utils.GamePreferences;

public class PiggyRunMain extends Game {
	@Override
	public void create () {
		Assets.getInstance().init(new AssetManager());
		GamePreferences.instance.load();
		setScreen(new MenuState(this));
	}
	
	@Override
	public void dispose () {
		screen.dispose();
		Assets.getInstance().dispose();
	}
}
