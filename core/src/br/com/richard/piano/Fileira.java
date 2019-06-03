package br.com.richard.piano;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


import static br.com.richard.piano.Constants.*;

public class Fileira {

    public float y;
    private int correta; // 0 a 3
    private int pos;

    private boolean ok;
    private boolean dest;
    private float anim;

    public Fileira(float y, int correta){
        this.y = y;
        this.correta = correta;
        ok = false;
        dest = false;
        anim = 0;
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(verde);
        shapeRenderer.rect(correta * tileWidth, y, tileWidth, tileHeight);

        if(dest){
            if(ok){
                shapeRenderer.setColor(certo);
            } else {
                shapeRenderer.setColor(errado);
            }

            shapeRenderer.rect(pos * tileWidth + (tileWidth - anim  * tileWidth)/2f,
                                y + (tileHeight - anim  * tileHeight)/2f,
                                anim * tileWidth, anim * tileHeight);
        }

        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GRAY);

        for(int i = 0; i <=3; i++){
            shapeRenderer.rect(i * tileWidth, y, tileWidth, tileHeight);
        }
    }

    public void anim(float delta){
        if(dest && anim < 1){
            anim += 5 * delta;
            if(anim >=  1){
                anim = 1;
            }
        }
    }

    public int update(float time){
        y -= time*velAtual;
        if(y < 0  - tileHeight){
            if(ok){
                return 1;
            } else {
                erro();
                return 2;
            }
        }
        return 0;
    }

    public int toque(int tx, int ty) {
        if(ty >= y && ty <= y + tileHeight){
            pos = tx / tileWidth;
            if(pos == correta){
                ok = true;
                dest = true;
                return 1;
            } else {
                dest = true;
                ok = false;
                return 2;
            }
        }
        return 0;
    }

    public void erro(){
        dest = true;
        pos = correta;
    }
}
