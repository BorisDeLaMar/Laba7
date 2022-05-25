package ru.itmo.server.src.Comms;
import com.google.gson.JsonSyntaxException;

import java.io.*;

import com.google.gson.reflect.TypeToken;
import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.Exceptions.LimitException;
import ru.itmo.server.src.Exceptions.NullException;
import ru.itmo.server.src.GivenClasses.*;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class AddIfMin implements Commands{
	/** 
	 *Adds element if it's minimum of all the elements
	 *@author BARIS  
	*/
	public String just_add_if_min(DAO<Worker> dao, ArrayList<String> args){

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
		try {
			w.setID(Worker.findPossibleID());
			LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(dao.getAll());
			boolean flag = true;
			for(Worker e : bd) {
				if(e.hashCode() <= w.hashCode()) {
					flag = false;
					System.out.println(e.hashCode());
				}
			}
			if(flag) {
				dao.appendToList(w);
				return "Worker successfully added";
			}
			else{
				return "Worker is not min, so that he was not added";
			}
		}
		catch(LimitException e) {
			return e.getMessage();
		}
	}
	public void add_if_min(DAO<Worker> dao, BufferedReader on) throws LimitException, IOException{
		Worker w = new Worker();
		Add dd = new Add();
		dd.add_read(w, on);
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(dao.getAll());
		boolean f = true;
		for(Worker e : bd) {
			if(e.hashCode() < w.hashCode()) {
				f = false;
			}
		}
		if(f) {
			w.setCreationDate();
			w.setID(Worker.findPossibleID());
			dao.appendToList(w);
			System.out.println("Worker successfully added");
		}
		else {
			System.out.println("Worker is not min, so he wasn't added");
		}
	}
	public void add_if_min_exec(DAO<Worker> dao, BufferedReader on) throws LimitException, IOException{
		Worker w = new Worker();
		Add dd = new Add();
		dd.add_read_exec(w, on);
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(dao.getAll());
		boolean f = true;
		for(Worker e : bd) {
			if(e.hashCode() < w.hashCode()) {
				f = false;
			}
		}
		if(f) {
			w.setCreationDate();
			String reply = GistStaff.getReply();
			reply += "\nWorker from file was successfully added\n";
			GistStaff.setReply(reply);
			w.setID(Worker.findPossibleID());
			dao.appendToList(w);
		}
		else{
			String reply = GistStaff.getReply();
			reply += "\nWorker from file wasn't min, so he wasn't added\n";
			GistStaff.setReply(reply);
		}
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
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on) throws IOException{
		AddIfMin aim = new AddIfMin();
		q = History.cut(q);
		q.addLast(aim);
		try {
			if(GistStaff.getFlag()) {
				aim.add_if_min_exec(dao, on);
			}
			else {
				aim.add_if_min(dao, on);
			}
		}
		catch(LimitException e) {
			System.out.println(e.getMessage());
		}
		return q; //Проверить history. Если не робит, попробуй в try возвращать q, а здесь null
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException {
		q = History.cut(q);
		q.addLast(this);
		Response response;
		try{
			TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>(){};
			String reply = this.just_add_if_min(dao, (ArrayList<String>) request.getArgumentAs(typeToken));
			response = new Response(
					Response.cmdStatus.OK,
					reply
			);
		}
		catch(JsonSyntaxException e){
			response = new Response(
					Response.cmdStatus.ERROR,
					"Not valid arguments for function 'add'"
			);
		}
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
}
