package ru.itmo.server.src.Comms;

import ru.itmo.common.connection.Request;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;

public class PrintUniqueStatus implements Commands{
	/** 
	 *Prints status of each element
	 *@author BARIS 
	*/
	
	public String print_unique_status(DAO<Worker> dao) {
		Sort srt = new Sort();
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(srt.sort(dao));
		StringBuilder list = new StringBuilder();
		for(Worker w : bd) {
			String person = "";
			//person = w.toString();
			person = w.getName() + ": " + w.getStatus().toString() + "\n";
			list.append(person);
		}
		return list.toString();
	}
	
	@Override
	public String getGist() {
		return "prints status of each element from collection";
	}
	@Override
	public String getName() {
		return "print_unique_status";
	}
	@Override
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws SQLException {
		PrintUniqueStatus prntu = new PrintUniqueStatus();
		q = History.cut(q);
		q.addLast(prntu);

		String reply = prntu.print_unique_status(dao);
		return new stringQueue(reply, q);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException {
		PrintUniqueStatus prntu = new PrintUniqueStatus();
		q = History.cut(q);
		q.addLast(prntu);

		String reply = prntu.print_unique_status(dao);
		return new stringQueue(reply, q);
	}
}
