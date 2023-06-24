package com.example.face_recoginization_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class home_page_java extends AppCompatActivity
{
    private Switch switch_secure_mode_toggle;
    private TextView txt_secure_mode_status,txt_secure_mode_on_description;
    private Button btn_all_faces,btn_add_face,btn_popup_menu1;
    private CardView cardView_previewView1;
    private ImageView camera_offline;

    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture1;
    PreviewView camera_previewView2;
    private ImageCapture imageCapture2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    @Override
    protected void onStart()
    {
        super.onStart();
        if(user == null)
        {
            startActivity(new Intent(home_page_java.this,login_page_java.class));
            finish();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_layout);
        get_camera_permission();

        switch_secure_mode_toggle = findViewById(R.id.switch_togggle_secure_mode);
        txt_secure_mode_status = findViewById(R.id.secure_mode);
        txt_secure_mode_on_description = findViewById(R.id.txt_secure_mode_description);
        btn_all_faces = findViewById(R.id.btn_all_face);
        btn_popup_menu1 = findViewById(R.id.btn_popup_menu);
        btn_add_face = findViewById(R.id.btn_add_face);
        cardView_previewView1 = findViewById(R.id.cardView_previewView);
        camera_offline = findViewById(R.id.img_camera_offline_dark);

        txt_secure_mode_on_description.setVisibility(View.INVISIBLE);
        camera_offline.setVisibility(View.VISIBLE);

        SharedPreferences sp = getSharedPreferences("SECURE_MODE_TOGGLE",MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        if(sp.getBoolean("secure_mode",true))
        {
            switch_secure_mode_toggle.setChecked(true);
            start_camera_with_ui();
        }
        else
        {
            switch_secure_mode_toggle.setChecked(false);
            stop_camera_with_ui();
        }

        switch_secure_mode_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                    if(isChecked)
                    {
                        ed.putBoolean("secure_mode",true);
                        ed.apply();
                        start_camera_with_ui();
                    }
                    else
                    {
                        ed.putBoolean("secure_mode",false);
                        ed.apply();
                        stop_camera_with_ui();
                    }
            }
        });

        btn_popup_menu1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu popupMenu = new PopupMenu(home_page_java.this,v);
                popupMenu.getMenuInflater().inflate(R.menu.home_dropdown_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.menu_profile:
                                startActivity(new Intent(home_page_java.this,profile_page_java.class));
                                return true;

                            case R.id.menu_contactus:
                                //Contactus
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        btn_all_faces.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent to_all_faces = new Intent(home_page_java.this,all_faces_java.class);
                startActivity(to_all_faces);
            }
        });

        btn_add_face.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent to_add_faces = new Intent(home_page_java.this,add_new_face_java.class);
                startActivity(to_add_faces);
            }
        });
    }

    private void start_camera_with_ui()
    {
        txt_secure_mode_status.setText("Secure mode On");
        txt_secure_mode_on_description.setVisibility(View.VISIBLE);
        camera_offline.setVisibility(View.INVISIBLE);

        camera_previewView2 = findViewById(R.id.camerax_previewView_home_monitor);

        cameraProviderListenableFuture1 = ProcessCameraProvider.getInstance(home_page_java.this);
        cameraProviderListenableFuture1.addListener(()->{
            try
            {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture1.get();
                startCameraX(cameraProvider);
            }
            catch (ExecutionException | InterruptedException e) { e.printStackTrace(); }
        },getExecutor());
    }

    private void startCameraX(ProcessCameraProvider cameraProvider)
    {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector1 = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        Preview preview1 = new Preview.Builder().build();

        preview1.setSurfaceProvider(camera_previewView2.getSurfaceProvider());

        //imageCapture2 = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();

        cameraProvider.bindToLifecycle((LifecycleOwner) this,cameraSelector1,preview1/*,imageCapture1*/);
    }

    private void stop_camera_with_ui()
    {
        txt_secure_mode_status.setText("Secure mode Off");
        txt_secure_mode_on_description.setVisibility(View.INVISIBLE);

        camera_previewView2 = findViewById(R.id.camerax_previewView_home_monitor);

        cameraProviderListenableFuture1 = ProcessCameraProvider.getInstance(home_page_java.this);
        cameraProviderListenableFuture1.addListener(()->{
            try
            {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture1.get();
                stopCameraX(cameraProvider);
            }
            catch (ExecutionException | InterruptedException e) { e.printStackTrace(); }
        },getExecutor());

        camera_offline.setVisibility(View.VISIBLE);
    }

    private void stopCameraX(ProcessCameraProvider cameraProvider)
    {
        cameraProvider.unbindAll();
    }

    private Executor getExecutor()
    {
        return ContextCompat.getMainExecutor(this);
    }

    private void get_camera_permission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
        }
    }
}