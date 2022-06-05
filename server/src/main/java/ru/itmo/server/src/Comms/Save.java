package ru.itmo.server.src.Comms;

import ru.itmo.common.connection.Request;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Save implements Commands{
	/** 
	 *Saves collection to the file
	 *@author BARIS 
	*/
	
	public void save(DAO<Worker> dao, String filepath) {

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
			LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(dao.getAll());
			out.write("{\n\t\"workers\":[");
			int i = 0;
			for(Worker w : bd) { 
				if(w.getId() > 0) {
					out.write("\n\t\t{\n\t\t\t");
					out.write("\"name\":" + "\"" +w.getName() + "\"" + ",\n\t\t\t");
					out.write("\"salary\":" + "\"" + w.getSalary() + "\"" + ",\n\t\t\t");
					out.write("\"position\":" + "\"" + w.getPosition().toString() + "\"" + ",\n\t\t\t");
					out.write("\"status\":" + "\"" + w.getStatus().toString() + "\"" + ",\n\t\t\t");
					out.write("\"organization\":[\n\t\t\t\t" + "\"" + w.getOrganization().getName() + "\"" + ",\n\t\t\t\t" + "\"" + w.getOrganization().getType().toString() + "\"" +"\n\t\t\t],\n\t\t\t");
					out.write("\"coordinates\":[\n\t\t\t\t" + "\"" + w.getCoordinates().getAbscissa() + "\"" + ",\n\t\t\t\t" + "\"" + w.getCoordinates().getOrdinate() + "\"" +"\n\t\t\t],\n\t\t\t");
					out.write("\"ID\":\"" + w.getId() + "\",\n\t\t\t");
					out.write("\"CreationDate\":\"" + w.getCreationDate() + "\"\n\t\t}");
				}
				/*
				JsonObject val = Json.createObjectBuilder()
						.add("name", w.getName())
						.add("salary", w.getSalary())
						.add("position", w.getPosition().toString())
						.add("status", w.getStatus().toString())
						.add("organization", Json.createArrayBuilder()
								.add(w.getOrganization().getName())
								.add(w.getOrganization().getType().toString())
								.build())
						.add("coordinates", Json.createArrayBuilder()
								.add(w.getCoordinates().getAbscissa())
								.add(w.getCoordinates().getOrdinate())
								.build())
						.build();*/
				if(i != bd.size()-1) {
					out.write(",");
				}
				i += 1;
			}
			out.write("\n\t]\n}");
			out.close();
			System.out.println("Collection saved to file");
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public String getGist() {
		return "saves collection to file";
	}
	@Override
	public String getName() {
		return "save";
	}
	@Override
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws SQLException {
		Save save = new Save();
		q = History.cut(q);
		q.addLast(save);

		String filepath = System.getenv("FPATH");
		save.save(dao, filepath);
		return new stringQueue("", q);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException{

		return new stringQueue("", q);
	}
}
