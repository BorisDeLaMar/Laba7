package ru.itmo.server.src.containers;

public class booleanString {
    private final boolean flag;
    private final String message;

    public booleanString(boolean flag, String message){
        this.flag = flag;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean getFlag() {
        return flag;
    }
}
