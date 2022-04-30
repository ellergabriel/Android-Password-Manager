package com.example.knox.systemComponents;
import androidx.room.RoomDatabase;

import java.util.concurrent.ThreadLocalRandom;

//Todo: implement password generation algorithm
@androidx.room.Database(entities = {Credentials.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract PasswordDAO passDao();
    private static volatile Database instance = null;
    private Database(){}
    public static Database getInstance(){
        if(instance == null){
            //instance = new Database();
        }
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
