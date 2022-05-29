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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ru.itmo.server.src.containers.requestResponse;
import ru.itmo.server.src.containers.stringQueue;

public class Server {
    private final static DAO<Worker> dao = new DataDAO();
    private static ArrayDeque<Commands> q = new ArrayDeque<Commands>();
    private static final ArrayList<Commands> cmd = Help.getLst();
    public static void main(String[] args){

        procedure();

        try {
            while(Exit.getExit()) {
                connection();
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void connection() throws InterruptedException{
        InputStreamReader in = new InputStreamReader(System.in);

        try(ServerSocketChannel serv = ServerSocketChannel.open()){
            SocketAddress address = new InetSocketAddress("localhost", 65100);
            serv.bind(address);
            SocketChannel server = serv.accept();

            //Многопоток клиентов
            ExecutorService exec = Executors.newCachedThreadPool();
            exec.execute(() -> {
                requestResponse requestResponse;
                Request request;
                Response response = new Response(
                        Response.cmdStatus.OK,
                        "Начинаю обработку команды"
                );
                try {
                    requestResponse = reading(server, response);
                    request = requestResponse.getRequest();
                    response = requestResponse.getResponse();
                    String message = response.getArgumentAs(String.class);

                    commandExecuting(message, request, server);
                }
                catch(IOException e){
                    Response errResponse = new Response(
                        Response.cmdStatus.ERROR,
                        e.getMessage() + " on line 67 in Server.main"
                    );
                    send(errResponse, server);
                }
                /*try {
                    server.close();
                    serv.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }*/
            });
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
    private static requestResponse reading(SocketChannel server, Response response) throws IOException{
        byte[] buffer = new byte[8192];

        int amount = server.read(ByteBuffer.wrap(buffer));
        if (amount <= 0) {
            response = new Response(
                    Response.cmdStatus.ERROR,
                    "There's nothing to read"
            );
        }
        byte[] bytes = new byte[amount];
        System.arraycopy(buffer, 0, bytes, 0, amount);
        String json = new String(bytes, StandardCharsets.UTF_8);

        return new requestResponse(Request.fromJson(json), response);
    }
    private static void commandExecuting(String message, Request request, SocketChannel server){
        Runnable task = () -> {

            String command = request.commandName;
            String answer = "";
            int flag = 0;

            for (Commands cm : cmd) {
                if (cm.getName().equals(command)) {
                    flag += 1;
                    try {
                        stringQueue stringQueue = cm.requestExecute(dao, q, request);
                        q = stringQueue.getQueue();
                        answer = stringQueue.getString();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            if (flag == 0) {
                answer = "Unknown command. Type help for the list of commands";
            }
            send(message + "\n" + answer, server);
        };
        Thread thread = new Thread(task);
        thread.start();
    }
    private static void send(Response response, SocketChannel server){
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(() -> {
            try {
                //System.out.println(response.getArgumentAs(String.class));
                server.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));
            }
            catch(IOException e){
                System.out.println(e.getMessage() + " on line 142 in Server.java");
            }
        });
    }
    private static void send(String message, SocketChannel server){
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(() -> {
            Response response = new Response(
                    Response.cmdStatus.OK,
                    message
            );
            try {
                server.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        });
    }
}
