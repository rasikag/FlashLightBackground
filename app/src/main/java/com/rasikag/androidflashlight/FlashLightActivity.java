package com.rasikag.androidflashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class FlashLightActivity extends AppCompatActivity {

    private ImageButton mSwich;
    private boolean isFlashOn;
    private boolean isDeviceHasFlas;


    private Camera mCamera;
    private Camera.Parameters mParams;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_light);

        mSwich = (ImageButton) findViewById(R.id.btnSwitch);

        isDeviceHasFlas = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isDeviceHasFlas) {
            AlertDialog alert = new AlertDialog.Builder(FlashLightActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
            return;
        }

        getCamera();

        isFlashOn = false;

        toggleButtonImage();

        mSwich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    turnOffFlash();
                } else {
                    turnOnFlash();
                }
            }
        });

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

        if (!isFlashOn) {
            if (mCamera == null || mParams == null) {
                return;
            }
            mParams = mCamera.getParameters();
            mParams.setFlashMode(mParams.FLASH_MODE_TORCH);
            mCamera.setParameters(mParams);
            mCamera.startPreview();
            isFlashOn = true;

            toggleButtonImage();
        }

    }

    private void turnOffFlash() {
        if (isFlashOn) {
            if (mCamera == null || mParams == null) {
                return;
            }
            mParams = mCamera.getParameters();
            mParams.setFlashMode(mParams.FLASH_MODE_OFF);
            mCamera.setParameters(mParams);
            mCamera.stopPreview();
            isFlashOn = false;

            toggleButtonImage();
        }
    }

    private void toggleButtonImage() {
        if (isFlashOn) {
            mSwich.setImageResource(R.drawable.btn_switch_on);
            playSound();
        } else {
            mSwich.setImageResource(R.drawable.btn_switch_off);
            playSound();
        }
    }

    private void playSound() {

        if (isFlashOn) {
            mMediaPlayer = MediaPlayer.create(FlashLightActivity.this, R.raw.light_switch_off);
        } else {
            mMediaPlayer = MediaPlayer.create(FlashLightActivity.this, R.raw.light_switch_on);
        }
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }

        });
        mMediaPlayer.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (isDeviceHasFlas) {
//            turnOnFlash();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

}
