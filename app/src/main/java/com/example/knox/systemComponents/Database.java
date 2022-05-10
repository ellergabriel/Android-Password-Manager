package com.example.knox.systemComponents;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

//Todo: implement password generation algorithm
@androidx.room.Database(entities = {Credentials.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static volatile Database instance;
    private static volatile PasswordDAO dao;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract PasswordDAO passDao();

    public Database(){

    }

    public static PasswordDAO getInstance(Context context) {
//        if (instance == null) {
//            //instance = new Database();
//            Log.d(" testing: ","Making credentials class");
//            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "credentials")
//                    .allowMainThreadQueries()
//                    .build();
//        }
//        Log.d("testing: ", "database instance");
//        return instance;
        if(instance == null){
            synchronized (Database.class){
                if(instance == null){
                    Log.d(" testing: ","Making credentials class");
                    instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "credentials")
                            .allowMainThreadQueries().build();
                    dao = instance.passDao();
                }
            }
        }
        Log.d("testing: ", "database instance");
        return dao;
    }


    public static String generate(int length){
        Random rand = new Random();
        //setSeed() ensures different rand values each runtime
        rand.setSeed(System.currentTimeMillis());
        boolean hasUpper, hasLower, hasNum, hasSym;
        hasUpper = hasLower = hasNum = hasSym = false;
        String password = "";
        while (!hasUpper || !hasLower || !hasNum || !hasSym) {//required elements for good password
            password = "";
            for (int i = 0; i < length; i++) {
                //generate ASCII value for valid character
                int currentChar = (rand.nextInt(90) + 33); //33 - 122 range gives valid chars

                password += (char) currentChar;
                //character validation; if not all 4 bools are true, repeat sequence
                if ((currentChar < 48)
                        || (currentChar >= 58 && currentChar < 65)
                        || (currentChar >= 91 && currentChar < 97)) {
                    hasSym = true;
                } else if (currentChar >= 48 && currentChar < 58) {
                    hasNum = true;
                } else if (currentChar >= 65 && currentChar < 91) {
                    hasUpper = true;
                } else {
                    hasLower = true;
                }
            } //end for
        }// end while
        return password;
    }
}
