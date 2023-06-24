package com.example.face_recoginization_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class contact_us_java extends AppCompatActivity
{
    private EditText contactus_name,contactus_msg;
    private Button btn_send_contactus;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String name_of_user,message;

    @Override
    protected void onStart()
    {
        super.onStart();
        if (user == null)
        {
            startActivity(new Intent(contact_us_java.this, login_page_java.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us_layout);

        contactus_name = findViewById(R.id.edittxt_contactus_name);
        contactus_msg = findViewById(R.id.edittxt_contactus_message);
        btn_send_contactus = findViewById(R.id.btn_contactus_send);

        btn_send_contactus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                name_of_user = String.valueOf(contactus_name.getText());
                message = String.valueOf(contactus_msg.getText());

                String[] email_sendto = {"amwos12345@gmail.com"};
                Intent feedback_msg = new Intent(Intent.ACTION_SENDTO);
                feedback_msg.setData(Uri.parse("mailto:"));
                feedback_msg.putExtra(Intent.EXTRA_EMAIL, email_sendto);
                feedback_msg.putExtra(Intent.EXTRA_SUBJECT, name_of_user);
                feedback_msg.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(feedback_msg.createChooser(feedback_msg,"Send Email Using: "));
            }
        });

    }

}