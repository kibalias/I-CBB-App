package com.example.bacterial_blight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Manual_Activity extends AppCompatActivity {

    ImageView backBtn;
    Button toMainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        backBtn = findViewById(R.id.back);
        toMainBtn = findViewById(R.id.toMainBtn);

        // Arrow button to redirect to main menu
        backBtn.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick (View v){
                int activity = 1;
                changePage(activity);
            }
        });

        // Proceed to backterial bligh detection button
        toMainBtn.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick (View v){
                int activity = 0;
                changePage(activity);
            }
        });
    }

    public void changePage(int toPage){
        int mainActivity = 1;
        Intent intent;

        if(toPage == mainActivity){
            intent = new Intent(this, Home_Activity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
    }
}