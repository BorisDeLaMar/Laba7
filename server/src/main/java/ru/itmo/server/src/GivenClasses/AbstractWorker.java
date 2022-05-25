package ru.itmo.server.src.GivenClasses;

import java.time.LocalDateTime;

public class AbstractWorker {
    protected long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    protected String name; //Поле не может быть null, Строка не может быть пустой
    protected Coordinates coordinates; //Поле не может быть null
    protected LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    protected long salary; //Значение поля должно быть больше 0
    protected Position position; //Поле может быть null
    protected Status status; //Поле не может быть null
    protected Organization organization; //Поле может быть null
    /*
    public void set(String name) throws NullException{
    	if(name == null || name == "") {
    		throw new NullException("Name couldn't be null");
    	}
    	else {
    		this.name = name;
    	}
    }
    public void set(long salary) throws LimitException{
    	if(salary <= 0) {
    		throw new LimitException("For " + name + ": Не платить зарплату?? Оставить человека еще и должным компании?? Профсоюз в ярости, будем созывать президиум для дальнейших разбирательств. Деменций, неси свиней\nПоле salary должно быть строго положительным");
    	}
    	else {
    		this.salary = salary;
    	}
    }
    public void set(Position position) {
    	this.position = position;
    }
    public void set(Status status) throws NullException{
    	if(status == null) {
    		throw new NullException("For " + name + ": Status couldn't be null");
    	}
    	else {
    		this.status = status;
    	}
    }
    public void set(Coordinates coordinates) throws LimitException{
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
    public void set(Organization organization) {
    	this.organization = organization;
    }*/
}
