package com.libgdx.piggyrun.states;

import
        com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.libgdx.piggyrun.PiggyRunMain;
import com.libgdx.piggyrun.objects.*;
import com.libgdx.piggyrun.utils.Assets;
import com.libgdx.piggyrun.utils.AudioManager;
import com.libgdx.piggyrun.utils.Constants;

public class GameState extends State implements InputProcessor {
    private Rectangle rb1;
    private Rectangle rb2;
    private boolean isFirst;
    private Stage stage;
    private Stage stageGUI;
    private SpriteBatch batch;
    public float meters;
    private int coins;
    private boolean antiGravitate;
    private boolean firstSpikes;
    private GroundManager groundManager;
    private Clouds clouds;
    private Pig pig;
    private OrthographicCamera camera;
    private float viewportUp;
    public static boolean isTouched;
    private Skin skin;
    private Button buttonPause;
    private Window pauseWindow;

    public GameState(PiggyRunMain main) {
        super(main);
    }

    private void init(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.V_WIDTH, Constants.V_HEIGHT);
        stage = new Stage(new FillViewport(Constants.V_WIDTH, Constants.V_HEIGHT,camera),batch);
        stageGUI = new Stage(new FillViewport(Constants.GAME_GUI_WIDTH,Constants.GAME_GUI_HEIGHT),batch);
        Gdx.input.setInputProcessor(new InputMultiplexer(stageGUI,this));
        Gdx.input.setCatchBackKey(true);
        skin = new Skin(Gdx.files.internal(Constants.UISKIN_JSON),new TextureAtlas(Constants.TEXTURE_ATLAS));
        isFirst = true;
        meters = 0;
        coins = 0;
        antiGravitate = false;
        firstSpikes = false;
        pig = new Pig();
        groundManager = new GroundManager(stage, pig);
        clouds = new Clouds();
        for (Ground ground : groundManager.grounds) {
            stage.addActor(ground);
        }
        for (Clouds.Cloud cloud : clouds.clouds) {
            stage.addActor(cloud);
        }
        stage.addActor(pig);
        rb1 = pig.rigidBody;
        rb2 = new Rectangle();
        viewportUp = 0;
        isTouched = false;
        buildGUIStage();
    }

    private void buildGUIStage() {
        buttonPause = new Button(skin,"pause");
        buttonPause.setSize(23,23);
        buttonPause.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pause();
                pauseWindow.setVisible(true);
            }
        });
        skin.getFont("default-font").getData().setScale(0.6f);
        pauseWindow = new Window("Paused",skin);
        pauseWindow.getTitleLabel().setAlignment(Align.center,Align.center);
        pauseWindow.padTop(23f);
        Button exitGame = new Button(skin,"exitGame");
        exitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuState(game));
            }
        });
        Button resume = new Button(skin,"play");
        resume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseWindow.setVisible(false);
                resume();
            }
        });
        pauseWindow.add(exitGame).size(49.5f,22.5f).pad(5,13,13,6.5f);
        pauseWindow.add(resume).size(49.5f,22.5f).pad(5,6.5f,13,13);
        pauseWindow.pack();
        pauseWindow.setMovable(false);
        pauseWindow.setModal(true);
        pauseWindow.setVisible(false);
        stageGUI.addActor(buttonPause);
        stageGUI.addActor(pauseWindow);
    }

    private void checkCollisions(){
        float widthOffset = 9.0f;
        rb1.set(pig.getX(),pig.getY(),rb1.width,rb1.height);
        for (Ground ground:groundManager.grounds) {
            rb2.set(ground.getX() + 4,ground.getY(),ground.rigidBody.width - widthOffset,ground.rigidBody.height);
            if(rb1.overlaps(rb2) && !antiGravitate)
                onCollisionsWithGround(rb2);
        }
        for (Coin coin:groundManager.coins) {
            rb2.set(coin.getX()+0.5f,coin.getY()+0.5f,coin.rigidBody.width,coin.rigidBody.height);
            if(rb1.overlaps(rb2))
                onCollisionsWithCoin(coin);
        }
        for(Spikes spikes:groundManager.spikeses){
            rb2.set(spikes.getX() + 3,spikes.getY(),spikes.rigidBody.width-6,spikes.rigidBody.height-1);
            if(rb1.overlaps(rb2))
                onCollisionsWithSpikes(spikes);
        }
    }

    private void onCollisionsWithSpikes(Spikes spikes) {
        stage.addActor(pig);
        if(!firstSpikes) {
            GroundManager.isStoped = true;
            clouds.isStoped = true;
            antiGravitate = true;
            pig.setRotation(MathUtils.degreesToRadians * 300);
            if (pig.getY() >= spikes.getY() + spikes.getHeight() - 2.5)
                pig.velocity.y = 25;
            AudioManager.instance.play(Assets.getInstance().soundAsset.death);
        }
        firstSpikes = true;
    }

    private void onCollisionsWithCoin(Coin coin) {
        if(!coin.isDestroyed()) {
            coin.destroy();
            groundManager.coins.removeValue(coin,true);
            stage.getActors().removeValue(coin,true);
            coins++;
            AudioManager.instance.play(Assets.getInstance().soundAsset.pickupCoin);
        }
    }

    private void onCollisionsWithGround(Rectangle rb){
        if(Math.abs(pig.getY() - rb.y - rb.height) < 2) {
            pig.onCollisionWithGround = true;
            if(isFirst){
                pig.setY(rb.y + rb.height);
                isFirst = false;
            }
        }
        else {
            GroundManager.isStoped = true;
            clouds.isStoped = true;
        }
    }

    private void handleInputDebug(){
        if(pig.jumpState == JumpState.JUMP_RISING || pig.onCollisionWithGround) {
            if (isTouched)
                pig.setJumpState(true);
        }
        if(!(pig.jumpState == JumpState.JUMP_RISING) && !isTouched){
            pig.jumpingTime = 0;
        }
        if(Gdx.input.justTouched() && isTouched) {
            if(pig.onCollisionWithGround) {
                AudioManager.instance.play(Assets.getInstance().soundAsset.jump,0.8f);
                pig.onCollisionWithGround = false;
                isFirst = true;
            }
        }else if(isTouched && pig.jumpingTime == 0 && pig.onCollisionWithGround)
            AudioManager.instance.play(Assets.getInstance().soundAsset.jump,0.8f);
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if(!pauseWindow.isVisible()) {
                pauseWindow.setVisible(true);
                pause();
            }else {
                pauseWindow.setVisible(false);
                resume();
            }
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        isTouched = true;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isTouched = false;
        return true;
    }

    public void update(float deltaTime) {
        stage.act();
        clouds.act(deltaTime);
        groundManager.act(deltaTime);
        pig.onCollisionWithGround = false;
        checkCollisions();
        meters += Math.abs(groundManager.grounds.get(0).velocity.x * deltaTime) / 5;
        Vector3 vector3 = new Vector3(camera.position);
        camera.position.y = Math.max(19,vector3.lerp(new Vector3(pig.getX(),pig.getY(),0),deltaTime*2.5f).y);
        camera.update();
    }

    private void renderCoins() {
        stageGUI.getBatch().setProjectionMatrix(stageGUI.getCamera().combined);
        stageGUI.getBatch().begin();
        TextureRegion reg = Assets.getInstance().coinAsset.coin;
        stageGUI.getBatch().draw(reg.getTexture(),6,viewportUp - 21,15,15,reg.getRegionX(),reg.getRegionY(),
                reg.getRegionWidth(),reg.getRegionHeight(),false,false);
        Assets.getInstance().fontAsset.fontDefault.getData().setScale(0.63f);
        Assets.getInstance().fontAsset.fontDefault.draw(stageGUI.getBatch(),String.valueOf(coins),24f,viewportUp - 6.49f);
        stageGUI.getBatch().end();
    }

    @Override
    public void show() {
        init();
    }

    @Override
    public void render(float delta) {
        handleInputDebug();
        if(!isPaused)
        update(delta);
        stageGUI.act();
        Gdx.gl.glClearColor(0f,0.853f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getViewport().apply();
        stage.draw();
        stageGUI.getViewport().apply();
        stageGUI.draw();
        renderCoins();
        if(pig.getY()<=-11-pig.getY()){
            game.setScreen(new MenuState(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height,true);
        stageGUI.getViewport().update(width, height,true);
        float viewportHeight = ((float) height / width) * Constants.GAME_GUI_WIDTH;
        viewportUp = (Constants.GAME_GUI_HEIGHT - viewportHeight) / 2 + viewportHeight;
        buttonPause.setPosition(270,viewportUp - 30);
        pauseWindow.setPosition(150 - pauseWindow.getWidth() / 2,150 - pauseWindow.getHeight() / 2);
    }

    @Override
    public void pause() {
        super.pause();
        pauseWindow.setVisible(true);
    }

    @Override
    public void resume() {
        if(!pauseWindow.isVisible())
        super.resume();
    }

    @Override
    public void hide() {
        stage.dispose();
        stageGUI.dispose();
        batch.dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
