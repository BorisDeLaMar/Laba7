package ru.itmo.server.src.Comms;

import ru.itmo.common.connection.Request;
import ru.itmo.server.src.GivenClasses.Status;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class FilterStatus implements Commands{
	/** 
	 *Prints every element which status is lower than given
	 *@author BARIS 
	*/
	public String just_filter_less_than_status(DAO<Worker> dao, Status state){

		Sort srt = new Sort();
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(srt.sort(dao));
		StringBuilder list = new StringBuilder();
		for(Worker w : bd) {
			String person = "";
			//System.out.println(state + " 25 line filter less than status");
			if(state.isBetter(w.getStatus())) {
				person = w.toString();
			}
			list.append(person);
		}
		return list.toString();
	}
	public String filter_less_than_status(DAO<Worker> dao, BufferedReader on) throws IOException{
		Status state = null;
		String reply = "";
		try {
			state = Status.valueOf(on.readLine().split(" ")[0]);
			//System.out.println(state.toString() + "\nFilterStatus line 34");
		}
		catch(IllegalArgumentException e) {
			reply += "\nAvailable status values:" + Status.strConvert() + "\n";
			return reply;
		}
		catch(NullPointerException e) {
			reply += "\nNo status to read from file for filter_less_than_status\n";
			return reply;
		}
		Sort srt = new Sort();
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(srt.sort(dao));
		StringBuilder list = new StringBuilder();
		if(state != null) {
			for (Worker w : bd) {
				String person = "";
				if (state.isBetter(w.getStatus())) {
					person = w.toString();
				}
				list.append(person);
			}
		}
		return list.toString();
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
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws IOException, SQLException {
		FilterStatus st = new FilterStatus();
		q = History.cut(q);
		q.addLast(st);

		String reply = st.filter_less_than_status(dao, on);
		return new stringQueue(reply, q);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException{
		FilterStatus st = new FilterStatus();
		q = History.cut(q);
		q.addLast(st);

		String reply = "";
		try {
			Status state = Status.valueOf(request.getArgumentAs(String.class));
			reply = st.just_filter_less_than_status(dao, state);

		} catch(IllegalArgumentException e) {
			reply = e.getMessage();
		}
		return new stringQueue(reply, q);
	}
}
