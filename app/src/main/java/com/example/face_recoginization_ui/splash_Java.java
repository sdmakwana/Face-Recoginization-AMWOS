package com.example.face_recoginization_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash_Java extends AppCompatActivity
{
    Handler handler1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        handler1 = new Handler();

        handler1.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(splash_Java.this, login_page_java.class);
                startActivity(intent);
                finish();
            }
        },2000);

    }
}