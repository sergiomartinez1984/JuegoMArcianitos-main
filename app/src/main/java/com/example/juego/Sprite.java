package com.example.juego;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Sprite {
    int[] DIRECTION_TO_ANIMATION_MAP = {4, 3, 1, 0, 2};
    private static final int BMP_ROWS = 5;
    private static final int BMP_COLUMNS = 2;
    private static final int MAX_SPEED = 5;
    private GameView gameView;
    private Bitmap bmp;
    private int x = 0;
    private int y = 0;
    private int xSpeed;
    private int ySpeed;
    private int currentFrame = 0;
    private int width;
    private int height;


    public Sprite(GameView gameView, Bitmap bmp){
        this.gameView = gameView;
        this.bmp = bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        Random rnd = new Random(System.currentTimeMillis());
        xSpeed = rnd.nextInt(10)-5;
        ySpeed = rnd.nextInt(10)-5;
        x = rnd.nextInt(gameView.getWidth() - width);
        y = rnd.nextInt(gameView.getHeight() - height - 200);
    }

    private void update(){
        if(x > gameView.getWidth() - width - xSpeed || x + xSpeed < 0){
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;
        if(y > gameView.getHeight() - height - ySpeed || y + ySpeed < 0){
            ySpeed = -ySpeed;
        }
        y = y + ySpeed;
        currentFrame = ++currentFrame % BMP_COLUMNS;
    }

    protected void onDraw(Canvas canvas){
        update();
        int srcX = currentFrame * width;
        int srcY = 0 * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        //Rect dst = new Rect(20, 20, width+20, height+20);
        Rect dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmp, src, dst, null);
    }

    private int getAnimationRow(){
        double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        System.out.println("Hola mundo "+direction);
        return DIRECTION_TO_ANIMATION_MAP[direction];
    }

    public boolean isCollition(float x2, float y2){
        return x2 > x && x2 < x + width && y2 > y && y2 < y +height;
    }
}
