package com.example.face_recoginization_ui.Fragements;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.face_recoginization_ui.Custome_Adapters.saved_faces_adapter_v2;
import com.example.face_recoginization_ui.Custome_Model_Classes.saved_faces_model;
import com.example.face_recoginization_ui.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class saved_faces_fragement_java extends Fragment
{
    RecyclerView recyclerView_owner_faces;
    saved_faces_adapter_v2 adapter_v2;
//    ArrayList<saved_faces_model> saved_faces_owner_dataholder = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.saved_faces_fragement_layout, container, false);
        recyclerView_owner_faces = view.findViewById(R.id.recylerview1_owner_faces1);
        recyclerView_owner_faces.setLayoutManager(new LinearLayoutManager(getContext()));

        String folderName = user.getEmail();
        String username = folderName.substring(0,folderName.indexOf("@"));

        FirebaseRecyclerOptions<saved_faces_model> options =
                new FirebaseRecyclerOptions.Builder<saved_faces_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Profiles").child(username), saved_faces_model.class)
                        .build();


        adapter_v2 = new saved_faces_adapter_v2(options);

        recyclerView_owner_faces.setAdapter(adapter_v2);

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        adapter_v2.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        adapter_v2.stopListening();
    }

}