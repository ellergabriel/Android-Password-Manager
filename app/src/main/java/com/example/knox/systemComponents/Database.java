package com.example.knox.systemComponents;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ThreadLocalRandom;

//Todo: implement password generation algorithm
@androidx.room.Database(entities = {Credentials.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static volatile Database instance;

    public abstract PasswordDAO passDao();

    public Database(){

    }

    public static Database getInstance(Context context) {
        if (instance == null) {
            //instance = new Database();
            Log.d(" testing: ","Making credentials class");
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "credentials")
                    .allowMainThreadQueries()
                    .build();
        }
        Log.d("testing: ", "database instance");
        return instance;
    }

    //Todo: implement password generation algorithm
    private void generate(int length){
        boolean hasUpper, hasLower, hasNum, hasSym;
        String password = "";
        for(int i = 0; i < length; i++) {
            //generate ASCII value for valid character
            int currentChar = ThreadLocalRandom.current().nextInt(33, 123);
            //switch to check which boolean flags are satisfied
            switch(currentChar){
                case (1):

            }
            password += (char) currentChar;
        }
    }
}
