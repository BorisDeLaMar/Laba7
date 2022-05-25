package ru.itmo.server.src.Comms;

import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.GivenClasses.Worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
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
		String list = "";
		for(Worker w : bd) {
			String person = "";
			//person = w.toString();
			person = w.getName() + ": " + w.getStatus().toString() + "\n";
			list += person;
		}
		return list;
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
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on){
		PrintUniqueStatus prntu = new PrintUniqueStatus();
		q = History.cut(q);
		q.addLast(prntu);
		String prnt_uniq = prntu.print_unique_status(dao);

		if(GistStaff.getFlag()) {
			String reply = GistStaff.getReply();
			reply += prnt_uniq;
			GistStaff.setReply(reply);
		}

		return q;
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException {
		PrintUniqueStatus prntu = new PrintUniqueStatus();
		q = History.cut(q);
		q.addLast(prntu);
		String prnt_uniq = prntu.print_unique_status(dao);

		Response response = new Response(
				Response.cmdStatus.OK,
				prnt_uniq
		);
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
}
