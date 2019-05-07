package com.libgdx.piggyrun.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.libgdx.piggyrun.utils.Constants;

public class GroundManager {
    public Array<Ground> grounds;
    private Ground lastGround;
    public static boolean isStoped;
    public Array<Coin> coins;
    public Array<Spikes> spikeses;
    private Stage stage;
    private Pig pig;

    public GroundManager(Stage stage, Pig pig) {
        this.stage = stage;
        this.pig = pig;
        init();
    }

    private void init(){
        grounds = new Array<Ground>();
        grounds.add(new Ground(new Vector2(0, 13f),4, Constants.DEFAULT_VELOCITY));
        grounds.add(new Ground(new Vector2(70, 10f), 3, Constants.DEFAULT_VELOCITY));
        grounds.add(new Ground(new Vector2(125, 15f), 3, Constants.DEFAULT_VELOCITY));
        lastGround = grounds.get(2);
        coins = new Array<Coin>();
        spikeses = new Array<Spikes>();
        isStoped = false;
    }

    private void reBuildGround(Ground ground){
        float positionY = MathUtils.clamp(MathUtils.random(5,17),lastGround.getY() - lastGround.getHeight(),
                lastGround.getY() + lastGround.getHeight() * 2 - 22);
        float positionX = lastGround.getX() + (lastGround.getWidth() * lastGround.lenght + lastGround.edgeDimension.x * 2) + MathUtils.random(12.3f,17);
        ground.reBuild(new Vector2(positionX,positionY), MathUtils.random(3,5), ground.velocity);
        lastGround = ground;
        int randomValue = MathUtils.random(0,2);
        if(randomValue == Constants.GROUND_WITH_COINS){
            buildGroundWithCoin(ground);
        }else if(randomValue == Constants.GROUND_WITH_SPIKES){
            buildGroundWithSpikes(ground);
        }
    }

    private void buildGroundWithSpikes(Ground ground) {
        int xColumn = MathUtils.random(1,Math.max(1,ground.lenght-2));
        Spikes spikes = new Spikes(ground,new Vector2(ground.edgeDimension.x + ground.getWidth() * xColumn,-1.2f));
        spikeses.add(spikes);
        stage.addActor(spikes);
        stage.getActors().removeValue(pig,true);
        stage.getActors().add(pig);
    }

    private void buildGroundWithCoin(Ground ground){
        int numberOfCoins = MathUtils.random(1, ground.lenght*2-1);
        int xColumn = MathUtils.random(0,Math.max(0,ground.lenght-numberOfCoins));
        int secondOffset = MathUtils.random(0,Math.max(0,ground.lenght*2-1-numberOfCoins));
        for (int i = xColumn; i < numberOfCoins + xColumn; i++) {
            if(i < ground.lenght) {
                Coin coin = new Coin(ground,new Vector2());
                coin.localPosition.set(new Vector2(ground.edgeDimension.x + ((ground.getWidth() - coin.getWidth()) / 2) + ground.getWidth() * i, Constants.FIRST_ROW));
                coins.add(coin);
                stage.addActor(coin);
            }
            else {
                Coin coin = new Coin(ground,new Vector2());
                coin.localPosition.set(new Vector2(ground.edgeDimension.x + ((ground.getWidth() - coin.getWidth()) / 2) + ground.getWidth() * (i-ground.lenght+secondOffset) + coin.getWidth(), Constants.FIRST_ROW + Constants.DISTANCE_ROW));
                coins.add(coin);
                stage.addActor(coin);
            }
        }
    }


    private void checkDestroy(){
        for (Ground ground:grounds) {
            if(ground.getX() + (ground.getWidth() * ground.lenght + ground.edgeDimension.x * 2) <= 0){
                reBuildGround(ground);
            }
        }
        for (Coin coin: coins) {
            if(coin.getX() + coin.getWidth() <= 0) {
                coins.removeValue(coin, true);
                stage.getActors().removeValue(coin,true);
            }
        }for (Spikes spikes: spikeses) {
            if(spikes.getX() + spikes.getWidth() <= 0) {
                spikeses.removeValue(spikes, true);
                stage.getActors().removeValue(spikes,true);
            }
        }
    }

    public void act(float delta){
        checkDestroy();
    }
}
