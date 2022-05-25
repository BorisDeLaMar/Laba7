package ru.itmo.server.src.Comms;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.io.BufferedReader;
import java.util.LinkedHashSet;

import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.GivenClasses.Worker;

public class Show implements Commands{
	/** 
	 *Prints collection
	 *@author BARIS 
	*/
	
	public String show(DAO<Worker> dao) {
		Sort srt = new Sort();
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(srt.sort(dao));
		String list = "";
		String person;
		for(Worker w : bd) {
			person = w.toString();
			//person = "Name: " + w.getName() + "\nSalary: " + w.getSalary() + "\nPosition: " + w.getPosition().toString() + "\nStatus: " + w.getStatus().toString() + "\nOrganization: " + w.getOrganization().getName() + ", " + w.getOrganization().getType().toString() + "\nCoordinates: " + w.getCoordinates().getAbscissa() + ", " + w.getCoordinates().getOrdinate() + "\n" + "ID: " + w.getId() + ",\n" + "creationDate: " + w.getCreationDate() + "\n";
			list += person;
		}
		return list;
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
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on){
		Show show = new Show();
		q = History.cut(q);
		q.addLast(show);
		String shw = this.show(dao);

		if(GistStaff.getFlag()) {
			String reply = GistStaff.getReply();
			reply += shw;
			GistStaff.setReply(reply);
		}

		return q;
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException {
		q = History.cut(q);
		q.addLast(this);
		String show = this.show(dao);

		if(GistStaff.getFlag()) {
			String reply = GistStaff.getReply();
			reply += show;
			GistStaff.setReply(reply);
		}

		Response response = new Response(
				Response.cmdStatus.OK,
				show
		);
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
}
