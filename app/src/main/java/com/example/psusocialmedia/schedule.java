package com.example.psusocialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class schedule extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseUser user;
    String[] allClasses;
    final long ONE_MEGABYTE = 1024 * 1024;
    int count;
    int count2;
    ArrayAdapter<String> arrayAdapter;
    String[]  dataset;
    ListView dynamicclasses;

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
        count = 0;
        allClasses = new String[20];
        userRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                Log.d("schedule:","Success!1");
                for (StorageReference thing : listResult.getPrefixes())
                {
                    thing.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            Log.d("schedule:","Success!2");

                            for (StorageReference item : listResult.getItems()){
                                item.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        String s =  new String(bytes, StandardCharsets.UTF_8);
                                        allClasses[count] = s;
                                        Log.d("schedule:",allClasses[count]);
                                        count++;
                                    }
                                });
                                Log.d("schedule1:",allClasses[count]);
                            }
                        }
                    });
                    //Log.d("schedule1:",allClasses[count]);
                }//for
            }//onSuccess
        });//OnSuccsessListener
        Log.d("stuff", String.valueOf(allClasses[0]));
        dataset = new String[allClasses.length];
        for(int i = 1; i < allClasses.length/4; i++){
            //dataset[i] = "Subject: "+allClasses[i]+"\nClass: "+allClasses[i]+"\nSection: "+allClasses[i];
        }
        dynamicclasses = findViewById(R.id.dynamicclasses);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataset);
        dynamicclasses.setAdapter(arrayAdapter);
        dynamicclasses.setOnItemClickListener(MshowforItem);
    }//onCreate
    private AdapterView.OnItemClickListener MshowforItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tvTemp = ((TextView) view);

            tvTemp.setText(dataset[position]);

            arrayAdapter.notifyDataSetChanged();
        }
    };
}//Class