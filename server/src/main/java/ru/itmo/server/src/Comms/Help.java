package ru.itmo.server.src.Comms;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.io.BufferedReader;
import java.util.ArrayList;
import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.GivenClasses.Worker;

public class Help extends AbstractHelp implements Commands{
	/** 
	 *Prints available commands
	 *@author BARIS 
	*/
	public String help(ArrayList<Commands> lst) {
		String list = "";
		for(Commands cm : lst) {
			String person = "";
			person = cm.getName() + ": " + cm.getGist() + "\n";
			list += person;
		}
		return list; //добавить команд в lst
	}
	
	@Override
	public String getGist() {
		return "shows the information about available commands";
	}
	@Override
	public String getName() {
		return "help";
	}
	@Override
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on){
		Help hlp = new Help();
		q = History.cut(q);
		q.addLast(hlp);
		String help = hlp.help(Help.getLst());

		if(GistStaff.getFlag()) {
			String reply = GistStaff.getReply();
			reply += help;
			GistStaff.setReply(reply);
		}

		return q;
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException {
		Help hlp = new Help();
		q = History.cut(q);
		q.addLast(hlp);
		//Help.fillLst();
		String help = hlp.help(Help.getLst());

		Response response = new Response(
				Response.cmdStatus.OK,
				help
		);
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
	public static ArrayList<Commands> getLst() {
		return lst;
	}
	public static void fillLst() {
		addToList();
	}
}
