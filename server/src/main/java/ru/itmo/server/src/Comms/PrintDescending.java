package ru.itmo.server.src.Comms;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.io.BufferedReader;
import ru.itmo.common.connection.Request;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;

public class PrintDescending implements Commands{
	/** 
	 *Prints elements in descending order
	 *@author BARIS 
	*/
	
	public String print_descending(DAO<Worker> dao) {
		Sort srt = new Sort();
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(srt.sort(dao));
		StringBuilder list = new StringBuilder();
		ArrayList<Worker> sorted = new ArrayList<Worker>(bd);
		for(int i = bd.size() - 1; i >= 0; i--) {
			String person = "";
			Worker w = sorted.get(i);
			person = w.toString();
			list.append(person);
		}
		return list.toString();
	}
	
	@Override
	public String getGist() {
		return "prints all the elements in descending order";
	}
	@Override
	public String getName() {
		return "print_descending";
	}
	@Override
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws SQLException {
		PrintDescending prnt = new PrintDescending();
		q = History.cut(q);
		q.addLast(prnt);

		String reply = prnt.print_descending(dao);
		return new stringQueue(reply, q);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException {
		PrintDescending prnt = new PrintDescending();
		q = History.cut(q);
		q.addLast(prnt);

		String reply = prnt.print_descending(dao);
		return new stringQueue(reply, q);
	}
}
