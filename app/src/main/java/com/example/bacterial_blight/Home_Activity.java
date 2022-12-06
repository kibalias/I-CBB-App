package com.example.bacterial_blight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Home_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button redirectToDetectionPage = findViewById(R.id.start_button);
        LoadingDialog loadingDialog = new LoadingDialog(Home_Activity.this);
        redirectToDetectionPage.setOnClickListener(v -> {
            loadingDialog.startLoadingDialog();

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                loadingDialog.dismissDialog();
                changeActivity();
            },1000);
        });

        TextView redirectToManual = (TextView) findViewById(R.id.toUse);
        redirectToManual.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick (View v){
                toManual();
            }
        });
    }
    private void changeActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void toManual(){
        Intent intent = new Intent(this, Manual_Activity.class);
        startActivity(intent);
    }
}