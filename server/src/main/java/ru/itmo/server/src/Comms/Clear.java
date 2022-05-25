package ru.itmo.server.src.Comms;
import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.GivenClasses.Worker;

import java.io.*;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
//import java.util.LinkedHashSet;
import java.util.ArrayList;

public class Clear implements Commands{
	/**
	 *Clear function
	 *@author BARIS
	 */
	public void clear(DAO<Worker> d) {
		ArrayList<Worker> bd = new ArrayList<Worker>(d.getAll());
		bd.stream().forEach(s -> d.delete(s));
		/*for(int i = 0; i < bd.size(); i++) {
			Worker w = bd.get(i);
			d.delete(w);
		}*/
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
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on) {
		Clear cl = new Clear();
		q = History.cut(q);
		q.addLast(cl);
		cl.clear(dao);

		if(GistStaff.getFlag()) {
			String reply = GistStaff.getReply();
			reply += "Collection was successfully cleaned up";
			GistStaff.setReply(reply);
		}

		return q;
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException {
		Clear cl = new Clear();
		q = History.cut(q);
		q.addLast(cl);
		cl.clear(dao);

		Response response = new Response(
				Response.cmdStatus.OK,
				"Collection was successfully cleaned up"
		);
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
}
