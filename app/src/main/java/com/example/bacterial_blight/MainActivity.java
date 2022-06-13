package com.example.bacterial_blight;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bacterial_blight.ml.Model;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.checkerframework.common.subtyping.qual.Bottom;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {
    Button select, scan;
    ImageView placeholder;
    TextView result, confidence, link;
    RadioGroup radioGroup;
    RadioButton chosenModel;
    Bitmap image = null;
    int imageSize =  224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        select = findViewById(R.id.upload_button);
        scan = findViewById(R.id.scan_button);
        result = findViewById(R.id.result);
        placeholder = findViewById(R.id.image_placeholder);
        radioGroup = findViewById(R.id.radioGroup);

        //Select Image Button
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        //Scan Button
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks if an image has been selected. If no message, return a toast message.
                if(image == null){
                    Toast.makeText(MainActivity.this, "Cannot perform prediction. Select an image.", Toast.LENGTH_LONG).show();
                } else {
                    //Resizes the image for classification
                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                    int model = radioGroup.getCheckedRadioButtonId();
                    chosenModel = findViewById(model);

                    switch(model){
                        case R.id.vggRadioBtn:
                            result.setText("Feature coming soon.");
                            break;
                        case R.id.resNetRadioBtn:
                            resNet(image);
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         *Get the selected image from the gallery and display to the placeholder
         */
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK && data != null){
                Uri selectedImage = data.getData();

                try{
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e){
                    Toast.makeText(MainActivity.this, "An error has occured.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                placeholder.setImageBitmap(image);
            }
        }

    public void resNet(Bitmap image){
        /*
         * This method loads the ResNet-50 model
         * and scans through the input message for prediction
         */
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixels and extract RGB values. Values will be added individually to the byte buffer
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; //RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f/1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f/1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f/1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            //find the index of class with big confidence
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            String[] classes = {"CBB", "Healthy"};
            //Display the class of the prediction
            result.setText(classes[maxPos]);

            resultPop(classes[maxPos]);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
            Toast.makeText(MainActivity.this, "An error has occured.", Toast.LENGTH_LONG).show();
        }
    }

    public void resultPop(String result){
        /*
         * This method accepts the prediction from VGG-19 or ResNet-50 Model
         * and display the corresponding bottom sheet accdg. to the result
         */
        BottomSheetDialog bottom_sheet = new BottomSheetDialog(MainActivity.this,R.style.BottomSheetTheme);
        View sheetView = null;

        if(result.equals("CBB")){
            //display the bottom sheet result
            sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_pop_result,
                    findViewById(R.id.popUpContainer));
        } else if(result.equals("Healthy")){
            sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_pop_healthy_result,
                    findViewById(R.id.popUpContainer));
        }
        bottom_sheet.setContentView(sheetView);
        bottom_sheet.show();
    }
}