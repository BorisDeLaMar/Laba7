package ru.itmo.server.src.Comms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import org.json.*;
import ru.itmo.common.DatabaseAccess;
import ru.itmo.server.src.Comms.database.DB_Worker;
import ru.itmo.server.src.Exceptions.LimitException;
import ru.itmo.server.src.Exceptions.NullException;
import ru.itmo.server.src.GivenClasses.*;

public class DataDAO implements DAO<Worker>{
	/** 
	 *Collection class
	 */
	
	private final LinkedHashSet<Worker> database = new LinkedHashSet<Worker>();
	public DataDAO() {}
	/**Чтение из файла*/
	@Override
	public synchronized void DateRead(Connection connection) throws SQLException{
		String hname;
		long hsalary;
		Position hpos; //h - heroe's
		Status hstatus;
		Organization horganization;
		Coordinates hcoordinates;
		long id = -1;
		String str_id = "-1";
		/** 
		 *Add function
		 *@param String filename
		 *@author BARIS  
		 *@throws IllegalArgumentException, JSONException, IOException
		*/
		try {
			String sql = "SELECT * from worker";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				str_id = resultSet.getString(1);
				hname = resultSet.getString(2);
				hsalary = resultSet.getLong(3);
				//есть o.getEnum но я не пон как аргументы в него вставить правильно
				if (resultSet.getString(4).equals("")) {
					hpos = null;
				} else {
					hpos = Position.valueOf(resultSet.getString(4));
				}
				hstatus = Status.valueOf(resultSet.getString(5));
				String creationDate = resultSet.getString(10);
				horganization = new Organization(resultSet.getString(6), OrganizationType.valueOf(resultSet.getString(7)));
				hcoordinates = new Coordinates(resultSet.getLong(8), resultSet.getDouble(9));
				try {
					Worker worker = new Worker(hname, hsalary, hpos, hstatus, horganization, hcoordinates, str_id, creationDate, this);
					worker.setUser_login(resultSet.getString(11));
					//System.out.println(worker.toString() + " datatdao 56 line");
					if (worker.getId() == -1) {
						this.delete(worker);
					} else {
						this.appendToList(worker);//dao запускает функцию
					}
				} catch (NullException | LimitException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		catch(IllegalArgumentException | JSONException | SQLException e) {
			id = Long.parseLong(str_id);
			DB_Worker.deleteWorker(id, DatabaseAccess.getDBConnection());
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 *DAO functions 
	 */
	//Worker clerk = new Worker();
	@Override
	public void appendToList(Worker w) {
		database.add(w);
		//System.out.println(id_count + " " + w.getName());
	}
	@Override
	public void delete(Worker w) {
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
