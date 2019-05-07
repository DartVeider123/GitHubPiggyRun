package com.libgdx.piggyrun.objects;

import com.badlogic.gdx.Gdx;
import com.libgdx.piggyrun.states.GameState;
import com.libgdx.piggyrun.utils.Assets;

public class Pig extends AbstractGameObject {
    public boolean onCollisionWithGround;
    private static float MIN_JUMP_TIME = 0.1f;
    private static float MAX_JUMP_TIME = 0.3f;
    public float jumpingTime;
    public JumpState jumpState;

    public Pig() {
        super(Assets.getInstance().piggyAsset.piggy);
        init();
    }

    private void init(){
        setBounds(18,24,12.155f,8.5f);
        setOrigin(getWidth()/2,getHeight()/2);
        terminalVelocity.set(0,100);
        gravitate.set(0,-200);
        rigidBody.set(getX(),getY(),getWidth(),getHeight());
        onCollisionWithGround = false;
        jumpState = JumpState.JUMP_FALLING;
    }

    private boolean jumpButtonTouched(){
        return GameState.isTouched;
    }

    public void setJumpState(boolean jumpButtonTouched){
        switch (jumpState){
            case JUMP_RISING:
                if(!jumpButtonTouched) {
                    jumpState = JumpState.JUMP_FALLING;
                }
                break;
            case JUMP_FALLING:
                if(jumpButtonTouched)
                    jumpState = JumpState.JUMP_RISING;
                break;
        }
    }

    @Override
    public void updateMotionY(float deltaTime) {
        if(!onCollisionWithGround) {
            gravitate.set(0,-200);
        }
        else {
            gravitate.set(0,0);
            velocity.set(0,0);
        }
        switch (jumpState){
            case JUMP_RISING:
                jumpingTime += deltaTime;
                if(jumpButtonTouched() && jumpingTime < MAX_JUMP_TIME){
                    velocity.set(0,40f);
                }else if(!jumpButtonTouched() && jumpingTime < MIN_JUMP_TIME){
                    velocity.set(0,40f);
                }else{
                    jumpState = JumpState.JUMP_FALLING;
                }
                break;
            case JUMP_FALLING:
                break;
        }
        super.updateMotionY(deltaTime);
    }
}
