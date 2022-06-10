package ru.itmo.client;

import ru.itmo.client.Commands.*;
import ru.itmo.common.authorization.User;
import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        ArrayList<Command> cmd = fillLst();

        try {
            read(cmd);
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void read(ArrayList<Command> cmd) throws IOException{
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader bf = new BufferedReader(in);

        User user = authorization(bf);

        while(Exit.getExit()) {

            String[] line = bf.readLine().split(" ");
            String command = line[0];
            int flag = 0;

            for (Command cm : cmd) {
                if (cm.getName().equals(command)) {
                    flag += 1;
                    try {
                        System.out.println(cm.executeCommand(bf, user));
                    } catch (IOException | NullPointerException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

            if(flag == 0) {
                System.out.println("Unknown command. Type \"help\" for the list of available commands");
            }
        }
        bf.close();
    }

    private static User authorization(BufferedReader bf) throws IOException{
        ServerAPIImpl serverAPI = new ServerAPIImpl();

        System.out.print("Enter login: ");
        String login = bf.readLine();
        System.out.print("Enter password: ");
        String password = bf.readLine();
        String[] arr = null;
        arr = new String[2];
        arr[0] = login; arr[1] = password;
        Response response = serverAPI.sendToServer(new Request(
                "addUser",
                login,
                arr
        ));
        System.out.println(response.getArgumentAs(String.class));
        if(response.status.equals(Response.cmdStatus.ERROR)){
            authorization(bf);
        }
        Response response1 = serverAPI.sendToServer(new Request(
                "getUser",
                login,
                null
        ));
        if(response1.status.equals(Response.cmdStatus.ERROR)){
            System.out.println(response1.getArgumentAs(String.class));
            System.out.println("Due to this problem client stopped working");
            System.exit(228);
        }
        return response1.getArgumentAs(User.class);
    }
    private static ArrayList<Command> fillLst(){
        ArrayList<Command> cmd = new ArrayList<Command>();

        Command add = new Add();
        Command add_if_min = new AddIfMin();
        Command info = new Info();
        Command show = new Show();
        Command clear = new Clear();
        Command exit = new Exit();
        Command history = new History();
        Command execute_script = new ExecuteScript();
        Command filter_less_than_status = new FilterStatus();
        Command help = new Help();
        Command print_descending = new PrintDescending();
        Command print_unique_status = new PrintUniqueStatus();
        Command remove = new Remove();
        Command remove_lower = new RemoveLower();
        Command update = new Update();

        cmd.add(add);
        cmd.add(add_if_min);
        cmd.add(info);
        cmd.add(show);
        cmd.add(clear);
        cmd.add(exit);
        cmd.add(history);
        cmd.add(execute_script);
        cmd.add(filter_less_than_status);
        cmd.add(help);
        cmd.add(print_descending);
        cmd.add(print_unique_status);
        cmd.add(remove);
        cmd.add(remove_lower);
        cmd.add(update);

        return cmd;
    }
}