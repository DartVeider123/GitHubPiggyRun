package com.libgdx.piggyrun.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class AbstractGameObject extends Actor {
    public Vector2 gravitate;
    public Vector2 friction;
    public Vector2 velocity;
    public Vector2 terminalVelocity;
    public Rectangle rigidBody;
    public float stateTime;
    public Animation animation;
    private final TextureRegion region;

    public AbstractGameObject(TextureRegion region){
        velocity = new Vector2();
        terminalVelocity = new Vector2(1,1);
        friction = new Vector2();
        gravitate = new Vector2();
        rigidBody = new Rectangle();
        this.region = region;
    }

    public void setAnimation(Animation animation){
        stateTime = 0;
        this.animation = animation;
    }

    public void updateMotionY(float deltaTime){
        if(velocity.y != 0){
            if(velocity.y > 0){
                velocity.y = Math.max(velocity.y - friction.y * deltaTime,0);
            }else {
                velocity.y = Math.min(velocity.y + friction.y * deltaTime,0);
            }
        }
        velocity.y += gravitate.y * deltaTime;
        velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, +terminalVelocity.y);
    }

    public void updateMotionX(float deltaTime){
        if(velocity.x != 0){
            if(velocity.x > 0){
                velocity.x = Math.max(velocity.x - friction.x * deltaTime,0);
            }
        }else{
            velocity.x = Math.min(velocity.x + friction.x * deltaTime,0);
        }
        velocity.x += gravitate.x * deltaTime;
        velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        updateMotionX(delta);
        updateMotionY(delta);
        moveBy(velocity.x * delta, velocity.y * delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(region.getTexture(),getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),
                getRotation(),region.getRegionX(),region.getRegionY(),region.getRegionWidth(),region.getRegionHeight(),false,false);
    }
}
