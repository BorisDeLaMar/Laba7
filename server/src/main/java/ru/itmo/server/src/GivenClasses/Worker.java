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
    private int x;
    private int y;
    private int moneymoney;
	private String user_login;
    
    public Worker(String name, long salary, Position pos, Status state, Organization org, Coordinates cords, String id, String creationDate, DAO<Worker> dao) throws NullException, LimitException{
    		setName(name);
    		setSalary(salary);
    		setPosition(pos);
    		setStatus(state);
    		setOrganization(org);
    		setCoordinates(cords);
    		setCreationDate(creationDate);
    		setId(id);
    	   	moneymoney = (int) salary/10000;
    		x = (int) coordinates.getAbscissa();
    		y = (int) coordinates.getOrdinate();
    }
    public Worker() {}
	
    public long getId() {
    	return id;
    }
	public void setId(String id) throws NumberFormatException{
		this.id = Long.parseLong(id);
	}
	public void setId(long id){
		this.id = id;
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

	public String getUser_login() {
		return user_login;
	}

	public void setUser_login(String user_login) {
		this.user_login = user_login;
	}

	public Organization getOrganization() {
    	return organization;
    }
    public void setOrganization(Organization organization) {
    	this.organization = organization;
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
    	return "Name: " + name + "\nSalary: " + salary + "\nPosition: " + position.toString() + "\nStatus: " + status.toString() + "\nOrganization: " + organization.getName() + ", " + organization.getType().toString() + "\nCoordinates: " + coordinates.getAbscissa() + ", " + coordinates.getOrdinate() + "\n" + "ID: " + id + ",\n" + "creationDate: " + creationDate.format(format) + "\n" + "user_login:" + user_login + "\n";
    }
    @Override
    public int compareTo(Worker w) {
		return this.getName().compareTo(w.getName());
    }
}
