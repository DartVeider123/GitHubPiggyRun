package com.libgdx.piggyrun.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.libgdx.piggyrun.utils.Assets;

public class Clouds {
    private Cloud lastCloud;
    public Array<Cloud> clouds;
    public boolean isStoped;

    public Clouds() {
        clouds = new Array<Cloud>();
        clouds.add(new Cloud(new Vector2(0,52 - MathUtils.random(0,10))));
        lastCloud = clouds.get(0);
        clouds.add(new Cloud(new Vector2(0,52 - MathUtils.random(0,10))));
        rebuildCloud(clouds.get(1));
        lastCloud = clouds.get(1);
        clouds.add(new Cloud(new Vector2(0,52 - MathUtils.random(0,10))));
        rebuildCloud(clouds.get(2));
        lastCloud = clouds.get(2);
        clouds.add(new Cloud(new Vector2(0,52 - MathUtils.random(0,10))));
        rebuildCloud(clouds.get(3));
        lastCloud = clouds.get(3);
        clouds.add(new Cloud(new Vector2(0,52 - MathUtils.random(0,10))));
        rebuildCloud(clouds.get(4));
        lastCloud = clouds.get(4);
        isStoped = false;
    }

    public class Cloud extends AbstractGameObject{
        public Cloud(Vector2 position){
            super(Assets.getInstance().cloudAsset.clouds.random());
            init(position);
        }

        public void init(Vector2 position){
            setBounds(position.x, position.y,21.56f,7f);
            terminalVelocity.set(35,0);
            gravitate.set(-0.3f,0);
            velocity.set(-10,0);
        }

        @Override
        public void act(float delta) {
            if(!isStoped)
            super.act(delta);
        }
    }

    private void rebuildCloud(Cloud cloud){
        float positionX = lastCloud.getX() + lastCloud.getWidth() + MathUtils.random(10,20);
        float positionY = MathUtils.random(52-lastCloud.getHeight(),57-lastCloud.getHeight());
        cloud.setPosition(positionX,positionY);
        lastCloud = cloud;
    }

    private void checkDestroy(){
        for (Cloud cloud:clouds){
            if(cloud.getX() + cloud.getWidth() <= 0){
                rebuildCloud(cloud);
            }
        }
    }

    public void act(float delta){
        checkDestroy();
    }
}
