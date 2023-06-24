package com.example.face_recoginization_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class add_new_face_java extends AppCompatActivity
{
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture1;
    PreviewView camera_previewView1;
    private ImageCapture imageCapture1;
    Button btn_capture,btn_gallery;
    private static final int REQUEST_WRITE_PERMISSION_CODE = 786;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onStart()
    {
        super.onStart();
        if(user == null)
        {
            startActivity(new Intent(add_new_face_java.this,login_page_java.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_face_layout);
        get_read_write_permission();

        camera_previewView1 = findViewById(R.id.camerax_previewView_add_face);
        btn_capture = findViewById(R.id.btn_capture_image);
        btn_gallery = findViewById(R.id.btn_set_image_from_gallery);

        cameraProviderListenableFuture1 = ProcessCameraProvider.getInstance(this);
        cameraProviderListenableFuture1.addListener(()->{
            try
            {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture1.get();
                startCameraX(cameraProvider);
            }
            catch (ExecutionException | InterruptedException e) { e.printStackTrace();}
        },getExecutor());

        btn_capture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                capture_image();
            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //startActivity(new Intent(add_new_face_java.this, image_upload_java.class));
            }
        });
    }

    private Executor getExecutor()
    {
        return ContextCompat.getMainExecutor(this);
    }

    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider)
    {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector1 = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();

        Preview preview1 = new Preview.Builder().build();

        preview1.setSurfaceProvider(camera_previewView1.getSurfaceProvider());

        imageCapture1 = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();

        cameraProvider.bindToLifecycle((LifecycleOwner) this,cameraSelector1,preview1,imageCapture1);
    }

    private void capture_image()
    {

        SharedPreferences sp1 = getSharedPreferences("Stranger_Images_no_add_new_face",MODE_PRIVATE);
        SharedPreferences.Editor ed1 = sp1.edit();

        int stranger_index_var = sp1.getInt("Stranger_index",0);
        stranger_index_var++;
        ed1.putInt("Stranger_index",stranger_index_var);
        ed1.apply();

        String fileName = "Stranger_Image "+stranger_index_var;

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/SafeGuard/Stranger_Images");

        imageCapture1.takePicture(new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults)
            {
                Toast.makeText(add_new_face_java.this, "photo SAVED", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(@NonNull ImageCaptureException exception)
            {
                Toast.makeText(add_new_face_java.this, "ERROR in SAVING photo", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void get_read_write_permission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION_CODE);
            }
        }
    }
}