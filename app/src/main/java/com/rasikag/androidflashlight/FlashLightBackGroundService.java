package com.rasikag.androidflashlight;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.Camera;
import android.util.Log;

/**
 * Created by Rasika Gayan on 3/17/2016.
 */
public class FlashLightBackGroundService extends IntentService {

    private static Camera mCamera;
    private static Camera.Parameters mParams;

    public static final String ACTION_TURN_ON = "com.rasikag.androidflashlight.TURN_ON";
    public static final String ACTION_TURN_OFF = "com.rasikag.androidflashlight.TURN_OFF";

    public FlashLightBackGroundService() {
        super("FlashLightBackGroundService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getCamera();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent.getAction().equals(ACTION_TURN_ON)) {
            turnOnFlash();
        } else if (intent.getAction().equals(ACTION_TURN_OFF)) {
            turnOffFlash();
        }

    }

    private void getCamera() {

        if (mCamera == null) {
            try {
                mCamera = Camera.open();
                mParams = mCamera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Failed to Open. Error: ", e.getMessage());
            }
        }
    }

    private void turnOnFlash() {

        if (mCamera == null || mParams == null) {
            return;
        }
        mParams.setFlashMode(mParams.FLASH_MODE_TORCH);
        mCamera.setParameters(mParams);
        mCamera.startPreview();

    }

    private void turnOffFlash() {

        if (mCamera == null || mParams == null) {
            return;
        }
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;

    }

}
