package ru.itmo.server.src.Comms;

import ru.itmo.common.connection.Request;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

public class ExecuteScript implements Commands{
	protected static ArrayDeque<Commands> q;

	private static final Stack<String> file_bd = new Stack<String>();
	public static stringQueue just_execute_script(DAO<Worker> dao, ArrayDeque<Commands> qu, Request request) throws IOException, SQLException {
		StringBuilder reply = new StringBuilder();
		ArrayList<Commands> cmd = Help.getLst();
		String filename = request.getArgumentAs(String.class);
		q = qu;

		try(BufferedReader on = new BufferedReader(new FileReader(filename))){
			boolean f = true;
			for(String s : file_bd) {
				if(filename.equals(s)) {
					f = false;
					reply.append("\nFile with name ").append(filename).append(" was already processed. I've killed it by myself!\n");
				}
			}
			if(f) {
				file_bd.push(filename);
				String command = "";
				while(((command = on.readLine()) != null && !command.isEmpty()) && Exit.getExit()) {//Exit.getExit() ??
					command = command.split(" ")[0];
					int flag = 0;
					for (Commands cm : cmd) {
						if (cm.getName().equals(command)) {
							flag += 1;
							stringQueue answer = cm.executeCommand(dao, q, on, request.getUser_login());
							q = answer.getQueue();
							reply.append(answer.getString());
						}
					}
					if(flag == 0) {
						reply.append("\nUnknown command. Type 'help' for the list of available commands\n");
					}
				}
			}

			on.close();
			return new stringQueue(reply.toString(), q);
		}
		catch(java.io.FileNotFoundException e) {
			reply.append("\nThere is no file with such name, ").append(filename).append(" does not exist. Watch out and try function again!\n");
			return new stringQueue(reply.toString(), q);
		}
		catch(IOException e) {
			reply.append("\n").append(e.getMessage()).append("\n");
			return new stringQueue(reply.toString(), q);
		}
	}
	/**
	 *Executes script from file
	 *@author BARIS
	 */
	public static stringQueue execute_script(DAO<Worker> dao, ArrayDeque<Commands> qu, BufferedReader in, String user_login) throws IOException, SQLException{
		q = qu;
		StringBuilder reply = new StringBuilder();
		String filename = "";
		filename = in.readLine();
		ArrayList<Commands> cmd = Help.getLst();
		try(BufferedReader on = new BufferedReader(new FileReader(filename))){
			boolean f = true;
			for(String s : file_bd) {
				if(filename.equals(s)) {
					f = false;
					reply.append("\nFile with name ").append(filename).append(" was already processed. I've killed it by myself!\n");
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
							stringQueue answer = cm.executeCommand(dao, q, on, user_login);
							q = answer.getQueue();
							reply.append(answer.getString());
						}
					}
					if(flag == 0) {
						reply.append("\nUnknown command. Type 'help' for the list of available commands\n");
					}
				}
			}

			on.close();
			return new stringQueue(reply.toString(), q);
		}
		catch(java.io.FileNotFoundException e) {
			reply.append("\nThere's no file with such name, ").append(filename).append(" does not exist. Watch out and try function again!\n");
			return new stringQueue(reply.toString(), q);
		}
		catch(IOException e) {
			reply.append("\n").append(e.getMessage()).append("\n");
			return new stringQueue(reply.toString(), q);
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
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws IOException, SQLException{
		ExecuteScript exec = new ExecuteScript();
		q = History.cut(q);
		q.addLast(exec);

		//execute_script C:\vpd\PudgePudgePudgePudge.txt execute_script C:\vpd\PudgePudgePudgePudge.txt
		return ExecuteScript.execute_script(dao, q, on, user_login);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException, SQLException {
		ExecuteScript exec = new ExecuteScript();
		q = History.cut(q);
		q.addLast(exec);

		ExecuteScript.file_bdCleaner();
		return ExecuteScript.just_execute_script(dao, q, request);
	}
	public static void file_bdCleaner() {
		file_bd.clear();
	}
}
