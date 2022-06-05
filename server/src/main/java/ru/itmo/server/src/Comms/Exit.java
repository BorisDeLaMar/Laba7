package ru.itmo.server.src.Comms;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import ru.itmo.common.connection.Request;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;
import java.io.BufferedReader;

public class Exit implements Commands{
	private static boolean flag = true;
	/** 
	 *Shuts down the programm
	 *@author BARIS  
	*/
	public static String exit(DAO<Worker> dao) {
		Save save = new Save();
		String filepath = System.getenv("FPATH");
		save.save(dao, filepath);

		return "Bye-bye";
	}
	public static boolean getExit() {
		return flag;
	}
	public static void setExit(boolean flagi){ flag = flagi;}
	
	@Override
	public String getGist() {
		return "shuts the programm down in a very rude way";
	}
	@Override
	public String getName() {
		return "exit";
	}
	@Override
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws SQLException {
		Exit exe = new Exit();
		q = History.cut(q);
		q.addLast(exe);

		String reply = Exit.exit(dao);
		return new stringQueue(reply, q);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException {
		Exit exe = new Exit();
		q = History.cut(q);
		q.addLast(exe);

		String reply = Exit.exit(dao);
		return new stringQueue(reply, q);
	}
}
