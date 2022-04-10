package com.example.psusocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    private AlertDialog.Builder currentAlertDialog;
    AlertDialog dialogAddClass;
    Button button_upload;
    ImageButton button_add;
    ImageButton button_scedule;
    EditText subject_et;
    EditText classnumber_et;
    EditText section_et;
    String newFileName;


    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference classesRef;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        classesRef = storageRef.child("classes");
        user = FirebaseAuth.getInstance().getCurrentUser();

        button_add = findViewById(R.id.button_addclass);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddClassDialogue();
            }
        });
        button_scedule = findViewById(R.id.button_scedule);
        button_scedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, schedule.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private static byte[][] convertToBytes(String[] strings) {
        byte[][] data = new byte[strings.length][];
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            data[i] = string.getBytes(Charset.defaultCharset()); // you can chose charset
        }
        return data;
    }

    private void saveToFireBase(String[] strings) {

        StorageReference userRef1 = storageRef.child("classes/"+user.getEmail()+"/class");
        newFileName = strings[1]+strings[2];
//        userRef1.listAll()
//                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
//                    @Override
//                    public void onSuccess(ListResult listResult) {
//                        if(listResult.getItems().size()>0) {
//                            newFileName = listResult.getItems().get(listResult.getItems().size()-1).getName();
//                        }
////                        for (StorageReference item : listResult.getItems()) {
////
////                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainActivity.this,"Error: "+e, Toast.LENGTH_LONG).show();
//                    }
//                });
//        StorageReference userRef2;
//        if (newFileName != null){
//            newFileName = newFileName+"1";
//        }else{
//            newFileName = "class";
//        }
        StorageReference userRef2;
        byte[][] twodbyte = convertToBytes(strings);
        userRef2 = storageRef.child("classes/"+user.getEmail()+"/"+newFileName+"/email");
        UploadTask uploadTask = userRef2.putBytes(twodbyte[0]);
        userRef2 = storageRef.child("classes/"+user.getEmail()+"/"+newFileName+"/subject");
        uploadTask = userRef2.putBytes(twodbyte[1]);
        userRef2 = storageRef.child("classes/"+user.getEmail()+"/"+newFileName+"/class_num");
        uploadTask = userRef2.putBytes(twodbyte[2]);
        userRef2 = storageRef.child("classes/"+user.getEmail()+"/"+newFileName+"/section");
        uploadTask = userRef2.putBytes(twodbyte[3]);
    }
    public void showAddClassDialogue()
    {
        currentAlertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.addclass_dialogue, null);
        button_upload = view.findViewById(R.id.button_upload);
        subject_et = view.findViewById(R.id.et_subject);
        classnumber_et = view.findViewById(R.id.classnumber_et);
        section_et = view.findViewById(R.id.section_et);

        currentAlertDialog.setView(view);
        currentAlertDialog.setTitle("Add Class");
        dialogAddClass = currentAlertDialog.create();
        dialogAddClass.show();

        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                String email = user.getEmail();
                String subject = subject_et.getText().toString();
                String classNum = classnumber_et.getText().toString();
                String section = section_et.getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(subject) || !TextUtils.isEmpty(classNum) || !TextUtils.isEmpty(section))
                {
                    String[] data = {email, subject,classNum,section};
                    saveToFireBase(data);
                    dialogAddClass.dismiss();
            }else{
                    Toast.makeText(MainActivity.this,"Please Fill All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Profile: {
                break;
            }
            case R.id.mso:{
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}