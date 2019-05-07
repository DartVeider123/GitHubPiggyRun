package com.libgdx.piggyrun.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.libgdx.piggyrun.PiggyRunMain;
import com.libgdx.piggyrun.utils.Constants;
import com.libgdx.piggyrun.utils.GamePreferences;

public class MenuState extends State {
    private Stage stage;
    private Skin skin;
    private float viewportUp;
    private Button buttonExit;
    private Window exitWindow;
    private Window optWindow;
    private CheckBox checkSound;
    private CheckBox checkMusic;
    private CheckBox checkNotifications;
    private Slider sliderSound;
    private Slider sliderMusic;
    private Window currentWindow;

    public MenuState(PiggyRunMain main) {
        super(main);
    }

    private void init(){
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal(Constants.UISKIN_JSON),new TextureAtlas(Constants.TEXTURE_ATLAS));
        float aspectRatio = (float)Gdx.graphics.getHeight()/Gdx.graphics.getWidth();
        float viewportHeight = Constants.GUI_WIDTH * aspectRatio;
        viewportUp = (Constants.GUI_HEIGHT-viewportHeight) / 2 + viewportHeight;
        buildStage();
        Gdx.input.setCatchBackKey(true);
    }

    private void buildStage(){
        stage.addActor(new Image(skin,"BackGround"));
        Image logo = new Image(skin,"Logo");
        logo.setPosition(67,76);
        stage.addActor(logo);
        Button buttonPlay = new Button(skin,"play");
        buttonPlay.setPosition(97.5f, 31);
        buttonPlay.setSize(44,20);
        buttonPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onPlayClicked();
            }
        });
        stage.addActor(buttonPlay);
        Button buttonShop = new Button(skin, "shop");
        buttonShop.setPosition(172.5f,35);
        buttonShop.setSize(25,25);
        buttonShop.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onShopClicked();
            }
        });
        stage.addActor(buttonShop);
        Button buttonOpt = new Button(skin,"options");
        buttonOpt.setPosition(41.5f,35);
        buttonOpt.setSize(25,25);
        buttonOpt.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onOptClicked();
            }
        });
        stage.addActor(buttonOpt);
        buttonExit = new Button(skin, "exit");
        buttonExit.setPosition(3,viewportUp-18);
        buttonExit.setSize(15,15);
        buttonExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onExitClicked();
            }
        });
        stage.addActor(buttonExit);
        stage.addActor(buildExitWindow());
        stage.addActor(buildOptWindow());
        currentWindow = null;
    }

    private Table buildExitWindow(){
        skin.getFont("default-font").getData().setScale(0.33f);
        skin.getFont("small-font").getData().setScale(0.4f);
        exitWindow = new Window("Exit Piggy Run?",skin);
        exitWindow.setSize(99,55);
        exitWindow.setPosition(Constants.GUI_WIDTH / 2 - exitWindow.getWidth() / 2, Constants.GUI_HEIGHT / 2 - exitWindow.getHeight() / 2);
        exitWindow.padTop(11f);
        exitWindow.getTitleLabel().setAlignment(Align.center,Align.center);
        exitWindow.setMovable(false);
        Label text = new Label("Do you want to exit Piggy Run?", skin, "small-font", "white");
        text.setPosition(2,25);
        exitWindow.addActor(text);
        Button noButton = new Button(skin, "no");
        noButton.setSize(20.6f,10);
        noButton.setPosition(20,8);
        noButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitWindow.setVisible(false);
                currentWindow = null;
            }
        });
        exitWindow.addActor(noButton);
        Button yesButton = new Button(skin, "yes");
        yesButton.setSize(20.6f,10);
        yesButton.setPosition(60,8);
        yesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
                dispose();
            }
        });
        exitWindow.addActor(yesButton);
        exitWindow.setVisible(false);
        exitWindow.setModal(true);
        return exitWindow;
    }

    private Table buildOptWindow(){
        skin.getFont("default-font").getData().setScale(0.33f);
        optWindow = new Window("Options",skin,"options");
        optWindow.padTop(11f);
        optWindow.getTitleLabel().setAlignment(Align.center,Align.center);
        optWindow.setMovable(false);
        optWindow.setModal(true);
        Table table = new Table();
        table.pad(10,5,3,5);
        table.row().padBottom(3);
        table.columnDefaults(0).padRight(5);
        table.columnDefaults(1).padRight(5);
        checkSound = new CheckBox("",skin);
        table.add(checkSound);
        table.add(new Label("Sound",skin));
        sliderSound = new Slider(0.0f,1.0f,0.1f,false,skin);
        table.add(sliderSound).size(50,4);
        table.row().padBottom(2);
        checkMusic = new CheckBox("",skin);
        table.add(checkMusic);
        table.add(new Label("Music", skin));
        sliderMusic = new Slider(0.0f,1.0f,0.1f,false,skin);
        table.add(sliderMusic).size(50,4);
        table.row();
        checkNotifications = new CheckBox("", skin);
        table.add(checkNotifications);
        table.add(new Label("Push-Notifications",skin)).colspan(2);
        optWindow.add(table).row();
        optWindow.add(buildOptButtons()).padBottom(5);
        optWindow.pack();
        optWindow.setPosition(Constants.GUI_WIDTH / 2 - optWindow.getWidth() / 2, Constants.GUI_HEIGHT / 2 - optWindow.getHeight() / 2);
        optWindow.setVisible(false);
        return optWindow;
    }

    private Table buildOptButtons(){
        Button cancelButton = new Button(skin, "cancel");
        Button applyButton = new Button(skin, "apply");
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                optWindow.setVisible(false);
                currentWindow = null;
            }
        });
        applyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.instance.soundEnable = checkSound.isChecked();
                GamePreferences.instance.musicEnable = checkMusic.isChecked();
                GamePreferences.instance.notificationsEnable = checkNotifications.isChecked();
                GamePreferences.instance.soundLevel = sliderSound.getValue();
                GamePreferences.instance.musicLevel = sliderMusic.getValue();
                GamePreferences.instance.save();
                optWindow.setVisible(false);
                currentWindow = null;
            }
        });
        Table table = new Table();
        table.add(cancelButton).padRight(20);
        table.add(applyButton).size(39,16);
        return table;
    }

    private void onPlayClicked(){
        game.setScreen(new GameState(game));
    }

    private void onShopClicked(){

    }

    private void onOptClicked(){
        checkSound.setChecked(GamePreferences.instance.soundEnable);
        checkMusic.setChecked(GamePreferences.instance.musicEnable);
        checkNotifications.setChecked(GamePreferences.instance.notificationsEnable);
        sliderSound.setValue(GamePreferences.instance.soundLevel);
        sliderMusic.setValue(GamePreferences.instance.musicLevel);
        optWindow.setVisible(true);
        currentWindow = optWindow;
    }

    private void onExitClicked(){
        exitWindow.setVisible(true);
        currentWindow = exitWindow;
    }

    @Override
    public void show() {
        stage = new Stage(new FillViewport(Constants.GUI_WIDTH, Constants.GUI_HEIGHT));
        init();
    }

    @Override
    public void render(float delta) {
        checkWindows();
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    private void checkWindows() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if (currentWindow != null) {
                currentWindow.setVisible(false);
                currentWindow = null;
            }
            else {
                exitWindow.setVisible(true);
                currentWindow = exitWindow;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height,true);
        float aspectRatio = (float)Gdx.graphics.getHeight()/Gdx.graphics.getWidth();
        float viewportHeight = Constants.GUI_WIDTH * aspectRatio;
        viewportUp = (Constants.GUI_HEIGHT-viewportHeight) / 2 + viewportHeight;
        buttonExit.setPosition(3,viewportUp - 18);
    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
