package com.example.sensorapp;

import static java.lang.Math.abs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensorLight;
    private TextView sensorLightTextView;
    private TextView sensorLightValueView;
    private double _value;
    public SensorDetailsActivity() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sensorLight!=null){
            sensorManager.registerListener(this,sensorLight,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];
        _value = currentValue;

        switch(sensorType){
            case Sensor.TYPE_LIGHT:
                sensorLightTextView.setText(getResources().getString(R.string.Light_sensor_label, currentValue));
                break;
            case Sensor.TYPE_PROXIMITY:
                sensorLightTextView.setText(getResources().getString(R.string.Proximity_sensor_label, currentValue));
                break;
            default:
                break;
        }
        int x = (int) (sensorLight.getMaximumRange()-currentValue);
        int rgb = Math.min(abs(x)*100%255, 220);


        sensorLightTextView.setBackgroundColor(Color.rgb(rgb, 0, rgb));
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        System.out.println("onAccuracyChanged");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        sensorLightTextView = findViewById(R.id.sensor_Light_Label);
        sensorLightValueView = findViewById(R.id.sensor_Light_Value);
        int sensorType = getIntent().getIntExtra("sensorDetails", 0);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorLight = sensorManager.getDefaultSensor(sensorType);
        if(sensorLight==null){
            sensorLightTextView.setText(R.string.missing_sensors);
        }
        else {
            sensorLightTextView.setText(sensorLight.getName());
        }
    }
}