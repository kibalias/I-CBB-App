package com.example.bacterial_blight;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.IDNA;
import android.media.Image;
import android.os.Bundle;
import android.service.controls.Control;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Result_Activity extends AppCompatActivity {
    ImageView imageInformation, result_fetched_image;
    TextView InformationHolder, diagnosisContainer,
            Characteristics, Diagnosis1,
            HeaderControl, ControlMeasures;

    int imageSize =  224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultscrollable);

        // instanciate here
        InformationHolder = (TextView)findViewById(R.id.InformationHolder);
        result_fetched_image = (ImageView)findViewById(R.id.result_fetched_image);
        imageInformation = (ImageView)findViewById(R.id.imageInformation);
        //ConfirmResult = (TextView)findViewById(R.id.ConfirmResult);
        Characteristics = (TextView)findViewById(R.id.Characteristics);
        Diagnosis1 = (TextView)findViewById(R.id.Diagnosis1);
        HeaderControl = (TextView)findViewById(R.id.HeaderControl);
        ControlMeasures = (TextView)findViewById(R.id.ControlMeasures);

        Intent getResult = getIntent();
        String Result = getResult.getStringExtra("PredictResult");
        if(Result.equals("CBB Infected")){
            InformationHolder.setText("Bacterial Blight Infected");
            imageInformation.setImageDrawable(getResources().getDrawable(R.drawable.information));

            //ConfirmResult.setText(getResources().getString(R.string.CBBdiagnosis1));
            Characteristics.setText("Symptoms");
            Diagnosis1.setText(getResources().getString(R.string.CBBdiagnosis1));

            HeaderControl.setText("Preventive and Control Measures");
            ControlMeasures.setText(getResources().getString(R.string.PreventiveMeasures));

        }else if(Result.equals("Healthy")){
            InformationHolder.setText("Healthy Cassava Leaf");
            imageInformation.setImageDrawable(getResources().getDrawable(R.drawable.information__1_));

            //ConfirmResult.setText(getResources().getString(R.string.CBBdiagnosis1));
            Characteristics.setText("Characteristics");
            Diagnosis1.setText(getResources().getString(R.string.Healthydiagnosis1));

            HeaderControl.setText("Maintenance");
            ControlMeasures.setText(getResources().getString(R.string.Maintenance));
        }else{
            InformationHolder.setText("Information Error");
            //ConfirmResult.setText("Confirm Error");
            Characteristics.setText("Characteristics");
            Diagnosis1.setText("Error Fetching Diagnosis");
        }

        byte[] byteArray = getIntent().getByteArrayExtra("ImgResult");
        Bitmap ImgResult = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        result_fetched_image.setImageBitmap(ImgResult);

        Button backToClassificationPage = findViewById(R.id.backButton);
        backToClassificationPage.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick (View v){
                backToPage();
            }
        });
    }
    private void backToPage(){
        finish();
    }
}
