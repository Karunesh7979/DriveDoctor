package com.csis.drivedoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.csis.drivedoctor.preauthDatabase.DatabaseHelper;
import com.csis.drivedoctor.Helpers.ModelHelper;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DatabaseHelper.applicationContext = getApplicationContext();
        ModelHelper.applicationContext = getApplicationContext();

        String userId = DatabaseHelper.getInstance().getPref().getString("userId","");
        System.out.println("Splash Userid-->>"+ userId);
        ModelHelper.getInstance();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String usrid = ModelHelper.getInstance().getUser().getUserId();
                    if ( usrid!= null && !usrid.isEmpty()) {
                        finish();
                        startActivity(new Intent(SplashActivity.this, NavigationMainActivity.class));
                    } else {
                        finish();
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }

                } catch (Exception e) {
                    finish();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }
        };
        Timer timr = new Timer();
        timr.schedule(task, 5000);
    }

}