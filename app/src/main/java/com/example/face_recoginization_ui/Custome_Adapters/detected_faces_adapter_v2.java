package com.example.face_recoginization_ui.Custome_Adapters;


import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.face_recoginization_ui.Custome_Model_Classes.detected_faces_model;
import com.example.face_recoginization_ui.R;

import java.io.File;
import java.util.ArrayList;

public class detected_faces_adapter_v2 extends RecyclerView.Adapter<detected_faces_adapter_v2.myviewholder>
{
    private ArrayList<detected_faces_model> detected_faces_stranger_dataholder1;
    private detected_faces_adapter_v2 adapter;

    public detected_faces_adapter_v2(ArrayList<detected_faces_model> detected_faces_stranger_dataholder1)
    {
        this.detected_faces_stranger_dataholder1 = detected_faces_stranger_dataholder1;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylerview2_stranger_faces_row_layout,parent,false);
        return new detected_faces_adapter_v2.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.stranger_name.setText(detected_faces_stranger_dataholder1.get(position).getStranger_name());
        holder.stranger_captured_date.setText(detected_faces_stranger_dataholder1.get(position).getStranger_captured_date());
        holder.stranger_img.setImageURI(detected_faces_stranger_dataholder1.get(position).getStranger_image());

        holder.delete_item.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v)
            {
                //show msg
                //Toast.makeText(holder.delete_item.getContext(),detected_faces_stranger_dataholder1.get(position).getStranger_name()+" Deleted", Toast.LENGTH_SHORT).show();

                //remove image from storage
                Uri uri = detected_faces_stranger_dataholder1.get(position).getStranger_image();
                String path = uri.getPath();
                File f = new File(path);
                f.delete();

                //remove from recview arrlist
                detected_faces_stranger_dataholder1.remove(detected_faces_stranger_dataholder1.get(position));

                //refresh recview
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return  detected_faces_stranger_dataholder1.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView stranger_img;
        TextView stranger_name,stranger_captured_date;
        Button delete_item;

        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            stranger_img = itemView.findViewById(R.id.stranger_face);
            stranger_name = itemView.findViewById(R.id.txt_stranger_name);
            stranger_captured_date = itemView.findViewById(R.id.txt_stranger_captured_date);

            delete_item = itemView.findViewById(R.id.btn_delete_items);
        }
    }
}

