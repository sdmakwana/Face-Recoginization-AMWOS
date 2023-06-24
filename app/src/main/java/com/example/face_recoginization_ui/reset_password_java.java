package com.example.face_recoginization_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class reset_password_java extends AppCompatActivity {

    private EditText forgot_pass_email;
    private Button btn_pass_rst_continue;
    private String forgot_email;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_1_layout);

        btn_pass_rst_continue = findViewById(R.id.btn_pass_reset_otp_continue1);
        forgot_pass_email = findViewById(R.id.edittxt_enter_email_reset);

        if(user != null)
            forgot_pass_email.setText(user.getEmail());

        btn_pass_rst_continue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                verify_user_inputs();
                startActivity(new Intent(reset_password_java.this,login_page_java.class));
            }
        });
    }

    private void verify_user_inputs()
    {
        forgot_email = forgot_pass_email.getText().toString();
        if(forgot_email.isEmpty())
        {
            Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show();
        }
        else
        {
            reset_pass();
        }
    }
    private void reset_pass()
    {
        mAuth.sendPasswordResetEmail(forgot_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    startActivity(new Intent(reset_password_java.this,login_page_java.class));
                    Toast.makeText(reset_password_java.this, "Check your email to reset password.", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    finish();
                }
                else
                {
                    Toast.makeText(reset_password_java.this, "Error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}