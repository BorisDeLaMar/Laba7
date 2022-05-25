package ru.itmo.server.src.Comms;

import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.GivenClasses.Worker;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
//import java.util.LinkedHashSet;
import java.io.*;

public class RemoveLower implements Commands{
	Remove rm = new Remove();
	/** 
	 *Removes all the elements lower than given
	 *@author BARIS 
	*/
	public String just_remove_lower(DAO<Worker> dao, long id){
		Worker w = dao.get(id);
		int flag = 0;
		try {
			ArrayList<Worker> dd = new ArrayList<Worker>(dao.getAll());
			for(int i = 0; i < dd.size(); i++) {
				if(dd.get(i).hashCode() < w.hashCode()) {
					rm.remove_by_id(dao, dd.get(i).getId());
					flag += 1;
				}
			}
			return "You have deleted " + flag + " workers";
		}
		catch(NullPointerException e) {
			return "There's no guy with such id";
		}
	}
	public void remove_lower(DAO<Worker> dao, long id) {
		Worker w = dao.get(id);
		if(w == null){
			String reply = GistStaff.getReply();
			reply += "\nThere is no worker with id " + id + "\n";
			GistStaff.setReply(reply);
		}
		else {
			try {
				ArrayList<Worker> dd = new ArrayList<Worker>(dao.getAll());
				for (int i = 0; i < dd.size(); i++) {
					if (dd.get(i).hashCode() < w.hashCode()) {
						rm.remove_by_id(dao, dd.get(i).getId());
					}
				}
			} catch (NullPointerException e) {
				String reply = GistStaff.getReply();
				reply += "\nThere's no guy with such id\n";
				GistStaff.setReply(reply);
			}
		}
	}
	
	@Override
	public String getGist() {
		return "deletes every element lower than given from collection";
	}
	@Override
	public String getName() {
		return "remove_lower";
	}
	@Override
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on) throws IOException{
		RemoveLower rmvl = new RemoveLower();
		q = History.cut(q);
		q.addLast(rmvl);
		try {
			long id = Long.valueOf(on.readLine().split(" ")[0]);
			rmvl.remove_lower(dao, id);
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
		RemoveLower rmv = new RemoveLower();
		q = History.cut(q);
		q.addLast(rmv);
		String remove = rmv.just_remove_lower(dao, request.getArgumentAs(Long.class));

		Response response = new Response(
				Response.cmdStatus.OK,
				remove
		);
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
}
