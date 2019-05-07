package com.libgdx.piggyrun.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.libgdx.piggyrun.utils.Assets;

public class Ground extends AbstractGameObject {
    private Array<TextureAtlas.AtlasRegion> imagesCenter;
    private Array<TextureAtlas.AtlasRegion> imagesEdge;
    public Vector2 edgeDimension;
    public int lenght;

    public Ground(Vector2 position, int lenght, Vector2 velocity) {
        super(null);
        reBuild(position,lenght,velocity);
    }

    public void reBuild(Vector2 position, int lenght, Vector2 velociy){
        this.lenght = lenght;
        this.velocity.set(velociy);
        setBounds(position.x,position.y,10.8f,12.0f);
        edgeDimension = new Vector2(6f,10.8f);
        setOrigin(getWidth() / 2, getHeight() / 2);
        terminalVelocity.set(50,0);
        gravitate.set(-0.3f,0);
        rigidBody.set(position.x,position.y,getWidth() * lenght + edgeDimension.x * 2,edgeDimension.y);
        imagesEdge = new Array<TextureAtlas.AtlasRegion>();
        imagesCenter = new Array<TextureAtlas.AtlasRegion>();
        for (int i = 0; i < lenght; i++) {
            imagesCenter.add(Assets.getInstance().groundAsset.ground_center.random());
        }
        for (int i = 0; i < 2; i++) {
            imagesEdge.add(Assets.getInstance().groundAsset.ground_edge.random());
        }
    }

    @Override
    public void act(float delta) {
        if(!GroundManager.isStoped)
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion reg = imagesEdge.get(0);
        float relX = 0;
        float edgeOffset = 0.6f;
        batch.draw(reg.getTexture(), getX(), getY() + edgeOffset, edgeDimension.x/2, edgeDimension.y/2,
                edgeDimension.x, edgeDimension.y, getScaleX(), getScaleY(), getRotation(), reg.getRegionX(),
                reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
        relX = edgeDimension.x;
        for (int i = 0; i < lenght; i++) {
            reg = imagesCenter.get(i);
            batch.draw(reg.getTexture(), getX() + relX, getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation(), reg.getRegionX(),
                    reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
            relX += getWidth();
        }
        reg = imagesEdge.get(1);
        batch.draw(reg.getTexture(), getX() + relX, getY() + edgeOffset, edgeDimension.x/2, edgeDimension.y/2,
                edgeDimension.x, edgeDimension.y, getScaleX(), getScaleY(), getRotation(), reg.getRegionX(),
                reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), true, false);
    }
}
