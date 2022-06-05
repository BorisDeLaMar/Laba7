package ru.itmo.client;

import ru.itmo.common.authorization.User;
import ru.itmo.common.connection.*;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ServerAPIImpl implements ServerAPI{
    @Override
    public Response add(ArrayList<String> args, User user) {
        Request request = new Request(
                "add",
                user.getLogin(),
                args
        );

        try {
            return sendToServer(request);
        } catch (IOException e) {
            /*handle error*/
            System.out.println(e.getMessage());
            return null;
            //throw new RuntimeException(e);
        }
    }
    @Override
    public Response add_if_min(ArrayList<String> args, User user) {
        Request request = new Request(
                "add_if_min",
                user.getLogin(),
                args
        );

        try {
            return sendToServer(request);
        } catch (IOException e) {
            /*handle error*/
            System.out.println(e.getMessage());
            return null;
            //throw new RuntimeException(e);
        }
    }
    @Override
    public Response info(User user){
        Request request = new Request(
                "info",
                user.getLogin(),
                null
        );

        try {
            return sendToServer(request);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public Response show(User user){
        Request request = new Request(
                "show",
                user.getLogin(),
                null
        );

        try {
            return sendToServer(request);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public Response clear(User user){
        Request request = new Request(
                "clear",
                user.getLogin(),
                null
        );

        try {
            return sendToServer(request);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public Response exit(User user){
        Request request = new Request(
                "exit",
                user.getLogin(),
                null
        );

        try {
            return sendToServer(request);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public Response history(User user){
        Request request = new Request(
                "history",
                user.getLogin(),
                null
        );

        try {
            return sendToServer(request);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public Response execute_script(String filename, User user){

        Request request = new Request(
                "execute_script",
                user.getLogin(),
                filename
        );

        try {
            return sendToServer(request);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public Response filter_less_than_status(String state, User user){

        Request request = new Request(
                "filter_less_than_status",
                user.getLogin(),
                state
        );

        try {
            return sendToServer(request);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public Response help(User user){

        Request request = new Request(
                "help",
                user.getLogin(),
                null
        );

        try {
            return sendToServer(request);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public Response print_descending(User user){

        Request request = new Request(
                "print_descending",
                user.getLogin(),
                null
        );

        try {
            return sendToServer(request);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public Response print_unique_status(User user){

        Request request = new Request(
                "print_unique_status",
                user.getLogin(),
                null
        );

        try {
            return sendToServer(request);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public Response remove(long id, User user){

        Request request = new Request(
                "remove_by_id",
                user.getLogin(),
                id
        );

        try {
            return sendToServer(request);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public Response remove_lower(long id, User user){

        Request request = new Request(
                "remove_lower",
                user.getLogin(),
                id
        );

        try {
            return sendToServer(request);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public Response update(ArrayList<String> args, User user){

        Request request = new Request(
                "update",
                user.getLogin(),
                args
        );

        try {
            return sendToServer(request);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Response sendToServer(Request request) throws IOException {
        SocketChannel client = null;
        SocketAddress address = new InetSocketAddress("localhost", 65100);
        try {
            client = SocketChannel.open();
            client.connect(address);
        }
        catch(UnknownHostException e){
            System.out.println(e.getMessage());
            //System.exit(228);
        }
        try {
            client.write(ByteBuffer.wrap(request.toJson().getBytes(StandardCharsets.UTF_8)));
            byte[] buffer = new byte[8192];
            int amount = client.read(ByteBuffer.wrap(buffer));
            byte[] responseBytes = new byte[amount];
            System.arraycopy(buffer, 0, responseBytes, 0, amount);
            String json = new String(responseBytes, StandardCharsets.UTF_8);
            client.close();
            return Response.fromJson(json);
        }
        catch(NotYetConnectedException | NullPointerException e){
            System.out.println(e.getMessage());
            //System.exit(228);
            client.close();
            return null;
        }
    }
}
