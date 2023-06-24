package com.example.face_recoginization_ui.Custome_Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.face_recoginization_ui.R;
import com.example.face_recoginization_ui.Custome_Model_Classes.saved_faces_model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class saved_faces_adapter_v2 extends FirebaseRecyclerAdapter<saved_faces_model,saved_faces_adapter_v2.myviewholder>
{
    public saved_faces_adapter_v2(@NonNull FirebaseRecyclerOptions<saved_faces_model> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull saved_faces_model model)
    {
        holder.owner_name.setText(model.getName());
        Glide.with(holder.owner_img.getContext()).load(model.getImage_URL()).into(holder.owner_img);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylerview1_owner_faces_row_layout,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView owner_img;
        TextView owner_name;

        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            owner_img = (ImageView) itemView.findViewById(R.id.owner_face);
            owner_name = (TextView) itemView.findViewById(R.id.txt_owner_name);
        }
    }
}
