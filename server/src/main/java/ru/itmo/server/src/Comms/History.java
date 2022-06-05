package ru.itmo.server.src.Comms;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.io.BufferedReader;
import ru.itmo.common.connection.Request;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;

public class History implements Commands{
	public String history(ArrayDeque<Commands> q) {
		/**
		 *Prints last seven commands
		 */
		ArrayDeque<Commands> q1 = new ArrayDeque<Commands>(q);
		StringBuilder list = new StringBuilder();
		while(q1.peek() != null) {
			//System.out.println(this.q.pop().getName());
			Commands cmd = q1.pop();
			list.append(cmd.getName()).append("\n");
		}

		return list.toString();
	}
	public static ArrayDeque<Commands> cut(ArrayDeque<Commands> q) {
		if(q != null && q.size() == 7) {
			q.removeFirst();
		}
		return q;
	}
	
	@Override
	public String getGist() {
		return "prints last 7 commands";
	}
	@Override
	public String getName() {
		return "history";
	}
	@Override
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws SQLException {
		History history = new History();

		String reply = history.history(q);
		q = History.cut(q);
		q.addLast(history);

		return new stringQueue(reply, q);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException {
		History history = new History();

		String reply = history.history(q);
		q = History.cut(q);
		q.addLast(history);

		return new stringQueue(reply, q);
	}
}
