package com.example.zjjc1.pushupcounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView textView;
    private SensorManager sensorManager;
    private Sensor sensor;

    private View view;

    private TextView totalText;
    private TextView counter;

    private float downValue = 2.0f;

    private int count = 0;

    private int total;
    SharedPreferences totalCount;

    private boolean downState = false;

    private int downColor = Color.GREEN;
    private int upColor = Color.DKGRAY;

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalCount = getSharedPreferences(PREFS_NAME,0);


        textView = findViewById(R.id.textView);
        counter = findViewById(R.id.counter);
        totalText = findViewById(R.id.total);


        view = this.getWindow().getDecorView();
        view.setBackgroundColor(upColor);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        total = totalCount.getInt("total", total);


        counter.setText(Integer.toString(count));
        totalText.setText("Total " + Integer.toString(total));
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY){
            if(event.values[0] < downValue){
                pushUpDown();
            } else {
                pushUpUp();
            }

            textView.setText(Float.toString(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }


    public void pushUpDown(){
        if (!downState){
            downState = true;
            view.setBackgroundColor(downColor);

            count++;
            counter.setText(Integer.toString(count));

            total++;
            SharedPreferences.Editor editor = totalCount.edit();
            editor.putInt("total", total);
            editor.commit();

            totalText.setText("Total " + Integer.toString(total));

        }
    }

    public void pushUpUp(){
        if (downState){
            downState = false;
            view.setBackgroundColor(upColor);
        }
    }
}
