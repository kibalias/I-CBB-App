package com.example.bacterial_blight;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PreviousImageViewHolder extends RecyclerView.ViewHolder{
    private TextView imgresult_filename;
    private TextView imgresult_vggresult;
    private TextView imgresult_resnetresult;
    private ImageView result_fetched_image;

    public PreviousImageViewHolder(@NonNull View itemView) {
        super(itemView);
        imgresult_filename = (TextView)itemView.findViewById(R.id.imgresult_filename);
        imgresult_vggresult = (TextView)itemView.findViewById(R.id.imgresult_vggresult);
        imgresult_resnetresult = (TextView)itemView.findViewById(R.id.imgresult_resnetresult);
        result_fetched_image = (ImageView)itemView.findViewById(R.id.result_fetched_image);
    }

    public void bind(String filename, String vggresult, String resnetresult, Bitmap Image){
        imgresult_filename.setText(filename);
        imgresult_vggresult.setText(vggresult);
        imgresult_resnetresult.setText(resnetresult);
        result_fetched_image.setImageBitmap(Image);
    }

    static PreviousImageViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.previous_image_holder, parent, false);
        return new PreviousImageViewHolder(view);
    }
}
