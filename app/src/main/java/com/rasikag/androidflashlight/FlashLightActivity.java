package com.rasikag.androidflashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class FlashLightActivity extends AppCompatActivity {

    private ImageButton mSwich;
    private boolean isFlashOn;
    private boolean isDeviceHasFlas;

    private MediaPlayer mMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_light);


        mSwich = (ImageButton) findViewById(R.id.btnSwitch);
        mSwich.setImageResource(R.drawable.btn_switch_off);
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

        isFlashOn = false;

        mSwich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FlashLightBackGroundService.class);
                if (isFlashOn) {
                    i.setAction(FlashLightBackGroundService.ACTION_TURN_OFF);
                    startService(i);
                    toggleButtonImage();
                    isFlashOn = false;
                } else {
                    toggleButtonImage();
                    i.setAction(FlashLightBackGroundService.ACTION_TURN_ON);
                    startService(i);
                    isFlashOn = true;
                }
            }
        });

    }


    private void toggleButtonImage() {
        if (isFlashOn) {
            mSwich.setImageResource(R.drawable.btn_switch_off);
            playSound();
        } else {
            mSwich.setImageResource(R.drawable.btn_switch_on);
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


}
