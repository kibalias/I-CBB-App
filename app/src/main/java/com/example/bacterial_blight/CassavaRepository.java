package com.example.bacterial_blight;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CassavaRepository {
    private CassavaDAO mcassavaDAO;
    private LiveData<List<Cassava>> cassavaResults;

    CassavaRepository(Application application){
        CassavaRoomDatabase db = CassavaRoomDatabase.getDatabase(application);
        mcassavaDAO = db.cassavaDAO();
        cassavaResults = mcassavaDAO.getCassavaResults();
    }

    LiveData<List<Cassava>> getAllCassavaResults(){
        return cassavaResults;
    }

    void insert(Cassava cassava){
        CassavaRoomDatabase.databaseWriteExecutor.execute(() -> {
            mcassavaDAO.insert(cassava);
        });
    }
}
