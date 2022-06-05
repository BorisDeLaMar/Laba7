package ru.itmo.client.Commands;

import ru.itmo.client.Command;
import ru.itmo.client.ServerAPI;
import ru.itmo.client.ServerAPIImpl;
import ru.itmo.common.authorization.User;
import ru.itmo.common.connection.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Update implements Command{
    public Response update(BufferedReader on, User user) throws IOException{
        System.out.print("Enter id: ");
        String id = on.readLine().split(" ")[0];

        Add add = new Add();
        ArrayList<String> arr = add.add(on);
        arr.add(id);

        ServerAPI serverAPI = new ServerAPIImpl();
        return serverAPI.update(arr, user);
    }

    @Override
    public String executeCommand(BufferedReader bf, User user) throws IOException{
        Response response = update(bf, user);

        if(response == null){
            return "Update command returned null";
        }
        else {
            if(response.status == Response.cmdStatus.OK){
                return response.getArgumentAs(String.class);
            }
            else{
                return "Update command returned status 'ERROR': " + response.getArgumentAs(String.class);
            }
        }
    }
    @Override
    public String getName(){
        return "update";
    }
}
