package com.libgdx.piggyrun.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.piggyrun.utils.Assets;

public class Coin extends GroundedObject {

    public Coin(Ground currrentGround, Vector2 localPosition) {
        super(currrentGround, localPosition, Assets.getInstance().coinAsset.coin);
        init();
    }

    private void init(){
        setSize(5.45f,5);
        rigidBody.set(getX(),getY(),getWidth()-1,getHeight()-1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(!isDestroyed)
        super.draw(batch, parentAlpha);
    }
}
