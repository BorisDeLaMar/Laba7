package ru.itmo.common.connection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable {
    public final String commandName;
    private final Object argument;
    private String user_login = "default user";

    public Request(String commandName, String user_login, Object argument){
        this.commandName = commandName;
        this.user_login = user_login;
        this.argument = new Gson().toJson(argument);
    }
    public Request(String commandName, Object argument){
        this.commandName = commandName;
        this.argument = new Gson().toJson(argument);
    }
    public Request(String commandName, String user_login, ArrayList<String> arguments){
        this.commandName = commandName;
        this.user_login = user_login;
        this.argument = new Gson().toJson(arguments);
    }

    public static Request fromJson(String json){
        return new Gson().fromJson(json, Request.class);
    }
    public <T> T getArgumentAs(Class<T> clazz){
        return new Gson().fromJson((String) argument, clazz);
    }
    public <T> Object getArgumentAs(TypeToken<T> typeToken){
        return new Gson().fromJson((String) argument, typeToken.getType());
    }
    public String toJson(){
        return new Gson().toJson(this);
    }

    public String getUser_login() {
        return user_login;
    }
}
