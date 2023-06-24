package com.example.face_recoginization_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.face_recoginization_ui.Custome_Adapters.all_faces_viewpager2_adapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class all_faces_java extends AppCompatActivity
{
    int OUTPUT_SIZE=192;
    Context context= all_faces_java.this;
    private TabLayout tabLayout;
    private Button btn_delete_faces;
    private ViewPager2 viewPager2;
    private all_faces_viewpager2_adapter adapter;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private HashMap<String, SimilarityClassifier.Recognition> registered = new HashMap<>();

    @Override
    protected void onStart()
    {
        super.onStart();
        if(user == null)
        {
            startActivity(new Intent(all_faces_java.this,login_page_java.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        registered=readFromSP();
        setContentView(R.layout.all_faces_layout);

        tabLayout = findViewById(R.id.all_faces_tablayout1);
        viewPager2 = findViewById(R.id.all_faces_viewPager1);
        btn_delete_faces = findViewById(R.id.btn_delete_faces);

        adapter = new all_faces_viewpager2_adapter(this);
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager2.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 1)
                    btn_delete_faces.setVisibility(View.INVISIBLE);
                else
                    btn_delete_faces.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
        {
            @Override
            public void onPageSelected(int position)
            {
                super.onPageSelected(position);
                Objects.requireNonNull(tabLayout.getTabAt(position)).select();
            }
        });

        btn_delete_faces.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //displaynameListview();
                updatenameListview();
            }
        });
    }

    private void updatenameListview()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(registered.isEmpty()) {
            builder.setTitle("No Faces Added!!");
            builder.setPositiveButton("OK",null);
        }
        else{
            builder.setTitle("Select Faces to delete:");

            // add a checkbox list
            String[] names= new String[registered.size()];
            boolean[] checkedItems = new boolean[registered.size()];
            int i=0;
            for (Map.Entry<String, SimilarityClassifier.Recognition> entry : registered.entrySet())
            {
                //System.out.println("NAME"+entry.getKey());
                names[i]=entry.getKey();
                checkedItems[i]=false;
                i=i+1;

            }

            builder.setMultiChoiceItems(names, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    // user checked or unchecked a box
                    //Toast.makeText(MainActivity.this, names[which], Toast.LENGTH_SHORT).show();
                    checkedItems[which]=isChecked;

                }
            });

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                    // System.out.println("status:"+ Arrays.toString(checkedItems));
                    for(int i=0;i<checkedItems.length;i++)
                    {
                        //System.out.println("status:"+checkedItems[i]);
                        if(checkedItems[i])
                        {
                            //Toast.makeText(context, names[i], Toast.LENGTH_SHORT).show();
                            registered.remove(names[i]);
                            delete_owner_image_from_firebase(names[i]);
                        }
                    }
                    insertToSP(registered,2); //mode: 0:save all, 1:clear all, 2:update all
                }
            });
            builder.setNegativeButton("Cancel", null);

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    private void insertToSP(HashMap<String, SimilarityClassifier.Recognition> jsonMap,int mode)
    {
        if(mode==1)  //mode: 0:save all, 1:clear all, 2:update all
            jsonMap.clear();
        else if (mode==0)
            jsonMap.putAll(readFromSP());
        String jsonString = new Gson().toJson(jsonMap);
//        for (Map.Entry<String, SimilarityClassifier.Recognition> entry : jsonMap.entrySet())
//        {
//            System.out.println("Entry Input "+entry.getKey()+" "+  entry.getValue().getExtra());
//        }

        String folderName = user.getEmail();
        String username = folderName.substring(0,folderName.indexOf("@"));

        SharedPreferences sharedPreferences = getSharedPreferences(username, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("map", jsonString);
        //System.out.println("Input josn"+jsonString.toString());
        editor.apply();
        //Toast.makeText(context, "Recognitions Saved", Toast.LENGTH_SHORT).show();
    }
    private HashMap<String, SimilarityClassifier.Recognition> readFromSP()
    {
        String folderName = user.getEmail();
        String username = folderName.substring(0,folderName.indexOf("@"));

        SharedPreferences sharedPreferences = getSharedPreferences(username, MODE_PRIVATE);

        String defValue = new Gson().toJson(new HashMap<String, SimilarityClassifier.Recognition>());
        String json=sharedPreferences.getString("map",defValue);
        // System.out.println("Output json"+json.toString());
        TypeToken<HashMap<String,SimilarityClassifier.Recognition>> token = new TypeToken<HashMap<String,SimilarityClassifier.Recognition>>() {};
        HashMap<String,SimilarityClassifier.Recognition> retrievedMap=new Gson().fromJson(json,token.getType());
        // System.out.println("Output map"+retrievedMap.toString());

        //During type conversion and save/load procedure,format changes(eg float converted to double).
        //So embeddings need to be extracted from it in required format(eg.double to float).
        for (Map.Entry<String, SimilarityClassifier.Recognition> entry : retrievedMap.entrySet())
        {
            float[][] output=new float[1][OUTPUT_SIZE];
            ArrayList arrayList= (ArrayList) entry.getValue().getExtra();
            arrayList = (ArrayList) arrayList.get(0);
            for (int counter = 0; counter < arrayList.size(); counter++) {
                output[0][counter]= ((Double) arrayList.get(counter)).floatValue();
            }
            entry.getValue().setExtra(output);

            //System.out.println("Entry output "+entry.getKey()+" "+entry.getValue().getExtra() );

        }
//        System.out.println("OUTPUT"+ Arrays.deepToString(outut));
       // Toast.makeText(context, "Recognitions Loaded", Toast.LENGTH_SHORT).show();
        return retrievedMap;
    }
    private void delete_owner_image_from_firebase(String image_names)
    {
        String objectName = image_names;
        String folderName = user.getEmail();
        String username = folderName.substring(0,folderName.indexOf("@"));

        //Realtime db entry delete
        databaseReference = FirebaseDatabase.getInstance().getReference("Profiles");
        //loop is done by the method which says:"I am the one who calls and loops apparently."
        databaseReference.child(username).child(objectName).removeValue();

        //Firebase Storage entry delete
        storageReference = FirebaseStorage.getInstance().getReference("Profiles/"+folderName+"/"+objectName);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void unused)
            {
                //Toast.makeText(context, "Deletion success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                //Toast.makeText(context, "Deletion Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}