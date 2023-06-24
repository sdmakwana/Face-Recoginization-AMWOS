package com.example.face_recoginization_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login_page_java extends AppCompatActivity
{
    private TextView txt_forgotpass_reset1,warning_txt_view;
    private Button btn_login_continue;
    private ImageView warning_imageview;

    private FirebaseAuth mAuth;

    private TextView login_email,login_pass;
    private String email,password;

    @Override
    protected void onStart()
    {
        super.onStart();
        if(mAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(this,home_ver2_java.class));
            finish();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_layout);
        mAuth = FirebaseAuth.getInstance();

        txt_forgotpass_reset1 = findViewById(R.id.txt_reset_here);
        btn_login_continue = findViewById(R.id.btn_login_continue1);
        warning_txt_view = findViewById(R.id.warning_text);
        warning_imageview = findViewById(R.id.warning_img);

        login_email = findViewById(R.id.edittxt_login_email);
        login_pass = findViewById(R.id.edittxt_login_pass);
        login_pass.setHint(R.string.login_passsin);

        warning_txt_view.setVisibility(View.INVISIBLE);
        warning_imageview.setVisibility(View.INVISIBLE);

        btn_login_continue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                verify_user_inputs();
            }
        });

        txt_forgotpass_reset1.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view)
            {
                txt_forgotpass_reset1.setTextColor(R.color.gray);
                startActivity(new Intent(login_page_java.this,reset_password_java.class));
            }
        });

    }

    private void verify_user_inputs()
    {
        email = login_email.getText().toString();
        password = login_pass.getText().toString();

        if(email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
        }
        else
        {
            login_user();
        }
    }

    private void login_user()
    {
        mAuth.signInWithEmailAndPassword(login_email.getText().toString(),login_pass.getText().toString()).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(login_page_java.this, "Login sucess", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(login_page_java.this,home_ver2_java.class));
                    finish();
                }
                else
                {
                    Toast.makeText(login_page_java.this, "Error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}