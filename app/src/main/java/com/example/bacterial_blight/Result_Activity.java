package com.example.bacterial_blight;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.IDNA;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Result_Activity extends AppCompatActivity {
    ImageView imageInformation, result_fetched_image;
    TextView InformationHolder, diagnosisContainer;
    int imageSize =  224;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // instanciate here
        InformationHolder = (TextView)findViewById(R.id.InformationHolder);
        result_fetched_image = (ImageView)findViewById(R.id.result_fetched_image);

        //Intent getVggResult = getIntent();
        //String VggResult = getVggResult.getStringExtra("VGGResult");
        InformationHolder.setText("Bacterial Blight Infected");

        byte[] byteArray = getIntent().getByteArrayExtra("VGGImgResult");
        Bitmap VGGImgResult = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        result_fetched_image.setImageBitmap(VGGImgResult);
        //Bundle bundle = getIntent().getExtras();
        //if(bundle != null){
        //    result_fetched_image.setImageBitmap(VGGImgResult);
        //}


    }
}
