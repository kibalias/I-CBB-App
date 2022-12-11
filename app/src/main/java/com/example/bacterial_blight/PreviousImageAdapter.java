package com.example.bacterial_blight;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class PreviousImageAdapter extends ListAdapter<Cassava, PreviousImageViewHolder> {

    public PreviousImageAdapter(@NonNull DiffUtil.ItemCallback<Cassava> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public PreviousImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PreviousImageViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousImageViewHolder holder, int position) {
        Cassava cassavaResult = getItem(position);

        holder.bind(cassavaResult.getFile_name()
                ,cassavaResult.getVggresult_placeholder()
                ,cassavaResult.getResnetresult_placeholder()
                ,cassavaResult.getImageresult());
    }


    static class CassavaDiff extends DiffUtil.ItemCallback<Cassava> {

        @Override
        public boolean areItemsTheSame(@NonNull Cassava oldItem, @NonNull Cassava newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Cassava oldItem, @NonNull Cassava newItem) {
            return oldItem.getFile_name().equals(newItem.getFile_name());
        }
    }
}
