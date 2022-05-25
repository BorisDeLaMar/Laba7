package ru.itmo.server.src.Comms;

import com.google.gson.JsonSyntaxException;
//import ru.itmo.common.LAB5.src.Exceptions.*;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
//import ru.itmo.common.LAB5.src.GivenClasses.*;
import com.google.gson.reflect.TypeToken;
import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import ru.itmo.server.src.Exceptions.LimitException;
import ru.itmo.server.src.Exceptions.NullException;
import ru.itmo.server.src.GivenClasses.*;

public class Add implements Commands{
	private boolean flag = true;
	public boolean getFlag(){ return flag; }
	public void setFlag(boolean flag){ this.flag = flag;}
	public String justAdd(DAO<Worker> dao, ArrayList<String> args){
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
			dao.appendToList(w);
		}
		catch(LimitException e) {
			System.out.println(e.getMessage());
		}
		return "Worker " + w.getName() + " successfully added";
		//System.out.println(args);
		//dao.appendToList(w);
	}
	public void add(DAO<Worker> d, BufferedReader on) throws IOException{
		/** 
		 *Add function
		 *@param DAO<Worker> dao, String[] args
		 *@author BARIS  
		*/
		Worker w = new Worker();
		add_read(w, on);
		w.setCreationDate();
		try {
			w.setID(Worker.findPossibleID());
			d.appendToList(w);
		}
		catch(LimitException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Worker successfully added");
	}
	public void add_exec(DAO<Worker> dao, BufferedReader on) {
		String reply = GistStaff.getReply();
		Worker w = new Worker();
		try {
			setFlag(add_read_exec(w, on));
		}
		catch(IOException e) {
			reply = GistStaff.getReply();
			reply += "\n" + e.getMessage() + "\n";
			GistStaff.setReply(reply);
		}
		boolean f = getFlag();
		//scam.nextLine();
		if(!f) {
			reply = GistStaff.getReply();
			reply += "\nWorker from file wasn't added or updated because of incorrect input" + "\n";
			GistStaff.setReply(reply);
		}
		else {
			w.setCreationDate();
			reply = GistStaff.getReply();
			reply += "\nWorker from file was successfully added\n";
			GistStaff.setReply(reply);
			try {
				w.setID(Worker.findPossibleID());
				dao.appendToList(w);
			}
			catch(LimitException e) {
				reply = GistStaff.getReply();
				reply += "\n" + e.getMessage() + "\n";
				GistStaff.setReply(reply);
			}
			setFlag(true);
			//System.out.println("Worker successfully added");
		}
	}
	public void add_read(Worker w, BufferedReader on) throws IOException{
		int i = 0;
		while(i < 8) {
			if (i == 0) {
				try {
					System.out.print("Enter name: ");
					String name = on.readLine().split(" ")[0];
					i = 1;
					w.setName(name);
				}
				catch(NullException e) {
					i = 0;
					System.out.println(e.getMessage());
				}
			}
			if(i == 1) {
				try {
					System.out.print("Enter salary: ");
					String salo = on.readLine().split(" ")[0];
					Long salary = Long.valueOf(salo);
					i = 2;
					w.setSalary(salary);
				}
				catch(NumberFormatException e) {
					i = 1;
					System.out.println("Salary should be just a number");
				}
				catch(LimitException e) {
					i = 1;
					System.out.println(e.getMessage());
				}
			}
			if(i == 2) {
				try {
					System.out.print("Enter position: ");
					String posit = on.readLine().split(" ")[0];
					i = 3;
					Position pos =  Position.valueOf(posit);
					w.setPosition(pos);
				}
				catch(IllegalArgumentException e) {
					i = 2;
					System.out.println("Available values for position are: " + Position.strConvert());
				}
			}
			if(i == 3) {
				try {
					System.out.print("Enter status: ");
					String stata = on.readLine().split(" ")[0];
					//scam.nextLine();
					i = 4;
					Status state = Status.valueOf(stata);
					w.setStatus(state);
				}
				catch(IllegalArgumentException e) {
					i = 3;
					System.out.println("Available values for status are: " + Status.strConvert());
				}
				catch(NullException e) {
					i = 3;
					System.out.println(e.getMessage());
				}
			}
			if(i == 4) {
				try {
					System.out.println("Enter organization: ");
					String[] arg = on.readLine().split(" ");
					i = 6;
					Organization org = new Organization(arg[0], arg[1]);
					if(!Organization.getFlag()) {
						i = 4;
					}
					Organization.setFlag(true);
					w.setOrganization(org);
				}
				catch(ArrayIndexOutOfBoundsException e) {
					i = 4;
					System.out.println("There should be two args in organization field: name and type\nAvailbale organizationtypes: " + OrganizationType.strConvert());
				}
			}
			if(i == 6) {
				try {
					System.out.print("Enter coordinates: ");
					String[] arg = on.readLine().split(" ");
					i = 8;
					Coordinates cords = new Coordinates(arg[0], arg[1]);
					if(!Coordinates.getFlag()) {
						i = 6;
					}
					Coordinates.setFlag();
					try {
						w.setCoordinates(cords);
					}
					catch(LimitException e) {
						i = 6;
						System.out.println(e.getMessage());
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					i = 6;
					System.out.println("There should be two args in coordinates field: x and y");
				}
			}
		}
	}
	public boolean add_read_exec(Worker w, BufferedReader on) throws IOException {
		String reply = GistStaff.getReply();
		int i = 0;
		boolean flag = true;
		while(i < 8) {
			if (i == 0) {
				try {
					String[] names = on.readLine().split(" ");
					i = 1;
					w.setName(names[0]);
				}
				catch(NullException e) {
					i = 8;
					flag = false;
					reply = GistStaff.getReply();
					reply += "\n" + e.getMessage() + "\n";
					GistStaff.setReply(reply);
				}
			}
			if(i == 1) {
				try {
					String[] salo = on.readLine().split(" ");
					Long salary = Long.valueOf(salo[0]);
					i = 2;
					w.setSalary(salary);
				}
				catch(NumberFormatException e) {
					i = 8;
					flag = false;
					reply = GistStaff.getReply();
					reply += "\nSalary should be just a number" + "\n";
					GistStaff.setReply(reply);
				}
				catch(LimitException e) {
					i = 8;
					flag = false;
					reply = GistStaff.getReply();
					reply += "\n" + e.getMessage() + "\n";
					GistStaff.setReply(reply);
				}
			}
			if(i == 2) {
				try {
					String[] posit = on.readLine().split(" ");
					i = 3;
					Position pos =  Position.valueOf(posit[0]);
					w.setPosition(pos);
				}
				catch(IllegalArgumentException e) {
					i = 8;
					flag = false;
					reply = GistStaff.getReply();
					reply += "\nAvailable values for position are: " + Position.strConvert() + "\n";
					GistStaff.setReply(reply);
				}
			}
			if(i == 3) {
				try {
					String[] stata = on.readLine().split(" ");
					//on.readLine();
					i = 4;
					Status state = Status.valueOf(stata[0]);
					w.setStatus(state);
				}
				catch(IllegalArgumentException e) {
					i = 8;
					flag = false;
					reply = GistStaff.getReply();
					reply += "\nAvailable values for status are: " + Status.strConvert() + "\n";
					GistStaff.setReply(reply);
				}
				catch(NullException e) {
					i = 8;
					flag = false;
					reply = GistStaff.getReply();
					reply += "\n" + e.getMessage() + "\n";
					GistStaff.setReply(reply);
				}
			}
			if(i == 4) {
				try {
					String[] arg = on.readLine().split(" ");
					i = 6;
					Organization org = new Organization(arg[0], arg[1]);
					if(!Organization.getFlag()) {
						i = 8;
						flag = false;
					}
					Organization.setFlag(true);
					w.setOrganization(org);
				}
				catch(ArrayIndexOutOfBoundsException e) {
					i = 8;
					flag = false;
					reply = GistStaff.getReply();
					reply += "\nThere should be two args in organization field: name and type\nAvailbale organizationtypes: " + OrganizationType.strConvert() + '\n';
					GistStaff.setReply(reply);
				}
			}
			if(i == 6) {
				try {
					String[] arg = on.readLine().split(" ");
					i = 8;
					Coordinates cords = new Coordinates(arg[0], arg[1]);
					if(!Coordinates.getFlag()) {
						flag = false;
					}
					reply = GistStaff.getReply();
					Coordinates.setFlag();
					try {
						w.setCoordinates(cords);
					}
					catch(LimitException e) {
						flag = false;
						reply = GistStaff.getReply();
						reply += "\n" + e.getMessage() + "\n";
						GistStaff.setReply(reply);
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					i = 8;
					flag = false;
					reply = GistStaff.getReply();
					reply += "\nThere should be two args in coordinates field: x and y" + "\n";
					GistStaff.setReply(reply);
				}
			}
		}
		GistStaff.setReply(reply);
		return flag;
	}
	@Override
	public String getGist() {
		return "adds element to collection";
	}
	@Override
	public String getName() {
		return "add";
	}
	//@SuppressWarnings("finally")
	@Override
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on) throws IOException{
		q = History.cut(q);
		q.addLast(this);
		if(GistStaff.getFlag()) {
			this.add_exec(dao, on);
			/*String reply = GistStaff.getReply();
			reply += help;
			GistStaff.setReply(reply);*/
		}
		else {
			this.add(dao, on);
		}
		return q;
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException{
		q = History.cut(q);
		q.addLast(this);
		Response response;
		try {
			TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>(){};
			String reply = this.justAdd(dao, (ArrayList<String>) request.getArgumentAs(typeToken));
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
