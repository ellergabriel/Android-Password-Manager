package com.example.knox.systemComponents;

//Todo: implement password generation algorithm
public final class Database {
    private static volatile Database instance = null;
    private Database(){}
    public static Database getInstance(){
        if(instance == null){
            instance = new Database();
        }
        return instance;
    }

    //Todo: implement password generation algorithm
    private void generate(int length){

    }
}
