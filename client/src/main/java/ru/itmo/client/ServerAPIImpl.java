package ru.itmo.client;

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
    public Response add(ArrayList<String> args) {
        Request request = new Request(
                "add",
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
    public Response add_if_min(ArrayList<String> args) {
        Request request = new Request(
                "add_if_min",
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
    public Response info(){
        Request request = new Request(
                "info",
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
    public Response show(){
        Request request = new Request(
                "show",
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
    public Response clear(){
        Request request = new Request(
                "clear",
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
    public Response exit(){
        Request request = new Request(
                "exit",
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
    public Response history(){
        Request request = new Request(
                "history",
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
    public Response execute_script(String filename){

        Request request = new Request(
                "execute_script",
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
    public Response filter_less_than_status(String state){

        Request request = new Request(
                "filter_less_than_status",
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
    public Response help(){

        Request request = new Request(
                "help",
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
    public Response print_descending(){

        Request request = new Request(
                "print_descending",
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
    public Response print_unique_status(){

        Request request = new Request(
                "print_unique_status",
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
    public Response remove(long id){

        Request request = new Request(
                "remove_by_id",
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
    public Response remove_lower(long id){

        Request request = new Request(
                "remove_lower",
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
    public Response update(ArrayList<String> args){

        Request request = new Request(
                "update",
                args
        );

        try {
            return sendToServer(request);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Response sendToServer(Request request) throws IOException {
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
