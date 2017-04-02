package com.coffeeshot.toolbox.level;

import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.coffeeshot.toolbox.level.sensors.SensorTool;

public class LevelActivity extends AppCompatActivity
{

  private SensorTool mSensorTool;
  private LevelView mLevelView;

  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_level);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    mLevelView = (LevelView) findViewById(R.id.level_view);
    // Prefer a gravity sensor, as the accelerometer does not subtract
    // out movement of the phone. If the gravity sensor is not available,
    // Accelerometer is close enough.
    boolean hasGravity = SensorTool.deviceHasSensor(this, Sensor.TYPE_GRAVITY);
    int sensorType = hasGravity ? Sensor.TYPE_GRAVITY : Sensor.TYPE_ACCELEROMETER;
    mSensorTool = new SensorTool(this, sensorType)
    {
      public void onSensorUpdate(float[] values)
      {
        mLevelView.updateGravityValues(values);
      }
    };
    mSensorTool.start();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_level, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    Intent intent = null;
    int id = item.getItemId();
    if (id == R.id.action_about)
    {
      intent = SettingsActivity.getLaunchIntent(this, item.getTitle(), SettingsActivity.TYPE_ABOUT);
    }

    if (intent != null)
    {
      startActivity(intent);
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    mSensorTool.start();
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    mSensorTool.stop();
  }
}
