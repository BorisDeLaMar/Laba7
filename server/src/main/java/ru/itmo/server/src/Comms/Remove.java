package ru.itmo.server.src.Comms;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.io.*;

import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.GivenClasses.Worker;

public class Remove implements Commands{
	/** 
	 *Removes element by id
	 *@author BARIS 
	*/
	public String just_remove_by_id(DAO<Worker> dao, long id){
		Worker w = dao.get(id);
		if(w == null) {
			return "There's no guy with such id";
		}
		else {
			dao.delete(w);
			return "Worker successfully removed from collection";
		}
	}
	public void remove_by_id(DAO<Worker> dao, long id) {
			Worker w = dao.get(id);
			if(w == null) {
				String reply = GistStaff.getReply();
				reply += "\nThere's no guy with such id as " + id + "\n";
				GistStaff.setReply(reply);
			}
			else {
				String reply = GistStaff.getReply();
				reply += "\nWorker with id " + id + " was successfully removed\n";
				GistStaff.setReply(reply);
				dao.delete(w);
			}
	}
	
	@Override
	public String getGist() {
		return "deletes element";
	}
	@Override
	public String getName() {
		return "remove_by_id";
	}
	@Override
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on) throws IOException{
		Remove rmv = new Remove();
		q = History.cut(q);
		q.addLast(rmv);
		try {
			long id = Long.valueOf(on.readLine().split(" ")[0]);
			rmv.remove_by_id(dao, id);
		}
		catch(IllegalArgumentException e) {
			String reply = GistStaff.getReply();
			reply += "\nId should be type long\n";
			GistStaff.setReply(reply);
		}
		catch(ArrayIndexOutOfBoundsException e) {
			String reply = GistStaff.getReply();
			reply += "\nThere should be an index argument\n";
			GistStaff.setReply(reply);
		}
		return q;
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException{
		Remove rmv = new Remove();
		q = History.cut(q);
		q.addLast(rmv);
		String remove = rmv.just_remove_by_id(dao, request.getArgumentAs(Long.class));

		Response response = new Response(
				Response.cmdStatus.OK,
				remove
		);
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
}
