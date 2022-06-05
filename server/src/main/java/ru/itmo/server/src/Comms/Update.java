package ru.itmo.server.src.Comms;

import java.sql.SQLException;
import java.util.ArrayDeque;
import com.google.gson.reflect.TypeToken;
import ru.itmo.common.DatabaseAccess;
import ru.itmo.common.connection.Request;
import ru.itmo.server.src.Comms.database.DB_Worker;
import ru.itmo.server.src.Exceptions.LimitException;
import ru.itmo.server.src.Exceptions.NullException;
import ru.itmo.server.src.GivenClasses.*;
import ru.itmo.server.src.containers.stringQueue;

import java.io.*;
import java.util.ArrayList;

public class Update implements Commands{
	/** 
	 *Updates element by id and given args
	 *@author BARIS 
	*/
	public String just_update(ArrayList<String> args, DAO<Worker> dao, String user_login) throws SQLException{
		long id = -1;
		try {
			id = Long.parseLong(args.get(8));
		}
		catch(NumberFormatException e){
			return "Id should be type long for " + getName() + " func";
		}
		Worker w = dao.get(id);
		if(w == null){
			return "There's no worker with such id";
		}
		else if(!w.getUser_login().equals(user_login)){
			return "You're not allowed to update info about " + w.getUser_login() + "'s worker";
		}
		try {
			try {
				String name = args.get(0);
				w.setName(name);
			} catch (NullException e) {
				return "Name field for " + getName() + " func cannot be null";
			}
			try {
				long salo = Long.parseLong(args.get(1));
				w.setSalary(salo);
			} catch (LimitException | IllegalArgumentException e) {
				return "Salary field for " + getName() + " func should be type long and cannot be negative or zero";
			}
			try {
				Position pos = Position.valueOf(args.get(2));
				w.setPosition(pos);
			} catch (IllegalArgumentException e) {
				return "Available arguments for position filed for " + getName() + " function are:\n" + Position.strConvert();
			}
			try {
				Status state = Status.valueOf(args.get(3));
				w.setStatus(state);
			} catch (IllegalArgumentException | NullException e) {
				return "Available arguments for status filed for " + getName() + " function are:\n" + Status.strConvert();
			}
			try {
				OrganizationType orgType = OrganizationType.valueOf(args.get(5));
				Organization org = new Organization(args.get(4), orgType);
				w.setOrganization(org);
			} catch (IllegalArgumentException e) {
				return "Available arguments for organizationType filed for " + getName() + " function are:\n" + OrganizationType.strConvert();
			}
			try {
				long x = Long.parseLong(args.get(6));
				double y = Double.parseDouble(args.get(7));
				Coordinates cords = new Coordinates(x, y);
				w.setCoordinates(cords);
				DB_Worker.updateWorker(w, DatabaseAccess.getDBConnection());
			} catch (IllegalArgumentException e) {
				return "'x' should be type long, 'y' should be type double for coordinates field in function " + getName();
			} catch (LimitException e) {
				return e.getMessage() + " for function " + getName();
			}
		}
		catch(IndexOutOfBoundsException e){
			return "There's lack of arguments for function " + getName();
		}

		return "Worker " + w.getName() + " was successfully updated with all fields";
	}
	public String update_by_id(DAO<Worker> dao, BufferedReader on, String user_login) throws IOException, SQLException{
		Add dd = new Add();
		String reply = "";
		try {
			long id = Long.parseLong(on.readLine().split(" ")[0]);
			Worker w = dao.get(id);
			if(w == null) {
				reply += "\nThere's no guy with such id\n";
			}
			else if(!w.getUser_login().equals(user_login)){
				reply += "\nYou're not allowed to update info about " + w.getUser_login() + "'s worker\n";
			}
			else {
				reply += dd.add_read_exec(w, on, reply).getMessage();
				DB_Worker.updateWorker(w, DatabaseAccess.getDBConnection());
				reply += "\nWorker with id " + w.getId() + " was successfully updated\n";
			}
		}
		catch(IllegalArgumentException e) {
			reply += "\nId should be type long for update func\n";
		}
		return reply;
	}
	
	@Override 
	public String getGist() {
		return "updates element with new arguments";
	}
	@Override
	public String getName() {
		return "update";
	}
	@Override
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws IOException, SQLException {
		Update upd = new Update();
		q = History.cut(q);
		q.addLast(upd);

		String reply = upd.update_by_id(dao, on, user_login);
		return new stringQueue(reply, q);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException, SQLException{
		Update upd = new Update();
		q = History.cut(q);
		q.addLast(upd);

		TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>(){};
		String reply = upd.just_update((ArrayList<String>) request.getArgumentAs(typeToken), dao, request.getUser_login());

		return new stringQueue(reply, q);
	}
}
