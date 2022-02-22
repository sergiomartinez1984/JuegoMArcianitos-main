package com.example.juego;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;

import java.util.List;

public class disparoSprite {
    private int x;
    private int y;
    private Bitmap bmp;
    private int life = 15;
    private GameView gameView;
    private int ySpeed;

    public disparoSprite(GameView gameView, int x, Bitmap bmp){
        this.bmp = bmp;
        this.gameView = gameView;
        this.x = x;
        this.y = gameView.getHeight()-200;
        ySpeed = 20;
    }

    private void update(){
            if(y > gameView.getHeight() - ySpeed || y + ySpeed < 0){
                ySpeed = -ySpeed;
            }
            y = y - ySpeed;
    }

    public void onDraw(Canvas canvas){
        update();
        for(int i = gameView.sprites.size()-1; i >= 0; i--){
            Sprite s = gameView.sprites.get(i);
            if(s.isCollition(x, y)){
                gameView.sprites.remove(s);
                MediaPlayer media = MediaPlayer.create( gameView.getContext(), R.raw.explosion_enemigo);
                media.start();
                gameView.temps.add(new tempSprite(gameView.temps, gameView, x, y, gameView.bmpExplosion));
                for(int j = gameView.disparos.size()-1; j >= 0; j--) {
                    if(gameView.disparos.get(j).x == x && gameView.disparos.get(j).y == y){
                        gameView.disparos.remove(j);
                    }
                }
            }
        }
        for(int j = gameView.disparos.size()-1; j >= 0; j--) {
            if(gameView.disparos.get(j).y <= 0){
                gameView.disparos.remove(j);
            }
        }
        canvas.drawBitmap(bmp, x, y, null);
    }

    public boolean isCollition(float x2, float y2){
        return x2 > x && x2 < x + bmp.getWidth() && y2 > y && y2 < y + bmp.getHeight();
    }
}
