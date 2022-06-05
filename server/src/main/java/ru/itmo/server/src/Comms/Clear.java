package ru.itmo.server.src.Comms;

import ru.itmo.common.DatabaseAccess;
import ru.itmo.common.connection.Request;
import ru.itmo.server.src.Comms.database.DB_Worker;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Clear implements Commands{
	/**
	 *Clear function
	 *@author BARIS
	 */
	public void clear(DAO<Worker> dao, String user_login) throws SQLException{
		ArrayList<Worker> bd = new ArrayList<Worker>(dao.getAll());
		for(Worker w : bd){
			if(w.getUser_login().equals(user_login)){
				dao.delete(w);
				DB_Worker.deleteWorker(w.getId(), DatabaseAccess.getDBConnection());
			}
		}
	}
	
	@Override
	public String getGist() {
		return "clears collection";
	}
	@Override
	public String getName() {
		return "clear";
	}
	@Override
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws SQLException {
		Clear cl = new Clear();
		q = History.cut(q);
		q.addLast(cl);

		cl.clear(dao, user_login);
		String reply = "Collection was successfully cleaned up";

		return new stringQueue(reply, q);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException, SQLException {
		Clear cl = new Clear();
		q = History.cut(q);
		q.addLast(cl);

		cl.clear(dao, request.getUser_login());
		String reply = "Collection was successfully cleaned up";

		return new stringQueue(reply, q);
	}
}
