package ru.itmo.server.src.Comms;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.io.BufferedReader;
import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.GivenClasses.Worker;

public class History implements Commands{
	/** 
	 *Prints last seven commands
	 *@param DAO<Worker> dao, Status state
	 *@author BARIS 
	*/
	private ArrayDeque<Commands> q;
	public String history(ArrayDeque<Commands> q) {
		this.q = new ArrayDeque<Commands>(q);
		String list = ""; 
		while(this.q.peek() != null) {
			//System.out.println(this.q.pop().getName());
			Commands cmd = this.q.pop();
			list += cmd.getName() + "\n";
		}
		return list;
		
	}
	public static ArrayDeque<Commands> cut(ArrayDeque<Commands> q) {
		if(q != null && q.size() == 7) {
			q.removeFirst();
		}
		return q;
	}
	
	@Override
	public String getGist() {
		return "prints last 7 commands";
	}
	@Override
	public String getName() {
		return "history";
	}
	@Override
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on){
		History history = new History();
		String hist = history.history(q);

		if(GistStaff.getFlag()) {
			String reply = GistStaff.getReply();
			reply += hist;
			GistStaff.setReply(reply);
		}

		q = History.cut(q);
		q.addLast(history);
		return q;
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException {
		History history = new History();
		String hist = history.history(q);
		q = History.cut(q);
		q.addLast(history);

		Response response = new Response(
				Response.cmdStatus.OK,
				hist
		);
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
}
