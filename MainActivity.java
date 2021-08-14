package com.hdone.proximity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    TextView txtview;
    private SensorManager mSensorManager;
    private Sensor mProximity;
    SensorEventListener listener;
    MediaPlayer mp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtview= findViewById(R.id.tv);
        mp= MediaPlayer.create(this, R.raw.alarm);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        listener = new SensorEventListener() {

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Toast.makeText(MainActivity.this, "accuracy changed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                txtview.setText(String.valueOf(event.values[0]));

                if (event.values[0] < mProximity.getMaximumRange()){
                    txtview.setText("Near");
                    mp.start();
                }
                else{
                    txtview.setText("Far");
                    if(mp.isPlaying())
                    mp.pause();
                }

            }
        };

        mSensorManager.registerListener(listener, mProximity, SensorManager.SENSOR_DELAY_FASTEST);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(listener, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(listener, mProximity);
    }
}