package ru.itmo.server.src.Comms;

import com.google.gson.JsonSyntaxException;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;
import ru.itmo.common.DatabaseAccess;
import ru.itmo.common.connection.Request;
import ru.itmo.server.src.Comms.database.DB_Worker;
import ru.itmo.server.src.Exceptions.LimitException;
import ru.itmo.server.src.Exceptions.NullException;
import ru.itmo.server.src.GivenClasses.*;
import ru.itmo.server.src.containers.booleanString;
import ru.itmo.server.src.containers.stringQueue;

public class Add implements Commands{
	public String justAdd(DAO<Worker> dao, ArrayList<String> args, String user_login) throws SQLException {
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
		long id = DB_Worker.addWorker(w, user_login, DatabaseAccess.getDBConnection());
		w.setId(id);
		w.setUser_login(user_login);
		dao.appendToList(w);
		return "Worker " + w.getName() + " successfully added";
	}
	public String add_exec(DAO<Worker> dao, BufferedReader on, String user_login) throws SQLException{
		String reply = "";
		Worker w = new Worker();
		boolean flag = true;
		try {
			booleanString booleanString = add_read_exec(w, on, reply);
			flag = booleanString.getFlag();
			reply += booleanString.getMessage();
		}
		catch(IOException e) {
			reply += "\n" + e.getMessage() + "\n";
		}
		if(!flag) {
			reply += "\nWorker from file wasn't added or updated because of incorrect input" + "\n";
		}
		else {
			w.setCreationDate();
			reply += "\nWorker from file was successfully added\n";
			long id = DB_Worker.addWorker(w, user_login, DatabaseAccess.getDBConnection());
			w.setId(id);
			w.setUser_login(user_login);
			dao.appendToList(w);
		}
		return reply;
	}
	public booleanString add_read_exec(Worker w, BufferedReader on, String reply) throws IOException {
		int i = 0;
		boolean flag = true;
		StringBuilder replyBuilder = new StringBuilder(reply);
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
					replyBuilder.append("\n").append(e.getMessage()).append("\n");
				}
			}
			if(i == 1) {
				try {
					String[] salo = on.readLine().split(" ");
					long salary = Long.parseLong(salo[0]);
					i = 2;
					w.setSalary(salary);
				}
				catch(NumberFormatException e) {
					i = 8;
					flag = false;
					replyBuilder.append("\nSalary should be just a number" + "\n");
				}
				catch(LimitException e) {
					i = 8;
					flag = false;
					replyBuilder.append("\n").append(e.getMessage()).append("\n");
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
					replyBuilder.append("\nAvailable values for position are: ").append(Position.strConvert()).append("\n");
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
					replyBuilder.append("\nAvailable values for status are: ").append(Status.strConvert()).append("\n");
				}
				catch(NullException e) {
					i = 8;
					flag = false;
					replyBuilder.append("\n").append(e.getMessage()).append("\n");
				}
			}
			if(i == 4) {
				try {
					String[] arg = on.readLine().split(" ");
					i = 6;
					Organization organization = new Organization();
					Organization org = organization.getOrganization(arg[0], arg[1]);
					if(org.getType() == null) {
						replyBuilder.append("\n").append("Available organization types are: ").append(OrganizationType.strConvert()).append("\n");
					}
					w.setOrganization(org);
				}
				catch(ArrayIndexOutOfBoundsException e) {
					i = 8;
					flag = false;
					replyBuilder.append("\nThere should be two args in organization field: name and type\nAvailbale organizationtypes: ").append(OrganizationType.strConvert()).append('\n');
				}
			}
			if(i == 6) {
				try {
					String[] arg = on.readLine().split(" ");
					i = 8;
					double y = -228.1337; long x = -228; boolean mistake = false;
					try {
						y = Double.parseDouble(arg[1]);
					}
					catch(IllegalArgumentException e) {
						mistake = true;
						replyBuilder.append("\n'y' should be type double" + "\n");
					}
					try {
						x = Long.parseLong(arg[0]);
					}
					catch(IllegalArgumentException e) {
						mistake = true;
						replyBuilder.append("\n'x' should be type long" + "\n");
					}
					if(!mistake) {
						try {
							Coordinates cords = new Coordinates(x, y);
							w.setCoordinates(cords);
						}
						catch(LimitException e) {
							flag = false;
							replyBuilder.append("\n").append(e.getMessage()).append("\n");
						}
					}
					else{
						flag = false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					i = 8;
					flag = false;
					replyBuilder.append("\nThere should be two args in coordinates field: x and y" + "\n");
				}
			}
		}
		reply = replyBuilder.toString();
		return new booleanString(flag, reply);
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
	public stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws IOException, SQLException{
		q = History.cut(q);
		q.addLast(this);

		return new stringQueue(this.add_exec(dao, on, user_login), q);
	}
	@Override
	public stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException, SQLException{
		q = History.cut(q);
		q.addLast(this);

		String reply = "";
		try {
			TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>(){};
			reply += this.justAdd(dao, (ArrayList<String>) request.getArgumentAs(typeToken), request.getUser_login());
		}
		catch(JsonSyntaxException e){
			reply += "Not valid arguments for function 'add'";
		}

		return new stringQueue(reply, q);
	}
	public static void Test(){

	}
}
