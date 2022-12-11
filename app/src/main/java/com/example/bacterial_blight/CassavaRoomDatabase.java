package com.example.bacterial_blight;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Cassava.class}, version=1, exportSchema= false)
@TypeConverters({Converters.class})
public abstract class CassavaRoomDatabase extends RoomDatabase {

    public abstract CassavaDAO cassavaDAO();

    private static volatile CassavaRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static CassavaRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (CassavaRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CassavaRoomDatabase.class, "tbcassava")
                            .addCallback(cRoomDatabaseCallback)
                            .build();
                }
            }
        }return INSTANCE;
    }
    // ADD " .addCallback(cRoomDatabaseCallback) " before the
    // " .build() " to fill some dummy datas in the database
    private static RoomDatabase.Callback cRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // This is for prior populating the database,
            // if you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                CassavaDAO dao = INSTANCE.cassavaDAO();
                dao.deleteAll();

                //Cassava test1 = new Cassava("Healthy","CassavaTest1");
                //dao.insert(test1);
                //test1 = new Cassava("CBB Infected", "CassavaTest2");
                //dao.insert(test1);
                //Cassava test3 = new Cassava("CBB Infected", "CassavaTest3");
                //dao.insert(test3);
            });
        }
    };
}
