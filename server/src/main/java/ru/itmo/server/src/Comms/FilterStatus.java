package ru.itmo.server.src.Comms;
import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.GivenClasses.Status;
import ru.itmo.server.src.GivenClasses.Worker;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class FilterStatus implements Commands{
	/** 
	 *Prints every elements which status is lower than given
	 *@author BARIS 
	*/
	public String just_filter_less_than_status(DAO<Worker> dao, Status state){
		Sort srt = new Sort();
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(srt.sort(dao));
		bd  = new LinkedHashSet<Worker>(bd.stream().filter(s -> state.isBetter(s.getStatus())).collect(Collectors.toSet()));
		String list = "";
		for(Worker w : bd) {
			String person = "";
			person = w.toString();
				//person = "\nName: " + w.getName() + "\nSalary: " + w.getSalary() + "\nPosition: " + w.getPosition().toString() + "\nStatus: " + w.getStatus().toString() + "\nOrganization: " + w.getOrganization().getName() + ", " + w.getOrganization().getType().toString() + "\nCoordinates: " + w.getCoordinates().getAbscissa() + ", " + w.getCoordinates().getOrdinate();;
			list += person;
		}
		for(Worker w : bd) {
			String person = "";
			if(state.isBetter(w.getStatus())) {
				person = w.toString();
				//person = "\nName: " + w.getName() + "\nSalary: " + w.getSalary() + "\nPosition: " + w.getPosition().toString() + "\nStatus: " + w.getStatus().toString() + "\nOrganization: " + w.getOrganization().getName() + ", " + w.getOrganization().getType().toString() + "\nCoordinates: " + w.getCoordinates().getAbscissa() + ", " + w.getCoordinates().getOrdinate();;
			}
			list += person;
		}
		return list;
	}
	public String filter_less_than_status(DAO<Worker> dao, BufferedReader on) throws IOException{
		Status state = null;
			try {
				state = Status.valueOf(on.readLine().split(" ")[0]);
				//System.out.println(state.toString() + "\nFilterStatus line 34");
			}
			catch(IllegalArgumentException e) {
				String reply = GistStaff.getReply();
				reply += "\nAvailable status values:" + Status.strConvert() + "\n";
				GistStaff.setReply(reply);
			}
			catch(NullPointerException e) {
				String reply = GistStaff.getReply();
				reply += "\nNo status to read from file for filter_less_than_status\n";
				GistStaff.setReply(reply);
			}
		Sort srt = new Sort();
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(srt.sort(dao));
		String list = "";
		if(!(state == null)) {
			for (Worker w : bd) {
				String person = "";
				if (state.isBetter(w.getStatus())) {
					person = w.toString();
					//person = "\nName: " + w.getName() + "\nSalary: " + w.getSalary() + "\nPosition: " + w.getPosition().toString() + "\nStatus: " + w.getStatus().toString() + "\nOrganization: " + w.getOrganization().getName() + ", " + w.getOrganization().getType().toString() + "\nCoordinates: " + w.getCoordinates().getAbscissa() + ", " + w.getCoordinates().getOrdinate();;
				}
				list += person;
			}
		}
		return list;
	}
	
	@Override
	public String getGist() {
		return "prints elements with status which is less than given";
	}
	@Override
	public String getName() {
		return "filter_less_than_status";
	}
	@Override
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on) throws IOException{
		FilterStatus st = new FilterStatus();
		q = History.cut(q);
		q.addLast(st);

		String filter = st.filter_less_than_status(dao, on);
		if(GistStaff.getFlag()) {
			String reply = GistStaff.getReply();
			reply += filter;
			GistStaff.setReply(reply);
		}

		return q;
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException{
		FilterStatus st = new FilterStatus();
		q = History.cut(q);
		q.addLast(st);
		String filterStatus = st.just_filter_less_than_status(dao, request.getArgumentAs(Status.class));

		Response response = new Response(
				Response.cmdStatus.OK,
				filterStatus
		);
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
}
