package com.libgdx.piggyrun.objects;

import com.badlogic.gdx.math.Vector2;
import com.libgdx.piggyrun.utils.Assets;

public class Spikes extends GroundedObject {

    public Spikes(Ground currrentGround, Vector2 localPosition) {
        super(currrentGround, localPosition,Assets.getInstance().spikesAsset.spikes);
        init();
    }

    private void init(){
        setSize(10.8f,3.18f);
        rigidBody.set(currrentGround.getX(),currrentGround.getY(),getWidth(),getHeight());
    }
}
