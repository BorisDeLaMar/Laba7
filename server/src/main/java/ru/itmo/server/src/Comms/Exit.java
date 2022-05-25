package ru.itmo.server.src.Comms;
//import GivenClasses.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.GivenClasses.Worker;

import java.io.BufferedReader;

public class Exit implements Commands{
	private static boolean flag = true;
	/** 
	 *Shuts down the programm
	 *@author BARIS  
	*/
	public static void exit(DAO<Worker> dao) {
		Save save = new Save();
		String filepath = System.getenv("FPATH");
		save.save(dao, filepath);
		//System.out.print("Bye-bye");
		//flag = false;
	}
	public static void just_exit(DAO<Worker> dao) {
		Save save = new Save();
		String filepath = System.getenv("FPATH");
		save.save(dao, filepath);
		//flag = false;
	}
	public static boolean getExit() {
		return flag;
	}
	public static void setExit(boolean flagi){ flag = flagi;}
	
	@Override
	public String getGist() {
		return "shuts the programm down in a very rude way";
	}
	@Override
	public String getName() {
		return "exit";
	}
	@Override
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on){
		Exit exe = new Exit();
		q = History.cut(q);
		q.addLast(exe);
		Exit.exit(dao);



		return q;
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException {
		Exit exe = new Exit();
		q = History.cut(q);
		q.addLast(exe);
		Exit.just_exit(dao);

		Response response = new Response(
				Response.cmdStatus.OK,
				"Bye-bye"
		);
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
}
