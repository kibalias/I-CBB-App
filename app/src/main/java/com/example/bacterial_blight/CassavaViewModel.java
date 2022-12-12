package com.example.bacterial_blight;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CassavaViewModel extends AndroidViewModel {

    private CassavaRepository cassavaRepository;

    private final LiveData<List<Cassava>> cassavaResults;

    public CassavaViewModel(@NonNull Application application) {
        super(application);
        cassavaRepository = new CassavaRepository(application);
        cassavaResults = cassavaRepository.getAllCassavaResults();
    }

    LiveData<List<Cassava>> getCassavaResults() { return cassavaResults; }

    public void insert(Cassava cassava) { cassavaRepository.insert(cassava);}


}
