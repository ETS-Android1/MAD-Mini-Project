package com.example.study_with_teddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

public class MpomodoroMain extends AppCompatActivity {

    private EditText mTitle, mDesc;
    private Button mSaveBtn, mShowBtn;
    private Button Timmer;

    private FirebaseFirestore db;
    private String uTitle,uDesc,uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpomodoro_main);

        mTitle = findViewById(R.id.edit_title);
        mDesc = findViewById(R.id.edit_desc);
        mSaveBtn = findViewById(R.id.save_btn);
        mShowBtn= findViewById(R.id.showall_btn);
        Timmer = findViewById(R.id.timmer_btn);

        //start of intent to timmer
        Timmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MpomodoroMain.this, com.example.study_with_teddy.MTimmer.class));

            }
        });
        //end of intent to timmer

        db = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mSaveBtn.setText("Update");
            uTitle = bundle.getString("uTitle");
            uId = bundle.getString("uId");
            uDesc = bundle.getString("uDesc");
            mTitle.setText(uTitle);
            mDesc.setText(uDesc);
        }else{
            mSaveBtn.setText("Save");
        }

        mShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MpomodoroMain.this, MShowActivity.class));
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitle.getText().toString();
                String desc = mDesc.getText().toString();

                Bundle bundle1 = getIntent().getExtras();
                if(bundle1 != null){
                    String id = uId;
                    updateToFireStore(id, title, desc);
                }else{
                    String id = UUID.randomUUID().toString();
                    saveToFireStore(id, title,desc);
                }
            }
        });
    }

    private void updateToFireStore(String id, String title, String desc) {
        db.collection("PomodoroSessions").document(id).update("title", title , "desc",desc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MpomodoroMain.this, "Data Updated!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MpomodoroMain.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MpomodoroMain.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToFireStore(String id, String title, String desc) {
        if(!title.isEmpty() && !desc.isEmpty()){
            HashMap<String,Object> map = new HashMap<>();
            map.put("id" ,id);
            map.put("title" ,title);
            map.put("desc",desc);

            db.collection("PomodoroSessions").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MpomodoroMain.this, "Data Saved!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MpomodoroMain.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }else
            Toast.makeText(this, "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show();
    }


}