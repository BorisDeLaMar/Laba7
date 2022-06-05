package ru.itmo.client.Commands;

import ru.itmo.client.Command;
import ru.itmo.client.ServerAPI;
import ru.itmo.client.ServerAPIImpl;
import ru.itmo.common.authorization.User;
import ru.itmo.common.connection.Response;

import java.io.BufferedReader;
import java.io.IOException;

public class Info implements Command {
    public void info(){

    }

    @Override
    public String executeCommand(BufferedReader bf, User user) throws IOException{
        ServerAPI serverAPI = new ServerAPIImpl();
        Response response = serverAPI.info(user);

        if(response == null){
            return "Info command returned null";
        }
        else {
            if(response.status == Response.cmdStatus.OK){
                return response.getArgumentAs(String.class);
            }
            else{
                return "Info command returned status 'ERROR': " + response.getArgumentAs(String.class);
            }
        }
    }
    @Override
    public String getName(){
        return "info";
    }
}
