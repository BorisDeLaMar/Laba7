package ru.itmo.server.src.Comms;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.io.BufferedReader;
import java.util.LinkedHashSet;
import ru.itmo.common.connection.Request;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;

public class Show implements Commands{
	/** 
	 *Prints collection
	 *@author BARIS 
	*/
	
	public String show(DAO<Worker> dao) {
		Sort srt = new Sort();
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(srt.sort(dao));
		StringBuilder list = new StringBuilder();
		String person;
		for(Worker w : bd) {
			person = w.toString() + "<============================>\n";
			list.append(person);
		}
		return list.toString();
	}
	
	@Override
	public String getName() {
		return "show";
	}
	@Override 
	public String getGist() {
		return "prints all the elements of collection";
	}
	@Override
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws SQLException {
		Show show = new Show();
		q = History.cut(q);
		q.addLast(show);

		String reply = this.show(dao);
		return new stringQueue(reply, q);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException {
		q = History.cut(q);
		q.addLast(this);

		String reply = this.show(dao);
		return new stringQueue(reply, q);
	}
}
