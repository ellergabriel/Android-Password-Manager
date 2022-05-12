package com.example.knox.systemComponents;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.DeleteTable;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


/**
 * Interface that allows for accessing Room database
 * All queries are set up here and called by the Database object
 */
@Dao
public interface PasswordDAO {

    //autofill query for matching URL
    @Query("SELECT * FROM credentials WHERE URL = :URL")
    Credentials getFullCred(String URL);

    //query used only for Vault display
    @Query("SELECT * FROM credentials")
    List<Credentials> vaultDisplay();

    @Delete
    void delete(Credentials cred);

    @Query("DELETE FROM credentials")
    void deleteAll();

    /**
     * Insertion will replace any existing URL; should only be called by onSaveRequest in Requestor
     * OR when the user
     * @param cred
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Credentials cred);

    @Query("Select * from credentials")
    List<Credentials> getAllCreds();


}
