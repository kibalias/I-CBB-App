package com.example.bacterial_blight;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CassavaDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Cassava cassava);

    @Query("DELETE FROM tbcassava")
    void deleteAll();

    @Query("SELECT * FROM tbcassava ORDER BY clCassavaID DESC")
    LiveData<List<Cassava>> getCassavaResults();
}
