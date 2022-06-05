package ru.itmo.client.Commands;

import ru.itmo.client.Command;
import ru.itmo.client.ServerAPI;
import ru.itmo.client.ServerAPIImpl;
import ru.itmo.common.authorization.User;
import ru.itmo.common.connection.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Add implements Command {

    public ArrayList<String> add(BufferedReader on) throws IOException {
        System.out.println("Enter name: ");
        String name = on.readLine();
        System.out.println("Enter salary: ");
        String salo = on.readLine().split(" ")[0];
        System.out.println("Enter position: ");
        String posit = on.readLine().split(" ")[0];
        System.out.println("Enter status: ");
        String stata = on.readLine().split(" ")[0];

        System.out.println("Enter organization: ");
        String[] arg = on.readLine().split(" ");
        while(arg.length < 2){
            System.out.println("There should be two arguments for organization filed: name and type. Enter args again:");
            arg = on.readLine().split(" ");
        }

        System.out.println("Enter coordinates: ");
        String[] args = on.readLine().split(" ");
        while(args.length < 2){
            System.out.println("There should be two arguments for coordinates filed: 'x' and 'y'. Enter args again:");
            args = on.readLine().split(" ");
        }

        return Stream.of(name, salo, posit, stata, arg[0], arg[1], args[0], args[1]).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String executeCommand(BufferedReader bf, User user) throws IOException, NullPointerException{
        ServerAPI serverAPI = new ServerAPIImpl();
        Response response = serverAPI.add(add(bf), user);
        if(response == null){
            return "So that worker wasn't added";
        }
        else {
            if(response.status == Response.cmdStatus.OK){
                return response.getArgumentAs(String.class);
            }
            else{
                return "Add command returned status 'ERROR': " + response.getArgumentAs(String.class);
            }
        }
    }
    @Override
    public String getName(){
        return "add";
    }
}
