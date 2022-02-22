package com.example.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView {
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    public List<Sprite> sprites = new ArrayList<Sprite>();
    public List<tempSprite> temps = new ArrayList<tempSprite>();
    public List<disparoSprite> disparos = new ArrayList<disparoSprite>();
    private long lastClick;
    public Bitmap bmpExplosion;
    private Bitmap bmpDisparo;
    private Bitmap bmp;
    private Bitmap bmpFinal;
    private Bitmap bmpWin;
    private Bitmap fondo;
    private int left;
    private Boolean muerto = false;
    private Boolean terminado = false;

    public GameView(Context context){
        super(context);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                createSprites();
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
        bmpExplosion = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
        bmpDisparo = BitmapFactory.decodeResource(getResources(), R.drawable.disparo);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.nave);
        bmpFinal = BitmapFactory.decodeResource(getResources(), R.drawable.game_over);
        bmpWin = BitmapFactory.decodeResource(getResources(), R.drawable.win);
        fondo = BitmapFactory.decodeResource(getResources(), R.drawable.fondo1);

        reloj();
    }

    public void reloj(){
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                for(int i = sprites.size() - 1; i >= 0; i--) {
                    sprites.remove(i);
                    muerto = true;
                    terminado = true;
                }
            }
        }.start();
    }

    public void nave(int left){
        this.left = left;
    }

    private void createSprites(){
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
    }

    private Sprite createSprite(int resource){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new Sprite(this, bmp);
    }

    @Override
    public void onDraw(Canvas canvas) {
        //canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(fondo, 0, 0, null);

        for(int i = temps.size() -1; i >= 0; i--){
            temps.get(i).onDraw(canvas);
        }

        for(Sprite sprite: sprites){
            sprite.onDraw(canvas);
        }

        for(int i = 0; i < disparos.size(); i++){
            disparos.get(i).onDraw(canvas);
        }

        if(sprites.size() == 0){
            if(muerto == false){
                canvas.drawBitmap(bmpWin, 0, 0, null);
                terminado = true;
            }
        }

        for(int i = sprites.size() - 1; i >= 0; i--){
            try{
                if(sprites.get(i).isCollition(left, canvas.getHeight()-150)){
                    for(int j = sprites.size() - 1; j >= 0; j--){
                        sprites.remove(j);
                    }
                    muerto = true;

                    MediaPlayer media = MediaPlayer.create(getContext(), R.raw.explosion_nave);
                    media.start();
                }else{
                    canvas.drawBitmap(bmp, left, canvas.getHeight()-150, null);
                }
            }catch (IndexOutOfBoundsException e){
                System.out.println(e);
            }
        }

        if(muerto == true){
            canvas.drawBitmap(bmpFinal, 0, 0, null);
            terminado = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(terminado == true){
            createSprites();
            terminado = false;
            muerto = false;
            reloj();
        }else{
            if(System.currentTimeMillis() - lastClick > 500){
                lastClick = System.currentTimeMillis();
                synchronized (getHolder()){
                    MediaPlayer media = MediaPlayer.create(getContext(), R.raw.disparo);
                    media.start();
                    disparos.add(new disparoSprite(this, left+30, bmpDisparo));
                }
            }
        }
        return true;
    }
}
