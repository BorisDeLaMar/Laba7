package ru.itmo.server.src.Comms;

import ru.itmo.common.connection.Request;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;
import java.util.ArrayDeque;
import java.util.ArrayList;
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
			for (Worker worker : dd) {
				if (worker.hashCode() < w.hashCode()) {
					rm.remove_by_id(dao, worker.getId());
					flag += 1;
				}
			}
			return "You have deleted " + flag + " workers";
		}
		catch(NullPointerException e) {
			return "There's no guy with such id";
		}
	}
	public String remove_lower(DAO<Worker> dao, long id) {
		Worker w = dao.get(id);
		StringBuilder reply = new StringBuilder();
		if(w == null){
			reply.append("\nThere is no worker with id ").append(id).append("\n");
		}
		else {
			try {
				ArrayList<Worker> dd = new ArrayList<Worker>(dao.getAll());
				for (Worker worker : dd) {
					if (worker.hashCode() < w.hashCode()) {
						reply.append(rm.remove_by_id(dao, worker.getId()));
					}
				}
			} catch (NullPointerException e) {
				reply.append("\nThere's no guy with such id\n");
			}
		}
		return reply.toString();
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
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on) throws IOException{
		RemoveLower rmvl = new RemoveLower();
		q = History.cut(q);
		q.addLast(rmvl);

		String reply = "";
		try {
			long id = Long.parseLong(on.readLine().split(" ")[0]);
			reply += rmvl.remove_lower(dao, id);
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
		RemoveLower rmv = new RemoveLower();
		q = History.cut(q);
		q.addLast(rmv);

		String reply = rmv.just_remove_lower(dao, request.getArgumentAs(Long.class));
		return new stringQueue(reply, q);
	}
}
