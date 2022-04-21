package com.example.knox;

public final class Database {
    private static volatile Database instance = null;
    private Database(){}
    public static Database getInstance(){
        if(instance == null){
            instance = new Database();
        }
        return instance;
    }
}
