package com.example.study_with_teddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ALLsettingsMain extends AppCompatActivity {
    private Button guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allsettingsmain);
        guide = findViewById(R.id.button);

        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ALLsettingsMain.this, listSettings.class));

            }
        });
    }
}