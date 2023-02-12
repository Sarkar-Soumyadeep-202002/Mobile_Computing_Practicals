package au.edu.jcu.spiritlevel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager manager;
    private Sensor gravitySensor;
    private SpiritLevelView spiritLevelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        for(Sensor sensor: deviceSensors)
            Log.i("MainActivity", sensor.getName());

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = manager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        spiritLevelView = findViewById(R.id.spiritLevelView);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final String[] axes = new String[] {"x", "y", "z"};
        final String format = "%s %.2f";

        StringBuilder message = new StringBuilder();
        for(int i = 0; i < axes.length; ++i)
            message.append(String.format(Locale.getDefault(), format, axes[i], sensorEvent.values[i]));

        Log.i("MainActivity", message.toString());

        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];

        spiritLevelView.setBubble(x,y);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        String message = String.format(Locale.getDefault(), "%s: %d", sensor.getName(), accuracy);
        Log.i("MainActivity", message);
    }

    @Override
    protected void onResume(){
        super.onResume();
        manager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        manager.unregisterListener(this);
    }

}
