package com.example.sensorsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    Sensor light, accel, magnet, steps, gyro, idle, motion, temperature;
    TextView tv_T, tv_accel, tv_gyro, tv_light, tv_magnet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //1
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        steps = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        //2
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //3
        magnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        idle = sensorManager.getDefaultSensor(Sensor.TYPE_STATIONARY_DETECT);
        motion = sensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT);

        tv_accel = findViewById(R.id.tv_accel);
        tv_gyro = findViewById(R.id.tv_gyro);
        tv_light = findViewById(R.id.tv_light);
        tv_T = findViewById(R.id.tv_T);
        tv_magnet = findViewById(R.id.tv_magnet);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(idle!=null){
            sensorManager.registerListener(this,idle,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(motion!=null){
            sensorManager.registerListener(this,motion,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(temperature!=null){
            sensorManager.registerListener(this,temperature,SensorManager.SENSOR_DELAY_NORMAL);
        }
        //
        if(light!=null){
            sensorManager.registerListener(this,light,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(magnet!=null){
            sensorManager.registerListener(this,magnet,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(accel!=null){
            sensorManager.registerListener(this,accel,SensorManager.SENSOR_DELAY_NORMAL);
        }
        //
        if(gyro!=null){
            sensorManager.registerListener(this,gyro,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()){

            case Sensor.TYPE_STATIONARY_DETECT:
                showToast("мы лежим!");
                break;
            case Sensor.TYPE_MOTION_DETECT:
                showToast("мы движемся");

                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                tv_T.setText("T="+sensorEvent.values[0]);
                break;
            case Sensor.TYPE_LIGHT:
                tv_light.setText("Light:"+sensorEvent.values[0]);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                tv_magnet.setText(String.format("magnet:x=%.2f, y=%.2f, z=%.2f", sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2]));
                break;
            case Sensor.TYPE_ACCELEROMETER:
                tv_accel.setText(String.format("accelerometer:x=%.2f, y=%.2f, z=%.2f", sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2]));
                break;
            case Sensor.TYPE_GYROSCOPE:
                tv_gyro.setText(String.format("gyro:x=%.2f, y=%.2f, z=%.2f", sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2]));
                break;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    void showToast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

}
