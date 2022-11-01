package com.example.bacterial_blight;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.bacterial_blight.ml.Model;
import com.example.bacterial_blight.ml.Vgg19;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.checkerframework.common.subtyping.qual.Bottom;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {
    Button select, scan;
    ImageView placeholder, placeholder1;
    TextView vggResult, resnetResult, confidence, link;
    Bitmap image = null;
    Bitmap segmentedImage = null;
    String imageString = "";
    int imageSize =  224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        select = findViewById(R.id.upload_button);
        scan = findViewById(R.id.scan_button);
        vggResult = findViewById(R.id.vggResult);
        resnetResult = findViewById(R.id.resnetResult);
        placeholder = findViewById(R.id.image_placeholder);

        //Select Image Button
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vggResult.setText("Result");
                resnetResult.setText("Result");
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
                    image = Bitmap.createScaledBitmap(segmentedImage, imageSize, imageSize, false);
                        //Pass the image to the models to make a prediction
                        vgg19(image);
                        resNet(image);
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
                    Toast.makeText(MainActivity.this, "An error has occurred.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                placeholder.setImageBitmap(image);
                segmentation(image);
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
            resnetResult.setText(classes[maxPos]);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
            Toast.makeText(MainActivity.this, "An error has occurred.", Toast.LENGTH_LONG).show();
        }
    }

    public void vgg19(Bitmap image){
        /*
         * This method loads the VGG19 model
         * and scans through the input image for prediction
         */
        try {
            Vgg19 model = Vgg19.newInstance(getApplicationContext());

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
            Vgg19.Outputs outputs = model.process(inputFeature0);
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
            vggResult.setText(classes[maxPos]);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
            Toast.makeText(MainActivity.this, "An error has occurred.", Toast.LENGTH_LONG).show();
        }
    }

    public void segmentation(Bitmap image){
        /*
         *   this function calls the python instance to perform segmentation
         */

        //store the encoded image to imageString and pass to python script
        imageString = getStringImage(image);

        //calling Python modules and function
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        final Python py = Python.getInstance();
        try{
            PyObject pyObj = py.getModule("segmentation");
            PyObject obj = pyObj.callAttr("main", imageString);

            //get the String image from the python function
            String str = obj.toString();
            //convert the string to byte array
            byte data[] = android.util.Base64.decode(str, Base64.DEFAULT);
            //convert the byte to Bitmap
            segmentedImage = BitmapFactory.decodeByteArray(data, 0, data.length);

            // NOTE: remove comment when asked to display the segmented image
            // placeholder.setImageBitmap(segmentedImage);
        } catch (Exception e){
            Toast.makeText(MainActivity.this, "An error has occurred.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private String getStringImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);

        //store image in byte array
        byte[] convertedImageByte = baos.toByteArray();
        //encode to string
        String encodedImage = android.util.Base64.encodeToString(convertedImageByte,Base64.DEFAULT);
        return encodedImage;
    }

    /* ---------------------------------------------
    * Pop up for result and control measures for CBB
    * Update: Not needed.
    *  ---------------------------------------------
    */
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