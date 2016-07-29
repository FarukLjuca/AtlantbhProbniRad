package com.atlantbh.rightway;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends BaseActivity implements
        SurfaceHolder.Callback,
        SensorEventListener {
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    ImageView arrow;

    private double locationLatitude, locationLongitude;
    private final double northPoleLatitude = 72.62, northPoleLongitude = 80.31;
    private double angleDifference;

    private Sensor acc;
    private Sensor mag;
    private SensorManager mSensorManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        surfaceView = (SurfaceView)findViewById(R.id.svCamera);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        arrow = (ImageView) findViewById(R.id.ivArrow);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        locationLatitude = getIntent().getExtras().getDouble("locationLatitude");
        locationLongitude = getIntent().getExtras().getDouble("locationLongitude");

        locationUpdated();
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    private void start_camera() {
        int cameraId;
        // do we have a camera?
        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
            cameraId = findBackFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(this, "No back facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                try {
                    camera = Camera.open(cameraId);
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.setDisplayOrientation(90);
                    camera.startPreview();
                } catch (Exception e) {
                    Log.e(this.getClass().getName(), "init_camera: " + e);
                    return;
                }
            }
        }
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    private void stop_camera() {
        try {
            camera.stopPreview();
            camera.release();
        } catch (RuntimeException ex) {
            Log.i(this.getClass().getName(), "Camera has already been released.");
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean hasSensor1 = mSensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
        boolean hasSensor2 = mSensorManager.registerListener(this, mag, SensorManager.SENSOR_DELAY_NORMAL);

        if (!hasSensor1 || !hasSensor2) Toast.makeText(this, "You do not have sensor on this device", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}

    public void surfaceCreated(SurfaceHolder holder) {
        start_camera();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stop_camera();
    }

    float[] mGravity;
    float[] mGeomagnetic;

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                if (userLatitude == 0) {
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        userLatitude = location.getLatitude();
                        userLongitude = location.getLongitude();
                    }
                }

                int animDuration = 60;

                if (userLatitude < locationLatitude) {
                    arrow.animate()
                            .rotation((-1) * (float) (Math.toDegrees(orientation[0] + angleDifference)) + 50)
                            .setDuration(animDuration)
                            .start();
                }
                else {
                    arrow.animate()
                            .rotation((-1) * (float) (Math.toDegrees(orientation[0] - angleDifference)) + 50)
                            .setDuration(animDuration)
                            .start();
                }

                arrow.animate().rotationX((-1) * (float) (Math.toDegrees(orientation[1]) / 1.2)).setDuration(animDuration).start();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void locationUpdated() {
        double a = calculateDistance(northPoleLatitude, northPoleLongitude, userLatitude, userLongitude);
        double b = calculateDistance(userLatitude, userLongitude, locationLatitude, locationLongitude);
        double c = calculateDistance(locationLatitude, locationLongitude, northPoleLatitude, northPoleLongitude);

        angleDifference = Math.acos((a * a + b * b - c * c) / (2 * a * b));
    }
}
