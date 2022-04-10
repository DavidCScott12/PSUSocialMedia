package com.example.psusocialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class schedule extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseUser user;
    ArrayList<String> allSections;
    ArrayList<String> allEmails;
    ArrayList<String> allSubjects;
    ArrayList<String> allClassNum;
    final long ONE_MEGABYTE = 1024 * 1024;
    int i;
    ArrayAdapter<String> arrayAdapter;
    ListView dynamicclasses;
    int sized;
    Button button_back;


    private static String[] convertToStrings(byte[][] byteStrings) {
        String[] data = new String[byteStrings.length];
        for (int i = 0; i < byteStrings.length; i++) {
            data[i] = new String(byteStrings[i], Charset.defaultCharset());

        }
        Log.d("convertToStrings: ", String.valueOf(data));
        return data;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        StorageReference userRef = storageRef.child("classes/"+user.getEmail());
        allSections = new ArrayList<String>();
        allEmails = new ArrayList<String>();
        allSubjects = new ArrayList<String>();
        allClassNum = new ArrayList<String>();

        button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(schedule.this, MainActivity.class);
                startActivity(intent);
            }
        });


        userRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                Log.d("schedule:","Success!1");
                for (StorageReference thing : listResult.getPrefixes())
                {
                    sized = listResult.getPrefixes().size();
                    thing.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            Log.d("schedule:","Success!2");

                            for (StorageReference item : listResult.getItems()){
                                item.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        String s =  new String(bytes, StandardCharsets.UTF_8);
                                        Log.d("Stuff", s);
                                        comp(s);
                                    }
                                });
                            }
                        }
                    });
                }//for
            }//onSuccess
        });//OnSuccsessListener

    }//onCreate
    private AdapterView.OnItemClickListener MshowforItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tvTemp = ((TextView) view);

            //tvTemp.setText(dataset[position]);

            arrayAdapter.notifyDataSetChanged();
        }
    };
    public void comp(String s){
        switch (s.substring(0,1)){
            case "E":
                allEmails.add(s.substring(1));
                break;
            case "U":
                allSubjects.add(s.substring(1));
                break;
            case "S":
                allSections.add(s.substring(1));
                break;
            case "C":
                allClassNum.add(s.substring(1));
                break;
        }
        if((allEmails.size() == sized) && (allSubjects.size() == sized) && (allClassNum.size() == sized) && (allSections.size() == sized)){
                String[] database = new String[sized];
                for(int i = 0; i < sized; i++){
                    database[i] = "Subject: " + allSubjects.get(i) + "\nClass: " + allClassNum.get(i) + "\nSection: " + allSections.get(i);
                }
                populate(database);
            }
        }

    public void populate(String[] database){ ;
            dynamicclasses = findViewById(R.id.partners);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, database);
            //arrayAdapter.add("Subject: "+allSubjects.get(arrl)+"\nClass: "+allClassNum.get(arrl)+"\nSection: "+allSections.get(arrl));
            dynamicclasses.setAdapter(arrayAdapter);
            dynamicclasses.setOnItemClickListener(MshowforItem);
    }
}//Class