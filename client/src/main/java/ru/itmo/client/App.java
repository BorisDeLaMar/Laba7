package ru.itmo.client;

import ru.itmo.client.Commands.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        ServerAPI serverAPI = new ServerAPIImpl();
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

        while(Exit.getExit()) {

            //GistStaff.setFlag(false);
            //GistStaff.setReply("");
            //ru.itmo.common.LAB5.src.Comms.ExecuteScript.file_bdCleaner();

            String[] line = bf.readLine().split(" ");
            String command = line[0];
            int flag = 0;

            for (Command cm : cmd) {
                if (cm.getName().equals(command)) {
                    flag += 1;
                    try {
                        System.out.println(cm.executeCommand(bf));
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