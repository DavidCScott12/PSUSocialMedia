package com.example.psusocialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.Charset;



public class schedule extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseUser user;
    String[][] allClasses;
    final long ONE_MEGABYTE = 1024 * 1024;
    int count;
    int count2;

    private static String[] convertToStrings(byte[][] byteStrings) {
        String[] data = new String[byteStrings.length];
        for (int i = 0; i < byteStrings.length; i++) {
            data[i] = new String(byteStrings[i], Charset.defaultCharset());

        }
        return data;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        StorageReference userRef = storageRef.child("classes/"+user.getEmail());
        count2 = 0;
        userRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getPrefixes())
                {
                    byte[][] temp = new byte[4][64];
                    item.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            count = 0;
                            for (StorageReference item : listResult.getItems()){
                                item.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        temp[count] = bytes;
                                        count++;
                                    }
                                });
                            }
                        }
                    });
                    allClasses[count2] = convertToStrings(temp);
                    count2++;
                }//for
            }//onSuccess
        });//OnSuccsessListener
        for(int x = 0; x < allClasses.length;x++)
        Log.d("Classes", allClasses[x].toString());
    }//onCreate
}//Class