package com.example.face_recoginization_ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.video.internal.compat.quirk.VideoQualityQuirk;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class profile_page_java extends AppCompatActivity
{

    private TextView txt_profile_reset1, txt_user_email, txt_sign_out;
    private EditText txt_user_name;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onStart() {
        super.onStart();
        if (user == null) {
            startActivity(new Intent(profile_page_java.this, login_page_java.class));
            finish();
        }
    }

    @SuppressLint({"MissingPermission", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page_layout);

        txt_profile_reset1 = findViewById(R.id.txt_reset_pass_profile);
        txt_user_email = findViewById(R.id.txt_user_email);
        txt_sign_out = findViewById(R.id.txt_sign_out);
        txt_user_email.setText(user.getEmail());
        txt_user_name = findViewById(R.id.txt_user_name3);
        txt_user_name.setText(user.getDisplayName().toString());

        txt_user_name.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(txt_user_name.getText().toString()).build();
                user.updateProfile(profileUpdates);
            }
        });

        txt_profile_reset1.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view)
            {
                txt_profile_reset1.setTextColor(R.color.gray);
                startActivity(new Intent(profile_page_java.this,reset_password_java.class));
            }
        });

        txt_sign_out.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v)
            {
                txt_sign_out.setTextColor(R.color.gray);

                mAuth.signOut();
                startActivity(new Intent(profile_page_java.this,login_page_java.class));
                finish();
            }
        });
    }
}