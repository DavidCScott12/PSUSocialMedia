package com.example.psusocialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class shareActivity extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseUser user;
    int sized;
    final long ONE_MEGABYTE = 1024 * 1024;
    ArrayList<String> allSections;
    ArrayList<String> allEmails;
    ArrayList<String> allSubjects;
    ArrayList<String> allClassNum;
    Hashtable<String, String[]> peoplesClasses;
    Button button_home, button_find;
    ArrayList<String> people;
    ArrayAdapter<String> arrayAdapter;
    ListView partners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        peoplesClasses = new Hashtable<String, String[]>();
        button_find = findViewById(R.id.button_find);
        button_home = findViewById(R.id.button_find);
        people = new ArrayList<>();
        partners = findViewById(R.id.partners);

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(shareActivity.this, MainActivity.class );
                startActivity(intent);
            }
        });

        AdapterView.OnItemClickListener MshowforItem = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvTemp = ((TextView) view);

                //tvTemp.setText(dataset[position]);

                arrayAdapter.notifyDataSetChanged();
            }
        };

        button_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enumeration<String> e = peoplesClasses.keys();
                while (e.hasMoreElements()) {
                    String key = e.nextElement();
                    for(int i = 0; i < peoplesClasses.get(key).length;i++) {
                        for(int j = 0; j < peoplesClasses.get(user.getEmail()).length ; j++) {
                            if (peoplesClasses.get(key)[i] == peoplesClasses.get(user.getEmail())[j] && !(peoplesClasses.get(key).equals(user.getEmail()))) {
                                people.add(key);
                            }
                        }
                    }
                }

                partners = findViewById(R.id.partners);
                String[] things = people.toArray(new String[people.size()]);
                partners.setAdapter(arrayAdapter);
                arrayAdapter = new ArrayAdapter<String>(shareActivity.this, android.R.layout.simple_list_item_1, things);
                partners.setOnItemClickListener(MshowforItem);

            }
        });

        StorageReference userRef1 = storageRef.child("classes");
        userRef1.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference thing : listResult.getPrefixes())
                {
                    getClasses(thing.getName());
                }
            }
        });
    }
    void getClasses(String email)
    {
        allSections = new ArrayList<String>();
        allEmails = new ArrayList<String>();
        allSubjects = new ArrayList<String>();
        allClassNum = new ArrayList<String>();
        StorageReference userRef = storageRef.child("classes/"+email);
        userRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference thing : listResult.getPrefixes())
                {
                    sized = listResult.getPrefixes().size();
                    thing.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {

                            for (StorageReference item : listResult.getItems()){
                                item.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        String s =  new String(bytes, StandardCharsets.UTF_8);
                                        sort(s);
                                    }
                                });
                            }
                        }
                    });
                }//for
            }//onSuccess
        });//OnSuccsessListener
    }
    void sort(String s) {

        switch (s.substring(0, 1)) {

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
        if ((allEmails.size() == sized) && (allSubjects.size() == sized) && (allClassNum.size() == sized) && (allSections.size() == sized)) {
            String[] database = new String[sized];
            for (int i = 0; i < sized; i++) {
                database[i] = "Subject: " + allSubjects.get(i) + "\nClass: " + allClassNum.get(i) + "\nSection: " + allSections.get(i);
            }
            peoplesClasses.put(allEmails.get(0), database);
        }
    }



}
