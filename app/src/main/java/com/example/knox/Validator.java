package com.example.knox;

public final class Validator {
    private static volatile Validator instance = null;
    private Validator(){}
    public static Validator getInstance(){
        if(instance == null){
            instance = new Validator();
        }
        return instance;
    }
}
