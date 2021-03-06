package com.pandasdroid.scanner.persistance;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DocumentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAll(List<Document> documents);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Document document);

    @Query("SELECT * FROM Document ORDER BY documentId DESC")
    LiveData<List<Document>> findAll();

    @Query("SELECT * FROM Document WHERE name like :text ORDER BY documentId DESC")
    LiveData<List<Document>> search( String text );

    @Update
    void update( Document document );

    @Delete
    void delete( Document document );
}
