package com.example.knox.systemComponents;
import androidx.room.RoomDatabase;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

//Todo: implement password generation algorithm
@androidx.room.Database(entities = {Credentials.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract PasswordDAO passDao();
    private static volatile Database instance = null;
    public Database(){}
    public static Database getInstance(){
        if(instance == null){
            //instance = new Database();
        }
        return instance;
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
                if ((currentChar >= 33 && currentChar < 48)
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
