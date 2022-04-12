package com.example.knox;

public final class Mediator {
    private static volatile Mediator instance = null;
    private Mediator(){}
    public static Mediator getInstance(){
        if(instance == null){
            instance = new Mediator();
        }
        return instance;
    }
}
