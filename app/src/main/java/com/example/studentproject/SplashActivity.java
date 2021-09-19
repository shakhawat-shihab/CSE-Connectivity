package com.example.studentproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences SP;
    private SharedPreferences.Editor editor;
    private ProgressBar PB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        PB=findViewById(R.id.progress_id);
        SP=getSharedPreferences("User", Context.MODE_PRIVATE);
        editor=SP.edit();
        Thread T=new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                startApp();
            }
        });
        T.start();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void doWork()
    {
        for(int i=20;i<=100;i+=10)
        {
            if(i<70)
            {
                try {
                    Thread.sleep(80);
                    PB.setProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    Thread.sleep(200);
                    PB.setProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    public void startApp()
    {
        String state=SP.getString("user_state","not found!");
        //(user_state=="1") -> Go to HomeActivity
        if (state.equals("1"))
        {
            finish();
            Intent intent =new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else
        {
            finish();
            Intent I=new Intent(SplashActivity.this,MainActivity.class);
            startActivity(I);
            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }
}
