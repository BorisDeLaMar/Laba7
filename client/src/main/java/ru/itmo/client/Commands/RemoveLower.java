package ru.itmo.client.Commands;

import ru.itmo.client.Command;
import ru.itmo.client.ServerAPI;
import ru.itmo.client.ServerAPIImpl;
import ru.itmo.common.authorization.User;
import ru.itmo.common.connection.Response;

import java.io.BufferedReader;
import java.io.IOException;

public class RemoveLower implements Command {

    @Override
    public String executeCommand(BufferedReader bf, User user) throws IOException{
        ServerAPI serverAPI = new ServerAPIImpl();
        long id;
        while(true) {
            try {
                System.out.print("Enter id: ");
                id = Integer.parseInt(bf.readLine());
                break;
            }
            catch (NumberFormatException e){
                System.out.println("Id should be type long");
            }
        }
        Response response = serverAPI.remove_lower(id, user);

        if(response == null){
            return "Remove_lower command returned null";
        }
        else {
            if(response.status == Response.cmdStatus.OK){
                return response.getArgumentAs(String.class);
            }
            else{
                return "Remove_lower command returned status 'ERROR': " + response.getArgumentAs(String.class);
            }
        }
    }
    @Override
    public String getName(){
        return "remove_lower";
    }
}
