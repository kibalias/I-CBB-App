package com.example.bacterial_blight;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Entity(tableName = "tbcassava")
public class Cassava {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int clCassavaID;

    @NonNull
    private String file_name;
    @NonNull
    private String vggresult_placeholder;
    @NonNull
    private String resnetresult_placeholder;
    @NonNull
    private Bitmap imageresult;

    public Cassava(@NonNull String file_name, @NonNull String vggresult_placeholder,
                   @NonNull String resnetresult_placeholder, Bitmap imageresult) {
        this.vggresult_placeholder = vggresult_placeholder;
        this.file_name = file_name;
        this.resnetresult_placeholder = resnetresult_placeholder;
        this.imageresult = imageresult;
    }

    public int getClCassavaID() {
        return clCassavaID;
    }

    public void setClCassavaID(int clCassavaID) {
        this.clCassavaID = clCassavaID;
    }

    @NonNull
    public String getVggresult_placeholder() {
        return vggresult_placeholder;
    }

    public void setVggresult_placeholder(@NonNull String vggresult_placeholder) {
        this.vggresult_placeholder = vggresult_placeholder;
    }

    @NonNull
    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(@NonNull String file_name) {
        this.file_name = file_name;
    }

    @NonNull
    public String getResnetresult_placeholder() {
        return resnetresult_placeholder;
    }

    public void setResnetresult_placeholder(@NonNull String resnetresult_placeholder) {
        this.resnetresult_placeholder = resnetresult_placeholder;
    }

    @NonNull
    public Bitmap getImageresult() {
        return imageresult;
    }

    public void setImageresult(@NonNull Bitmap imageresult) {
        this.imageresult = imageresult;
    }
}
