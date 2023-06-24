package com.example.face_recoginization_ui.Custome_Model_Classes;

import android.net.Uri;

public class detected_faces_model
{
    String stranger_name;
    String stranger_captured_date;
    Uri stranger_image;

    public detected_faces_model(String stranger_name, String stranger_captured_date, Uri stranger_image)
    {
        this.stranger_name = stranger_name;
        this.stranger_captured_date = stranger_captured_date;
        this.stranger_image = stranger_image;
    }

    public String getStranger_name()
    {
        return stranger_name;
    }

    public String getStranger_captured_date()
    {
        return stranger_captured_date;
    }

    public Uri getStranger_image()
    {
        return stranger_image;
    }
}