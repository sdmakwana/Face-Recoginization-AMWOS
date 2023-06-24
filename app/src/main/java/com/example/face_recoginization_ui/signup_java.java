package com.example.face_recoginization_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.processing.SurfaceProcessorNode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup_java extends AppCompatActivity {

    private Button btn_continue_signup;
    private TextView txt_login1;
    private FirebaseAuth mAuth;

    private EditText signup_email,signup_pass;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        mAuth = FirebaseAuth.getInstance();

        btn_continue_signup = findViewById(R.id.btn_continue1);
        txt_login1 = findViewById(R.id.txt_login);

        signup_email = findViewById(R.id.edittxt_email);
        signup_pass = findViewById(R.id.edittxt_password);
        signup_pass.setHint(R.string.pass_in);


        btn_continue_signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                verify_user_inputs();
            }
        });

        txt_login1.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view)
            {
                txt_login1.setTextColor(R.color.gray);
                Intent intent_login = new Intent(signup_java.this,login_page_java.class);
                startActivity(intent_login);
            }
        });
    }

    private void verify_user_inputs()
    {
        email = signup_email.getText().toString();
        password = signup_pass.getText().toString();

        if(email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
        }
        else
        {
            register_user();
        }
    }

    private void register_user()
    {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(signup_java.this, "User Created", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    startActivity(new Intent(signup_java.this,login_page_java.class));
                }
                else
                {
                    Toast.makeText(signup_java.this, "ERROR:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                }
            }
        });
    }
}