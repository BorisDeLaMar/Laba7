package ru.itmo.server.src.Comms;

import java.io.IOException;
import java.io.BufferedReader;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.time.LocalDateTime;
import java.time.ZoneId;
import ru.itmo.common.connection.Request;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;

public class Info implements Commands{
	/** 
	 *Prints info about collection
	 *@author BARIS 
	*/
	
	public String info(DAO<Worker> dao) {
		try {
			String filepath = System.getenv("FPATH");
			Path path = Paths.get(filepath);
			BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class); 
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime tm = attrs.creationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			return "File type: .json\n" + "Initializing date: " + tm.format(format) + "\n" + "Size: " + dao.getAll().size() + " workers\n";
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String getGist() {
		return "prints some info about collection";
	}
	@Override
	public String getName() {
		return "info";
	}
	@Override
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws SQLException {
		Info inf = new Info();
		q = History.cut(q);
		q.addLast(inf);

		String reply = inf.info(dao);
		return new stringQueue(reply, q);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException{
		q = History.cut(q);
		q.addLast(this);

		String reply = this.info(dao);
		return new stringQueue(reply, q);
	}
}
