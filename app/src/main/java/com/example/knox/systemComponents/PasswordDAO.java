package com.example.knox.systemComponents;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PasswordDAO {

    @Query("SELECT username, password FROM credentials WHERE url = :URL")
    Credentials getFullCred(String URL);

    @Insert
    void insertAll(Credentials creds);
    @Delete
    void delete(Credentials cred);
}
