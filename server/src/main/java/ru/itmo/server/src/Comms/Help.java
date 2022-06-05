package ru.itmo.server.src.Comms;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.io.BufferedReader;
import java.util.ArrayList;
import ru.itmo.common.connection.Request;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;

public class Help extends AbstractHelp implements Commands{
	/** 
	 *Prints available commands
	 *@author BARIS 
	*/
	public String help(ArrayList<Commands> lst) {
		StringBuilder list = new StringBuilder();
		for(Commands cm : lst) {
			String person = "";
			person = cm.getName() + ": " + cm.getGist() + "\n";
			list.append(person);
		}
		return list.toString(); //добавить команд в lst
	}
	
	@Override
	public String getGist() {
		return "shows the information about available commands";
	}
	@Override
	public String getName() {
		return "help";
	}
	@Override
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws SQLException {
		Help hlp = new Help();
		q = History.cut(q);
		q.addLast(hlp);

		String reply = hlp.help(Help.getLst());
		return new stringQueue(reply, q);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException {
		Help hlp = new Help();
		q = History.cut(q);
		q.addLast(hlp);

		String reply = hlp.help(Help.getLst());
		return new stringQueue(reply, q);
	}
	public static ArrayList<Commands> getLst() {
		return lst;
	}
	public static void fillLst() {
		addToList();
	}
}
