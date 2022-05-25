package ru.itmo.server;

//import javafx.concurrent.Worker;
import ru.itmo.server.src.Comms.*;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.common.connection.*;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.io.*;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Server {
    private final static DAO<Worker> dao = new DataDAO();
    private static ArrayDeque<Commands> q = new ArrayDeque<Commands>();
    private static ArrayList<Commands> cmd = Help.getLst();
    public static void main(String[] args){

        procedure();

        try {
            while(Exit.getExit()) {
                connection();
            }
        } catch (InterruptedException e) {
            println(e.getMessage());
        }
    }

    public static void connection() throws InterruptedException{
        InputStreamReader in = new InputStreamReader(System.in);
        /*Scanner input = new Scanner(System.in);

        String cur_cmd;
        if ((cur_cmd = input.next()) != null && cur_cmd.equals("secret_save228")) {
            Save save = new Save();
            String filepath = System.getenv("FPATH");
            save.save(dao, filepath);
            System.out.println("Работаем без шума и пыли");
        }
        else{
            System.out.println(cur_cmd + " is something unclear for me");
        }
        input.close();*/
        try(ServerSocketChannel serv = ServerSocketChannel.open()){
            SocketAddress address = new InetSocketAddress("localhost", 65100);
            serv.bind(address);
            SocketChannel server = serv.accept();

            BufferedReader bif = new BufferedReader(in);
            byte[] buffer = new byte[8192];

            {
                ExecuteScript.file_bdCleaner();
                int amount = server.read(ByteBuffer.wrap(buffer));
                if (amount <= 0) {
                    Response response = new Response(
                            Response.cmdStatus.ERROR,
                            "There's nothing to read (Server var amount is <= 0)"
                    );//как этот респонс на клианта отправить
                    server.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));
                    server.close();
                    serv.close();
                }
                byte[] bytes = new byte[amount];
                System.arraycopy(buffer, 0, bytes, 0, amount);
                String json = new String(bytes, StandardCharsets.UTF_8);

                Request request = Request.fromJson(json);

                String command = request.commandName;
                int flag = 0;

                for (int i = 0; i < cmd.size(); i++) {
                    Commands cm = cmd.get(i);
                    if (cm.getName().equals(command)) {
                        flag += 1;
                        try {
                            q = cm.requestExecute(dao, q, bif, request, server);
                            GistStaff.setReply("");
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                if (flag == 0) {
                    Response response = new Response(
                            Response.cmdStatus.ERROR,
                            "Unknown command. Type help for the list of commands"
                    );
                    server.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));
                }
                server.close(); //Добавить с exit эту тему
                serv.close();
            }
            bif.close();
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
            Exit.setExit(false);
        }
    }
    private static void procedure(){
        Worker.bannedID.clear();
        Help.fillLst();
        Long c = (long) 0;
        Worker.bannedID.add(0, c);

        String filepath = System.getenv("FPATH");
        dao.DateRead(filepath);
        DataDAO.setFlag(true);
    }
    public static void println(String stuff){
        System.out.println(stuff);
    }
}
