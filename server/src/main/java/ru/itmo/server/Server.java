package ru.itmo.server;

//import javafx.concurrent.Worker;
import com.google.gson.reflect.TypeToken;
import ru.itmo.common.DatabaseAccess;
import ru.itmo.common.authorization.User;
import ru.itmo.server.src.Comms.*;
import ru.itmo.server.src.Comms.database.DB_User;
import ru.itmo.server.src.Comms.database.DB_Worker;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.common.connection.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.io.*;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
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
    private static final ExecutorService exec = Executors.newCachedThreadPool();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
    public static void main(String[] args) {
        try {
            DB_User db_user = new DB_User();
            DB_Worker dbWorker = new DB_Worker();
        }
        catch(SQLException e){
            System.out.println(e.getMessage() + " \nP.S. Someone defecated into console, sorry((((");
        }
        procedure();

        try {
            while(Exit.getExit()) {
                connection();
                DatabaseAccess.getDBConnection();
            }
        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public static void connection() throws InterruptedException, SQLException{
        InputStreamReader in = new InputStreamReader(System.in);

        try(ServerSocketChannel serv = ServerSocketChannel.open()){
            SocketAddress address = new InetSocketAddress("localhost", 65100);
            serv.bind(address);
            SocketChannel server = serv.accept();

            //Многопоток клиентов
            exec.execute(() -> {
                requestResponse requestResponse;
                Request request;
                Response response = new Response(
                        Response.cmdStatus.OK,
                        "Started working on the command"
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
            });
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
            Exit.setExit(false);
        }
    }
    private static void procedure(){
        Help.fillLst();

        try {
            dao.DateRead(DatabaseAccess.getDBConnection());
        } catch(SQLException e){
            System.out.println(e.getMessage() + " line 180 Server");
        }
    }
    private static synchronized requestResponse reading(SocketChannel server, Response response) throws IOException{
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
    private static synchronized void commandExecuting(String message, Request request, SocketChannel server){
        Runnable task = () -> {

            String command = request.commandName;
            String answer = "Base value for 'answer'";
            int flag = 0;
            Response.cmdStatus respStatus = Response.cmdStatus.OK;
            Response response = new Response(
                    respStatus,
                    answer
            );

            if(command.equals("addUser")) {
                try {
                    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>(){};
                    ArrayList<String> arr = (ArrayList<String>) request.getArgumentAs(typeToken);
                    Connection connection = DatabaseAccess.getDBConnection();
                    if (!DB_User.isInBD(request.getUser_login(), connection)) {
                        User user = new User(arr.get(0), arr.get(1));
                        DB_User.addUser(user, DatabaseAccess.getDBConnection());
                        answer = "User was successfully logged in";
                    } else {
                        String maybe_password = arr.get(1);
                        String salt = DB_User.getUserSalt(request.getUser_login(), DatabaseAccess.getDBConnection());
                        if(User.secure_password(maybe_password, salt).equals(DB_User.getUserPassword(request.getUser_login(), connection))){
                            answer = "User was successfully authorized";
                        }
                        else{
                            answer = "You entered incorrect password!! bruh";
                            respStatus = Response.cmdStatus.ERROR;
                        }
                    }
                } catch (SQLException | NoSuchAlgorithmException e) {
                    respStatus = Response.cmdStatus.ERROR;
                    answer = e.getMessage() + "هيكات تشثونيوس ، أرتميس تشثونيوس ، هيرميس خثونيوس ، وجهوا كراهيتكم إلى Phanagora و Demetrius ، وفي الحانة الخاصة بهم ، وعلى أموالهم وممتلكاتهم. سأربط عدوي ديمتريوس وفاناغورا بالدم والتراب بكل الموتى. لن يتم إطلاق سراحك بحلول الدورة القادمة التي مدتها أربع سنوات. سأقيدك بمثل هذه التعويذة ، ديميتريوس ، قدر الإمكان ، وسأرمي أذن كلب على لسانك. ";
                }
                response = new Response(
                        respStatus,
                        message + "\n" + answer
                );
            }
            else if(command.equals("getUser")){
                try {
                    Response.cmdStatus respStatus1 = Response.cmdStatus.OK;
                    User user = DB_User.getUser(request.getUser_login(), DatabaseAccess.getDBConnection());
                    response = new Response(
                            respStatus1,
                            user
                    );
                }
                catch(NoSuchAlgorithmException | SQLException e){
                    Response.cmdStatus respStatus1 = Response.cmdStatus.ERROR;
                    answer = e.getMessage();
                    response = new Response(
                            respStatus1,
                            answer
                    );
                }
            }
            else {
                for (Commands cm : cmd) {
                    if (cm.getName().equals(command)) {
                        flag += 1;
                        try {
                            stringQueue stringQueue = cm.requestExecute(dao, q, request);
                            q = stringQueue.getQueue();
                            answer = stringQueue.getString();
                        } catch (IOException | SQLException e) {
                            respStatus = Response.cmdStatus.ERROR;
                            answer = e.getMessage() + " on line 152 Server";
                        }
                    }
                }
                if (flag == 0) {
                    respStatus = Response.cmdStatus.ERROR;
                    answer = "Unknown command. Type help for the list of commands";
                }
                response = new Response(
                        respStatus,
                        message + "\n" + answer
                );
            }
            send(response, server);
        };
        Thread thread = new Thread(task);
        thread.start();
    }
    private static synchronized void send(Response response, SocketChannel server){
        executorService.execute(() -> {
            try {
                //System.out.println(response.getArgumentAs(String.class));
                server.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));
            }
            catch(IOException e){
                System.out.println(e.getMessage());
                //server.write(ByteBuffer.wrap((e.getMessage() + " on line 142 in Server.java").getBytes(StandardCharsets.UTF_8)));
            }
        });
    }
    private static synchronized void send(String message, SocketChannel server){
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
