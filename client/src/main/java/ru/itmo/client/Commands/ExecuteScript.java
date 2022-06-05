package ru.itmo.client.Commands;

import ru.itmo.client.Command;
import ru.itmo.client.ServerAPI;
import ru.itmo.client.ServerAPIImpl;
import ru.itmo.common.authorization.User;
import ru.itmo.common.connection.Response;

import java.io.BufferedReader;
import java.io.IOException;

public class ExecuteScript implements Command {

    @Override
    public String executeCommand(BufferedReader bf, User user) throws IOException{
        System.out.print("Enter filepath: ");
        String filepath = bf.readLine();
        ServerAPI serverAPI = new ServerAPIImpl();
        Response response = serverAPI.execute_script(filepath, user);

        if(response == null){
            return "Execute_script command returned null";
        }
        else {
            if(response.status == Response.cmdStatus.OK){
                return response.getArgumentAs(String.class);
            }
            else{
                return "Execute_script command returned status 'ERROR': " + response.getArgumentAs(String.class);
            }
        }
    }
    @Override
    public String getName(){
        return "execute_script";
    }
}
