package ru.itmo.server.src.Comms;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.BufferedReader;
import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.GivenClasses.Worker;

public class PrintDescending implements Commands{
	/** 
	 *Prints elements in descending order
	 *@author BARIS 
	*/
	
	public String print_descending(DAO<Worker> dao) {
		Sort srt = new Sort();
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(srt.sort(dao));
		String list = "";
		ArrayList<Worker> sorted = new ArrayList<Worker>(bd);
		for(int i = bd.size() - 1; i >= 0; i--) {
			String person = "";
			Worker w = sorted.get(i);
			person = w.toString();
			//person = "\nName: " + w.getName() + "\nSalary: " + w.getSalary() + "\nPosition: " + w.getPosition().toString() + "\nStatus: " + w.getStatus().toString() + "\nOrganization: " + w.getOrganization().getName() + ", " + w.getOrganization().getType().toString() + "\nCoordinates: " + w.getCoordinates().getAbscissa() + ", " + w.getCoordinates().getOrdinate();
			list += person;
		}
		return list;
	}
	
	@Override
	public String getGist() {
		return "prints all the elements in dedcending order";
	}
	@Override
	public String getName() {
		return "print_descending";
	}
	@Override
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on){
		PrintDescending prnt = new PrintDescending();
		q = History.cut(q);
		q.addLast(prnt);
		String prnt_desc = prnt.print_descending(dao);

		if(GistStaff.getFlag()) {
			String reply = GistStaff.getReply();
			reply += prnt_desc;
			GistStaff.setReply(reply);
		}

		return q;
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException {
		PrintDescending prnt = new PrintDescending();
		q = History.cut(q);
		q.addLast(prnt);
		String prnt_desc = prnt.print_descending(dao);

		Response response = new Response(
				Response.cmdStatus.OK,
				prnt_desc
		);
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
}
