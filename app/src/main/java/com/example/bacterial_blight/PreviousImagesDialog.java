package com.example.bacterial_blight;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class PreviousImagesDialog extends RecyclerView.Adapter<PreviousImagesDialog.MyViewHolder> {
    private ArrayList<Cassava> cassavaList;

    public PreviousImagesDialog(ArrayList<Cassava> cassavaList){
        this.cassavaList = cassavaList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView imgresult_placeholder;
        private TextView imgresult_filename;
        private TextView imgresult_datetaken;

        public MyViewHolder(final View view){
            super(view);
            imgresult_placeholder = (TextView)view.findViewById(R.id.imgresult_filename);
            imgresult_filename = (TextView)view.findViewById(R.id.imgresult_vggresult);
            imgresult_datetaken = (TextView)view.findViewById(R.id.imgresult_resnetresult);

        }

    }

    @NonNull
    @Override
    public PreviousImagesDialog.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View imagelist_results = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.previous_image_holder, parent, false);
        return new MyViewHolder(imagelist_results);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousImagesDialog.MyViewHolder holder, int position) {
        String filename = cassavaList.get(position).getFile_name();
        String cassavavgg = cassavaList.get(position).getVggresult_placeholder();
        String cassavaresnet = cassavaList.get(position).getResnetresult_placeholder();

        holder.imgresult_placeholder.setText(filename);
        holder.imgresult_filename.setText(cassavavgg);
        holder.imgresult_datetaken.setText(cassavaresnet);
    }

    @Override
    public int getItemCount() {
        return cassavaList.size();
    }
}
