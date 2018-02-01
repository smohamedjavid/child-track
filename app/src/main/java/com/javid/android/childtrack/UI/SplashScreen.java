package com.javid.android.childtrack.UI;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.javid.android.childtrack.R;

public class SplashScreen extends AppCompatActivity {
    String TAG = SplashScreen.class.getSimpleName();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = this;
        new CountDownTimer(2000,1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(context,HomeActivity.class));
            }
        }.start();
    }
}
