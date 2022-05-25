package ru.itmo.server.src.Comms;

import java.io.IOException;
import java.io.BufferedReader;
//import java.time.format.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.time.LocalDateTime;
import java.time.ZoneId;
import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.GivenClasses.Worker;

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
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on){
		Info inf = new Info();
		q = History.cut(q);
		q.addLast(inf);
		String info = inf.info(dao);

		if(GistStaff.getFlag()) {
			String reply = GistStaff.getReply();
			reply += info;
			GistStaff.setReply(reply);
		}

		return q;
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException{
		q = History.cut(q);
		q.addLast(this);
		String info = this.info(dao);

		Response response = new Response(
				Response.cmdStatus.OK,
				info
		);
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
}
