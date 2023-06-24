package com.example.face_recoginization_ui.Custome_Model_Classes;

public class saved_faces_model
{
    private String Image_URL;
    private String Name;

    public saved_faces_model()
    {
    }

    public saved_faces_model(String Image_URL,String Name)
    {
        this.Image_URL = Image_URL;
        this.Name = Name;
    }

    public String getImage_URL() {
        return Image_URL;
    }

    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }




}

