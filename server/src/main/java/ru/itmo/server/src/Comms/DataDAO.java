package ru.itmo.server.src.Comms;
import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;

import org.json.*;
import ru.itmo.server.src.GivenClasses.*;
//import Exceptions.*;

public class DataDAO implements DAO<Worker>{
	/** 
	 *Collection class
	 *@param DAO<Worker> dao, String[] args
	 *@author AP
	*/
	
	private LinkedHashSet<Worker> database = new LinkedHashSet<Worker>();
	private String filepath;
	private static boolean flag;
	public static boolean getFlag() {
		return flag;
	}
	public static void setFlag(boolean f) {
		flag = f;
	}
	
	private static long id_count = 1;
	public static long getIDCounter() {
		return id_count;
	}
	public static void incrementID() {
		id_count += 1;
	}
	//private static long available_id;
	
	public DataDAO() {
		
	}
	
	/**Чтение из файла*/
	// TODO
	@Override
	public void DateRead(String filename) {
		String hname;
		long hsalary;
		Position hpos; //h - heroe's
		Status hstatus;
		Organization horganization;
		Coordinates hcoordinates;
		/** 
		 *Add function
		 *@param String filename
		 *@author BARIS  
		 *@throws IllegalArgumentException, JSONException, IOException
		*/
		filepath = filename;
		try(BufferedReader in = new BufferedReader(new FileReader(filepath))){
			try {
				JSONTokener tokener = new JSONTokener(in);
				JSONObject obj =  new JSONObject(tokener);
				JSONArray arr = obj.getJSONArray("workers");
				for(int i = 0; i < arr.length(); i++) {
					flag = true;
					JSONObject o = arr.getJSONObject(i);
					hname = o.getString("name");
					hsalary = o.getLong("salary");
					//есть o.getEnum но я не пон как аргументы в него вставить правильно
					if(o.getString("position") == "null") {
						hpos = null;
					}
					else {
						hpos = Position.valueOf(o.getString("position"));
					}
					hstatus = Status.valueOf(o.getString("status"));
					String str_id = o.getString("ID");
					String creationDate = o.getString("CreationDate");
					JSONArray org = o.getJSONArray("organization");
					horganization = new Organization(org);
					JSONArray cord = o.getJSONArray("coordinates");
					hcoordinates = new Coordinates(cord);
					Worker worker = new Worker(hname, hsalary, hpos, hstatus, horganization, hcoordinates, str_id, creationDate, this);
					if(worker.getId() == -1) {
						this.delete(worker);
					}
					else {
						this.appendToList(worker);//dao запускает функцию
					}
					//id += 1;
				}
			}
			catch(IllegalArgumentException e) {
				flag = false;
				System.out.println(e.getMessage());
			}
			catch(JSONException e) {
				flag = false;
				System.out.println(e.getMessage());
			}
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 *DAO functions 
	 */
	//Worker clerk = new Worker();
	@Override
	public void appendToList(Worker w) {
		if(DataDAO.getFlag()) {
			database.add(w);
			if(w.getFlag()) {
				DataDAO.incrementID();
				w.setFlag();
			}
		}
		//System.out.println(id_count + " " + w.getName());
	}
	@Override
	public void delete(Worker w) {
		Worker.removeFromBanned(w.getId());
		database.remove(w);
	}
	@Override
	public Worker get(long id) {
		for(Worker w : database) {
			if(w.getId() == id) {
				return w;
			}
		}
		return null; //и эту ошибку обханделить потом
	}

	@Override
	public LinkedHashSet<Worker> getAll(){
		
		return database;
	}
	@Override
	public void saveCollection(String s) {
		Save sv  = new Save();
		sv.save(this, s);
	}
}
