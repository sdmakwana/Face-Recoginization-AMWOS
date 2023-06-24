package com.example.face_recoginization_ui.Custome_Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.face_recoginization_ui.Fragements.detected_faces_fragement_java;
import com.example.face_recoginization_ui.Fragements.saved_faces_fragement_java;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class all_faces_viewpager2_adapter extends FragmentStateAdapter
{
    public all_faces_viewpager2_adapter(@NonNull FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        switch (position)
        {
            case 1:
                return new detected_faces_fragement_java();
            case 0:
            default: return new saved_faces_fragement_java();
        }
    }

    @Override
    public int getItemCount()
    {
        return 2;
    }
}
