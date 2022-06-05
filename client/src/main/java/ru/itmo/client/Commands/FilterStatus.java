package ru.itmo.client.Commands;

import ru.itmo.client.Command;
import ru.itmo.client.ServerAPI;
import ru.itmo.client.ServerAPIImpl;
import ru.itmo.common.authorization.User;
import ru.itmo.common.connection.Response;

import java.io.BufferedReader;
import java.io.IOException;

public class FilterStatus implements Command {
    public String filter_less_than_status(BufferedReader bf) throws IOException{
        System.out.print("Enter status: ");
        return bf.readLine().split(" ")[0];
    }

    @Override
    public String executeCommand(BufferedReader bf, User user) throws IOException{
        ServerAPI serverAPI = new ServerAPIImpl();
        Response response = serverAPI.filter_less_than_status(filter_less_than_status(bf), user);

        if(response == null){
            return "Filter_less_than_status command returned null";
        }
        else {
            if(response.status == Response.cmdStatus.OK){
                return response.getArgumentAs(String.class);
            }
            else{
                return "Filter_less_than_status command returned status 'ERROR': " + response.getArgumentAs(String.class);
            }
        }
    }
    @Override
    public String getName(){
        return "filter_less_than_status";
    }
}
