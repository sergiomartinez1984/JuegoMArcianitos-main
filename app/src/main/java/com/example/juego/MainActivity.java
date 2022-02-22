package com.example.juego;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.widget.Toast;

import java.util.List;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor acelerometerSensor;

    private GameView gv;

    private int left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(gv = new GameView(this));

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> listaSensores;
        listaSensores = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (!listaSensores.isEmpty()) {
            acelerometerSensor = listaSensores.get(0);
            sensorManager.registerListener(this, acelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        synchronized (this) {
            switch(event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    if(Math.round(event.values[0]) >= 2){
                        if(left <= 0){
                        }else{
                            left = left-15;
                            gv.nave(left);
                        }
                    }else if(Math.round(event.values[0]) <= -2){
                        if(left >= metrics.widthPixels-130){
                        }else{
                            left = left+15;
                            gv.nave(left);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}