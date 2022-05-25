package ru.itmo.server.src.GivenClasses;

//import java.lang.reflect.*;
import ru.itmo.server.src.Comms.DAO;
import ru.itmo.server.src.Comms.DataDAO;
import ru.itmo.server.src.Exceptions.LimitException;
import ru.itmo.server.src.Exceptions.NullException;

import java.io.Serializable;
import java.time.format.*;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Worker extends AbstractWorker implements Comparable<Worker>, Serializable {
    public static ArrayList<Long> bannedID = new ArrayList<Long>();
    // TODO
    private boolean IdNotFromFile = true;
    public boolean getFlag() {
    	return IdNotFromFile;
    }
    public void setFlag() {
    	IdNotFromFile = true;
    }
    
    private int x;
    private int y;
    private int moneymoney;
    
    public Worker(String name, long salary, Position pos, Status state, Organization org, Coordinates cords, String id, String creationDate, DAO<Worker> dao) {
    	try {
    		setName(name);
    		setSalary(salary);
    		setPosition(pos);
    		setStatus(state);
    		setOrganization(org);
    		setCoordinates(cords);
    		setCreationDate(creationDate);
    		setId(id, dao);
    	   	moneymoney = (int) salary/10000;
    		x = (int) coordinates.getAbscissa();
    		y = (int) coordinates.getOrdinate();
    	}
    	catch(NullException e) {
    		DataDAO.setFlag(false);
    		System.out.println(e.getMessage());
    	}
    	catch(LimitException e) {
    		DataDAO.setFlag(false);
    		System.out.println(e.getMessage());
    	}
    }
    public Worker() {}
	
    public long getId() {
    	return id;
    }
    public void setId(String id, DAO<Worker> dao) throws NullException, LimitException{
    	if(id == "") {
    		this.id = Worker.findPossibleID();
			if(this.id == -1) {
				throw new LimitException("Our dear " + name + " was deleted from collection, cause there couldn't be more than 1000 people in bd");      
			}
    		bannedID.add(this.id);
    	}
    	else {
    		try {
    			long possible_ID = Long.valueOf(id);
    			IdNotFromFile = false;
    	    	//System.out.println(bannedID.size());
        		for(int c = 0; c < bannedID.size(); c++) {
        			//System.out.println(possible_ID + " " + bannedID.get(c));
        			if(possible_ID <= 0) {
        				this.id = Worker.findPossibleID();
        				if(this.id == -1) {
        					throw new LimitException("Our dear " + name + " was deleted from collection, cause there couldn't be more than 1000 people in bd");      
        				}
        				bannedID.add(this.id);
        				throw new LimitException("Id can't be below 1, so we changed it to appropriate one (" + this.id + ") for " + name);
        			}
        			if(possible_ID == bannedID.get(c)) {
        				if(false == dao.get(bannedID.get(c)).getFlag()) {
        					//System.out.println(bannedID.get(c));
        					//Worker w = dao.get(possible_ID);
        					long idik = Worker.findPossibleID();
        					if(idik == -1) {
        						throw new LimitException("Our dear " + name + " was deleted from collection, cause there couldn't be more than 1000 people in bd");      
        					}
        					this.id = possible_ID;
        					String s = "There were two guys with same id:\n" + name + ", " + creationDate + "\n" + dao.get(bannedID.get(c)).getName() + ", " + dao.get(bannedID.get(c)).getCreationDate() + "\nSo I changed the id of second guy to " + idik;   					
        					dao.get(bannedID.get(c)).setID(idik);
        					bannedID.add(idik);  
        					throw new LimitException(s);
        				}
        				else if (possible_ID < DataDAO.getIDCounter()) {
        					Worker w = dao.get(possible_ID);
        					long idik = Worker.findPossibleID();
        					if(idik == -1) {
        						throw new LimitException("Our dear " + name + " was deleted from collection, cause there couldn't be more than 1000 people in bd");      
        					}
        					this.id = possible_ID;
        					w.setID(idik);
        					bannedID.add(idik);
        				}
        				else {
	        				this.id = Worker.findPossibleID();
	        				if(this.id == -1) {
	        					throw new LimitException("Our dear " + name + " was deleted from collection, cause there couldn't be more than 1000 people in bd");      
	        				}
	        				bannedID.add(this.id);
        				}
        				break;
        			}
        			else if(c == bannedID.size() - 1) {
        				this.id = possible_ID;
        				bannedID.add(this.id);
        				break;
        			}
        		}
    		}
    		catch(IllegalArgumentException e) {
				this.id = Worker.findPossibleID();
				if(this.id == -1) {
					throw new LimitException("Our dear " + name + " was deleted from collection, cause there couldn't be more than 1000 people in bd");      
				}
				bannedID.add(this.id);
				throw new LimitException("Id should be type long, so we'll fix it\n" + "Id of " + name + " was set to appropriate one: " + this.id);
    		}
    	}
    }
	public void setTemporaryID(long id){
		this.id = id;
	}
    public void setID(long id) throws LimitException{
    	for(int c = 0; c < bannedID.size(); c++) {
    		if(id == bannedID.get(c)) {
    			this.id = Worker.findPossibleID();
				if(this.id == -1) {
					throw new LimitException("Our dear " + name + " was deleted from collection, cause there couldn't be more than 1000 people in bd");      
				}
    			bannedID.add(this.id);
    			//DataDAO.incrementID();
    			break;
    		}
    		else if(c == bannedID.size() - 1){
    			this.id = id;
    			bannedID.add(this.id);
    			//DataDAO.incrementID();
    			break;
    		}
    	}
    }
    public static long findPossibleID() {
    	for(long i = DataDAO.getIDCounter(); i < 1001; i++) {
    		for(int c = 0; c < bannedID.size(); c++) {
    			if(i == bannedID.get(c)) {
    				break;
    			}
    			if(c == bannedID.size() - 1){
    				long id = i;
    				return id;
    			}
    		}
    	}
    	return -1;
    }
    public String getName() {
    	return name;
    }
    public void setName(String name) throws NullException{
    	if(name == null || name.equals("")) {
    		throw new NullException("Name couldn't be null");
    	}
    	else {
    		this.name = name;
    	}
    }
    
    public Coordinates getCoordinates() {
    	return coordinates;
    }
    public void setCoordinates(Coordinates coordinates) throws LimitException{
    	if(coordinates == null) {
    		throw new LimitException("For " + name + ": Coordinates couldn't be null");
    	}
    	if(coordinates.getAbscissa() > 176) {
    		throw new LimitException("For " + name + ": x should be no more than 176");
    	}
    	if(coordinates.getOrdinate() > 729) {
    		throw new LimitException("For " + name + ": y is no more than 729");
    	}
    	else {
    		this.coordinates = coordinates;
    	}
    }
    
    public LocalDateTime getCreationDate() {
    	return creationDate;
    }
    public void setCreationDate(String date) throws NullException, LimitException{
    	LocalDateTime cDate = LocalDateTime.now();
    	if(cDate == null) {
    		throw new NullException("For " + name + ": CreationDate can't be null");
    	}
    	else if(date == "") {
    		creationDate = cDate;
    	}
    	else {
    		try {
    			creationDate = LocalDateTime.parse(date);
    		}
    		catch(DateTimeParseException e) {
    			creationDate = LocalDateTime.now();
    			System.out.println("For " + name + ": creationDate format is incorrect, so we'll set this argument to the time of this message");
    		}
    	}
    }
    public void setCreationDate() {
    	creationDate =  LocalDateTime.now();
    }
    public long getSalary() {
    	return salary;
    }
    public void setSalary(long salary) throws LimitException{
    	if(salary <= 0) {
    		throw new LimitException("For " + name + ": Не платить зарплату?? Оставить человека еще и должным компании?? Профсоюз в ярости, будем созывать президиум для дальнейших разбирательств. Деменций, неси свиней\nПоле salary должно быть строго положительным");
    	}
    	else {
    		this.salary = salary;
    	}
    }
    
    public Position getPosition() {
    	return position;
    }
    public void setPosition(Position position) {
    	this.position = position;
    }
    
    public Status getStatus() {
    	return status;
    }
    public void setStatus(Status status) throws NullException{
    	if(status == null) {
    		throw new NullException("For " + name + ": Status couldn't be null");
    	}
    	else {
    		this.status = status;
    	}
    }
    
    public Organization getOrganization() {
    	return organization;
    }
    public void setOrganization(Organization organization) {
    	this.organization = organization;
    }
    
    public static void removeFromBanned(Long id) {
    	//System.out.println(bannedID.toString());
    	bannedID.remove(id);
    }
    @Override
    public int hashCode() {
    	return 31*name.length() + 29*x*x + 31*y*y + moneymoney/1000;
    }
    @Override
    public boolean equals(Object obj) {
    	Worker w  = (Worker) obj;
    	if((name == w.getName()) && (x == w.coordinates.getAbscissa()) && (y == w.coordinates.getOrdinate()) && (moneymoney == w.moneymoney)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    @Override
    public String toString() {
    	DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    	return "Name: " + name + "\nSalary: " + salary + "\nPosition: " + position.toString() + "\nStatus: " + status.toString() + "\nOrganization: " + organization.getName() + ", " + organization.getType().toString() + "\nCoordinates: " + coordinates.getAbscissa() + ", " + coordinates.getOrdinate() + "\n" + "ID: " + id + ",\n" + "creationDate: " + creationDate.format(format) + "\n";
    }
    @Override
    public int compareTo(Worker w) {
		return this.getName().compareTo(w.getName());
    }
}
