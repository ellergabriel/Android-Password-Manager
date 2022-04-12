package com.example.knox;

public final class Requestor {
    private static volatile Requestor instance = null;
    private Requestor(){}
    public static Requestor getInstance(){
        if(instance == null){
            instance = new Requestor();
        }
        return instance;
    }
}
