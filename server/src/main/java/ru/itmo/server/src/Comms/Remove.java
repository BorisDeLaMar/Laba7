package ru.itmo.server.src.Comms;

import java.util.ArrayDeque;
import java.io.*;
import ru.itmo.common.connection.Request;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;

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
	public String remove_by_id(DAO<Worker> dao, long id) {
			Worker w = dao.get(id);
			if(w == null) {
				return "\nThere's no guy with such id as " + id + "\n";
			}
			else {
				dao.delete(w);
				return "\nWorker with id " + id + " was successfully removed\n";
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
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on) throws IOException{
		Remove rmv = new Remove();
		q = History.cut(q);
		q.addLast(rmv);

		String reply = "";
		try {
			long id = Long.parseLong(on.readLine().split(" ")[0]);
			reply += rmv.remove_by_id(dao, id);
		}
		catch(IllegalArgumentException e) {
			reply += "\nId should be type long\n";
		}
		catch(ArrayIndexOutOfBoundsException e) {
			reply += "\nThere should be an index argument\n";
		}

		return new stringQueue(reply, q);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException{
		Remove rmv = new Remove();
		q = History.cut(q);
		q.addLast(rmv);

		String reply = rmv.just_remove_by_id(dao, request.getArgumentAs(Long.class));
		return new stringQueue(reply, q);
	}
}
