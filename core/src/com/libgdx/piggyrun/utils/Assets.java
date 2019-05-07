package com.libgdx.piggyrun.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {
    private static final String TAG = Assets.class.getName();
    private static final Assets instance = new Assets();
    private AssetManager assetManager;
    public PiggyAsset piggyAsset;
    public CoinAsset coinAsset;
    public GroundAsset groundAsset;
    public FontAsset fontAsset;
    public CloudAsset cloudAsset;
    public SoundAsset soundAsset;
    public SpikesAsset spikesAsset;

    public static Assets getInstance(){
        return instance;
    }

    public void init(AssetManager assetManager){
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);
        assetManager.load("sounds/pickup_coin.wav", Sound.class);
        assetManager.load("sounds/jump.wav", Sound.class);
        assetManager.load("sounds/death.wav", Sound.class);
        assetManager.finishLoading();
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS);
        piggyAsset = new PiggyAsset(atlas);
        coinAsset = new CoinAsset(atlas);
        groundAsset = new GroundAsset(atlas);
        cloudAsset = new CloudAsset(atlas);
        spikesAsset = new SpikesAsset(atlas);
        fontAsset = new FontAsset();
        soundAsset = new SoundAsset(assetManager);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" +  asset.fileName + "'", throwable);
    }

    public class PiggyAsset {
        public TextureAtlas.AtlasRegion piggy;

        public PiggyAsset(TextureAtlas atlas) {
            piggy = atlas.findRegion("piggy");
        }
    }

    public class CoinAsset {
        public TextureAtlas.AtlasRegion coin;

        public CoinAsset(TextureAtlas atlas) {
            coin = atlas.findRegion("coin");
        }
    }

    public class GroundAsset {
        public Array<TextureAtlas.AtlasRegion> ground_center;
        public Array<TextureAtlas.AtlasRegion> ground_edge;

        public GroundAsset(TextureAtlas atlas) {
            ground_center = atlas.findRegions("ground-center");
            ground_edge = atlas.findRegions("ground-edge");
        }
    }

    public class CloudAsset{
        public Array<TextureAtlas.AtlasRegion> clouds;

        public CloudAsset(TextureAtlas atlas){
            clouds = atlas.findRegions("cloud");
        }
    }

    public class SpikesAsset{
        public TextureAtlas.AtlasRegion spikes;

        public SpikesAsset(TextureAtlas atlas){spikes = atlas.findRegion("Spikes");}
    }

    public class FontAsset {
        public BitmapFont fontDefault;

        public FontAsset() {
            this.fontDefault = new BitmapFont(Gdx.files.internal("fonts/default.fnt"));
        }
    }

    public class SoundAsset {
        public final Sound pickupCoin;
        public final Sound jump;
        public final Sound death;

        public SoundAsset(AssetManager assetManager){
            pickupCoin = assetManager.get("sounds/pickup_coin.wav",Sound.class);
            jump = assetManager.get("sounds/jump.wav", Sound.class);
            death = assetManager.get("sounds/death.wav", Sound.class);
        }
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fontAsset.fontDefault.dispose();
    }
}
