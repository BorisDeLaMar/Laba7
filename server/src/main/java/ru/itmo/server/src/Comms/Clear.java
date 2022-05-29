package ru.itmo.server.src.Comms;

import ru.itmo.common.connection.Request;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;
import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Clear implements Commands{
	/**
	 *Clear function
	 *@author BARIS
	 */
	public void clear(DAO<Worker> d) {
		ArrayList<Worker> bd = new ArrayList<Worker>(d.getAll());
		bd.stream().forEach(s -> d.delete(s));
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
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on) {
		Clear cl = new Clear();
		q = History.cut(q);
		q.addLast(cl);

		cl.clear(dao);
		String reply = "Collection was successfully cleaned up";

		return new stringQueue(reply, q);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException {
		Clear cl = new Clear();
		q = History.cut(q);
		q.addLast(cl);

		cl.clear(dao);
		String reply = "Collection was successfully cleaned up";

		return new stringQueue(reply, q);
	}
}
