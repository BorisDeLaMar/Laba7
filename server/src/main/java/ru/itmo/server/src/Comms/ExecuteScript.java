package ru.itmo.server.src.Comms;
import java.io.BufferedReader;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Stack;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.GivenClasses.Worker;

public class ExecuteScript implements Commands{
	protected static ArrayDeque<Commands> q;

	private static final Stack<String> file_bd = new Stack<String>();
	public static ArrayDeque<Commands> just_execute_script(DAO<Worker> dao, ArrayDeque<Commands> qu, BufferedReader in, Request request, SocketChannel client) throws IOException{
		String reply = GistStaff.getReply();
		ArrayList<Commands> cmd = Help.getLst();
		GistStaff.setFlag(true);

		String filename = request.getArgumentAs(String.class);
		q = qu;

		try(BufferedReader on = new BufferedReader(new FileReader(filename))){
			boolean f = true;
			for(String s : file_bd) {
				if(filename.equals(s)) {
					f = false;
					reply = GistStaff.getReply();
					reply += "\nFile with name " + filename + " was already processed. I've killed it by myself!\n";
					GistStaff.setReply(reply);
				}
			}
			if(f) {
				file_bd.push(filename);
				String command = "";
				while(((command = on.readLine()) != null && !command.isEmpty()) && Exit.getExit()) {//Exit.getExit() ??
					//in.hasNext()
					//String[] exec = new String[1];
					//String command = line[0];
					//System.out.println(cmd.size());
					//try-ем надо оборачивать сам while?
					command = command.split(" ")[0];
					int flag = 0;
					for (Commands cm : cmd) {
						if (cm.getName().equals(command)) {
							flag += 1;
							q = cm.executeCommand(dao, q, on);
							reply = GistStaff.getReply();
						}
					}
					if(flag == 0) {
						reply = GistStaff.getReply();
						reply += "\nUnknown command. Type 'help' for the list of available commands\n";
						GistStaff.setReply(reply);
					}
				}
			}
			GistStaff.setReply(reply);
			on.close();
			return q;
		}
		catch(java.io.FileNotFoundException e) {
			//System.out.println(filename); Кто-то удваивает скобки (гсон??) два раза
			reply = GistStaff.getReply();
			reply += "\nThere is no file with such name, " + filename + " does not exist. Watch out and try function again!\n";
			GistStaff.setReply(reply);
			return q;
		}
		catch(IOException e) {
			reply = GistStaff.getReply();
			reply += "\n" + e.getMessage() + "\n";
			GistStaff.setReply(reply);
			return q;
		}
	}
	public static ArrayDeque<Commands> execute_script(DAO<Worker> dao, ArrayDeque<Commands> qu, BufferedReader in) throws IOException{
		q = qu;
		String reply = GistStaff.getReply();
		String filename = "";
		filename = in.readLine();
		ArrayList<Commands> cmd = Help.getLst();
		GistStaff.setFlag(true);
		/** 
		 *Executes script from file
		 *@param All the available commands
		 *@author BARIS  
		*/
		try(BufferedReader on = new BufferedReader(new FileReader(filename))){
			boolean f = true;
			for(String s : file_bd) {
				if(filename.equals(s)) {
					f = false;
					reply = GistStaff.getReply();
					reply += "\nFile with name " + filename + " was already processed. I've killed it by myself!\n";
					GistStaff.setReply(reply);
				}
			}
			if(f) {
				file_bd.push(filename);
				String command;
				while((command = on.readLine()) != null && !command.isEmpty()) {
					//in.hasNext()
					//String[] exec = new String[1];
					command = command.split(" ")[0];
					//System.out.println(cmd.size());
					//try-ем надо оборачивать сам while?
					int flag = 0;
					for (Commands cm : cmd) {
						if (cm.getName().equals(command)) {
							flag += 1;
							q = cm.executeCommand(dao, q, on);
							reply = GistStaff.getReply();
						}
					}
					if(flag == 0) {
						reply = GistStaff.getReply();
						reply += "\nUnknown command. Type 'help' for the list of available commands\n";
						GistStaff.setReply(reply);
					}
				}
			}
			GistStaff.setReply(reply);
			on.close();
			return q;
		}
		catch(java.io.FileNotFoundException e) {
			reply = GistStaff.getReply();
			reply += "\nThere's no file with such name, " + filename + " does not exist. Watch out and try function again!\n";
			GistStaff.setReply(reply);
			return q;
		}
		catch(IOException e) {
			reply = GistStaff.getReply();
			reply += "\n" + e.getMessage() + "\n";
			GistStaff.setReply(reply);
			return q;
		}
	}
	
	
	@Override
	public String getGist() {
		return "executes and complete the script from given file";
	}
	@Override
	public String getName() {
		return "execute_script";
	}
	@Override
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on) throws IOException{
		ExecuteScript exec = new ExecuteScript();
		q = History.cut(q);
		q.addLast(exec);
		q = ExecuteScript.execute_script(dao, q, on); //execute_script C:\vpd\PudgePudgePudgePudge.txt execute_script C:\vpd\PudgePudgePudgePudge.txt
		return q;
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException{
		ExecuteScript exec = new ExecuteScript();
		q = History.cut(q);
		q.addLast(exec);

		Response response;
		q = ExecuteScript.just_execute_script(dao, q, on, request, client);
		response = new Response(
				Response.cmdStatus.OK,
				GistStaff.getReply()
		);
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
	public static void file_bdCleaner() {
		file_bd.clear();
	}
}
