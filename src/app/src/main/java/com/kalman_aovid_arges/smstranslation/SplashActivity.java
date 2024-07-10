package com.kalman_aovid_arges.smstranslation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        // Hide the ActionBar
        if (getActionBar() != null) {
            getActionBar().hide();
        }

        setContentView(R.layout.activity_splash); // Set the layout for the splash screen

        int SPLASH_DISPLAY_LENGTH = 5000;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the InboxActivity. */
                Intent mainIntent = new Intent(SplashActivity.this, InboxActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}