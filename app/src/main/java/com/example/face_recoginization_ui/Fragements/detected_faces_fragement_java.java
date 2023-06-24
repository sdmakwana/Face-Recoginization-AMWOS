package com.example.face_recoginization_ui.Fragements;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.face_recoginization_ui.Custome_Adapters.detected_faces_adapter_v2;
import com.example.face_recoginization_ui.Custome_Model_Classes.detected_faces_model;
import com.example.face_recoginization_ui.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.ArrayList;

public class detected_faces_fragement_java extends Fragment
{
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    RecyclerView recyclerView_stranger_faces;
    ArrayList<detected_faces_model> detected_faces_stranger_dataholder = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.detected_faces_fragement_layout, container, false);
        recyclerView_stranger_faces = view.findViewById(R.id.recylerview1_stranger_faces1);
        recyclerView_stranger_faces.setLayoutManager(new LinearLayoutManager(getContext()));

        setup_stranger_faces_method();

        recyclerView_stranger_faces.setAdapter(new detected_faces_adapter_v2(detected_faces_stranger_dataholder));

        return view;
    }

    private void setup_stranger_faces_method()
    {
        String folderName = user.getEmail();
        String username = folderName.substring(0,folderName.indexOf("@"));

        String path_to_dir = Environment.getExternalStorageDirectory().toString()+"/DCIM/SafeGuard/Stranger_Images/"+username;
        File dir = new File(path_to_dir);
        if(dir.exists())
        {
            File[] files = dir.listFiles();
            int no_of_stranger_imgs= files.length;
            Uri stranger_images_v2[] = new Uri[no_of_stranger_imgs];
            String stranger_captured_dates[] = new String[no_of_stranger_imgs];
            String stranger_captured_dates_v2[] = new String[no_of_stranger_imgs];
            String stranger_captured_dates_v3[] = new String[no_of_stranger_imgs];
            String stranger_captured_dates_v4[] = new String[no_of_stranger_imgs];
            String stranger_names[] = new String[no_of_stranger_imgs];

            if (files != null)
            {
                //Stranger images array load
                for (int i = 0; i < no_of_stranger_imgs; i++)
                {
                    stranger_images_v2[i] = Uri.fromFile(files[i]);
                }
                //stranger_captured_dates array load and formatting
                for (int i = 0; i < no_of_stranger_imgs; i++)
                {
                    stranger_captured_dates[i] = files[i].getName();
                    //removing file extension
                    stranger_captured_dates_v2[i] = stranger_captured_dates[i].substring(0, stranger_captured_dates[i].lastIndexOf('.'));
                    //replacing - with /
                    stranger_captured_dates_v3[i] = stranger_captured_dates_v2[i].replace("-","/");
                    //replacing _ with :
                    stranger_captured_dates_v4[i] = stranger_captured_dates_v3[i].replace("_",":");
                }
                //stranger temp names creation
                int stranger_name_index = 1;
                for (int i = 0; i < no_of_stranger_imgs; i++)
                {
                    stranger_names[i] = "Stranger "+stranger_name_index;
                    stranger_name_index++;
                }
            }

            for (int i = 0; i < stranger_images_v2.length; i++)
            {
                detected_faces_model detected_faces_model_datamodel = new detected_faces_model(stranger_names[i], stranger_captured_dates_v4[i], stranger_images_v2[i]);
                detected_faces_stranger_dataholder.add(detected_faces_model_datamodel);
            }
        }
    }
}