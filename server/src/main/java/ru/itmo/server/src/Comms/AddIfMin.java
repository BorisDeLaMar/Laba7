package ru.itmo.server.src.Comms;
import com.google.gson.JsonSyntaxException;

import java.io.*;

import com.google.gson.reflect.TypeToken;
import ru.itmo.common.DatabaseAccess;
import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.Comms.database.DB_Worker;
import ru.itmo.server.src.Exceptions.LimitException;
import ru.itmo.server.src.Exceptions.NullException;
import ru.itmo.server.src.GivenClasses.*;
import ru.itmo.server.src.containers.stringQueue;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class AddIfMin implements Commands{
	/** 
	 *Adds element if it's minimum of all the elements
	 *@author BARIS  
	*/
	public String just_add_if_min(DAO<Worker> dao, ArrayList<String> args, String user_login) throws SQLException {

		Worker w = new Worker();
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
			} catch (IllegalArgumentException e) {
				return "'x' should be type long, 'y' should be type double for coordinates field in function " + getName();
			} catch (LimitException e) {
				return e.getMessage() + " for function " + getName();
			}
		}
		catch(IndexOutOfBoundsException e){
			return "There's lack of arguments for function " + getName();
		}
		w.setCreationDate();
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(dao.getAll());
		boolean flag = true;
		for (Worker e : bd) {
			if (e.hashCode() <= w.hashCode()) {
				flag = false;
				System.out.println(e.hashCode());
			}
		}
		if (flag) {
			long id = DB_Worker.addWorker(w, user_login, DatabaseAccess.getDBConnection());
			w.setId(id);
			w.setUser_login(user_login);
			dao.appendToList(w);
			return "Worker successfully added";
		} else {
			return "Worker is not min, so that he was not added";
		}
	}
	public String add_if_min_exec(DAO<Worker> dao, BufferedReader on, String user_login) throws LimitException, IOException, SQLException{
		Worker w = new Worker();
		Add dd = new Add();

		String reply = "";
		reply += dd.add_read_exec(w, on, reply);
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(dao.getAll());
		boolean f = true;
		for(Worker e : bd) {
			if(e.hashCode() < w.hashCode()) {
				f = false;
			}
		}
		if(f) {
			w.setCreationDate();
			reply += "\nWorker from file was successfully added\n";
			long id = DB_Worker.addWorker(w, user_login, DatabaseAccess.getDBConnection());
			w.setId(id);
			w.setUser_login(user_login);
			dao.appendToList(w);
		}
		else{
			reply += "\nWorker from file wasn't min, so he wasn't added\n";
		}
		return reply;
	}
	@Override
	public String getGist() {
		return "adds element to collection, if it's the lowest one";
	}
	@Override
	public String getName() {
		return "add_if_min";
	}
	@Override
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws IOException, SQLException{
		AddIfMin aim = new AddIfMin();
		q = History.cut(q);
		q.addLast(aim);

		String reply = "";
		try {
			reply = aim.add_if_min_exec(dao, on, user_login);
		}
		catch(LimitException e) {
			System.out.println(e.getMessage());
		}
		return new stringQueue(reply, q); //Проверить history. Если не робит, попробуй в try возвращать q, а здесь null
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException, SQLException {
		q = History.cut(q);
		q.addLast(this);

		String reply = "";
		try{
			TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>(){};
			reply += this.just_add_if_min(dao, (ArrayList<String>) request.getArgumentAs(typeToken), request.getUser_login());
		}
		catch(JsonSyntaxException e){
			reply += "Not valid arguments for function 'add'";
		}

		return new stringQueue(reply, q);
	}
}
