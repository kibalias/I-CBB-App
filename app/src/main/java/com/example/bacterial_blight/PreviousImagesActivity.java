package com.example.bacterial_blight;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PreviousImagesActivity extends AppCompatActivity {
    private CassavaViewModel cassavaViewModel;
    ImageView back;

    public static final int NEW_CASSAVA_ACTIVITY_REQUEST_CODE = 1;
    // New implementation
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_image);

        back = (ImageView)findViewById(R.id.back);

        RecyclerView recyclerView = findViewById(R.id.imagelist_results);
        final PreviousImageAdapter adapter = new PreviousImageAdapter(new PreviousImageAdapter.CassavaDiff());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cassavaViewModel = new ViewModelProvider(this).get(CassavaViewModel.class);

        cassavaViewModel.getCassavaResults().observe(this, cassavas -> {
            //Update the cached copy of the cassava in the adapter.
            adapter.submitList(cassavas);
        });

        back.setOnClickListener(v -> {
            backtoPreviousPage();
        });
    }

    private void backtoPreviousPage() {
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    // Previous Implementation
    /*
    private ArrayList<Cassava> cassavaList;
    private RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_image);

        recyclerView = findViewById(R.id.imagelist_results);
        cassavaList = new ArrayList<>();

        setCassavaResultInfos();
        setAdapter();
    }

    private void setAdapter() {
        PreviousImagesDialog adapter = new PreviousImagesDialog(cassavaList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    private void setCassavaResultInfos() {
        cassavaList.add(new Cassava("Healthy","CassavaTest1"));
        cassavaList.add(new Cassava("CBB Infected","CassavaTest2"));
        cassavaList.add(new Cassava("Healthy","CassavaTest3"));
    }*/

}
