package com.libgdx.piggyrun.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class GroundedObject extends AbstractGameObject {
    protected boolean isDestroyed;
    public Vector2 localPosition;
    public Ground currrentGround;

    public GroundedObject(Ground currrentGround, Vector2 localPosition, TextureRegion region) {
        super(region);
        this.currrentGround = currrentGround;
        this.localPosition = localPosition;
        isDestroyed = false;
        setPosition(currrentGround.getX() + localPosition.x,currrentGround.getY() + currrentGround.getHeight() + localPosition.y);
    }

    public void destroy(){
        isDestroyed = true;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    public void act(float delta) {
        if(!isDestroyed) {
            super.act(delta);
            setPosition(currrentGround.getX() + localPosition.x, currrentGround.getY() + currrentGround.getHeight() + localPosition.y);
        }
    }
}
