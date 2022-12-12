package com.example.bacterial_blight;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.room.TypeConverter;
import java.io.ByteArrayOutputStream;
//This class is used when we are storing bitmap by converting it to bytearray not needed if using uri method
class Converters {

    @TypeConverter
    public static byte[] fromBitmap(Bitmap bitmap)  {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        return outputStream.toByteArray();
    }
    @TypeConverter
    public static Bitmap toBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
    }
}
