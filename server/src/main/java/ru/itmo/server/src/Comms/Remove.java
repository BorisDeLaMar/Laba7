package ru.itmo.server.src.Comms;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.io.*;

import ru.itmo.common.DatabaseAccess;
import ru.itmo.common.connection.Request;
import ru.itmo.server.src.Comms.database.DB_Worker;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;

public class Remove implements Commands{
	/** 
	 *Removes element by id
	 *@author BARIS 
	*/
	public String just_remove_by_id(DAO<Worker> dao, long id, String user_login) throws SQLException{
		Worker w = dao.get(id);
		if(w == null) {
			return "There's no guy with such id";
		}
		else {
			if (w.getUser_login().equals(user_login)) {
				dao.delete(w);
				DB_Worker.deleteWorker(id, DatabaseAccess.getDBConnection());
				return "Worker successfully removed from collection";
			} else {
				return "You are not allowed to kick " + w.getUser_login() + "'s worker out!";
			}
		}
	}
	public String remove_by_id(DAO<Worker> dao, long id, String user_login) throws SQLException{
			Worker w = dao.get(id);
			if(w == null) {
				return "\nThere's no guy with such id as " + id + "\n";
			}
			else {
				if (w.getUser_login().equals(user_login)) {
					dao.delete(w);
					DB_Worker.deleteWorker(id, DatabaseAccess.getDBConnection());
					return "\nWorker successfully removed from collection\n";
				} else {
					return "\nYou are not allowed to kick " + w.getUser_login() + "'s worker out!\n";
				}
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
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws SQLException, IOException{
		Remove rmv = new Remove();
		q = History.cut(q);
		q.addLast(rmv);

		String reply = "";
		try {
			long id = Long.parseLong(on.readLine().split(" ")[0]);
			reply += rmv.remove_by_id(dao, id, user_login);
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
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException, SQLException{
		Remove rmv = new Remove();
		q = History.cut(q);
		q.addLast(rmv);

		String reply = rmv.just_remove_by_id(dao, request.getArgumentAs(Long.class), request.getUser_login());
		return new stringQueue(reply, q);
	}
}
