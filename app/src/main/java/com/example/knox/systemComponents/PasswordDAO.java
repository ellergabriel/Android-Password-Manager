package com.example.knox.systemComponents;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


/**
 * Interface that allows for accessing Room database
 * All queries are set up here and called by the Database object
 */
@Dao
public interface PasswordDAO {

    @Query("SELECT * FROM credentials WHERE URL = :URL")
    Credentials getFullCred(String URL);

    @Insert(entity = Credentials.class, onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Credentials creds);
    @Delete
    void delete(Credentials cred);

    //new
    @Insert
    void insert(Credentials cred);
    @Query("Select * from credentials")
    List<Credentials> getAllCreds();


}
