package ru.itmo.server.src.GivenClasses;

import org.json.*;

public class Coordinates {
    private long x; 
    private double y;
    public Coordinates(JSONArray cord) {
    	try {
	    	x = cord.optLong(0);
	    	y = cord.optDouble(1); //разобраться с double default value
    	}
    	catch(Exception e) {
			System.out.println(e.getMessage());
    	}
    }
	public Coordinates(long x, double y){
		this.x = x;
		this.y = y;
	}
    
    public long getAbscissa() {
    	return x;
    }
    public double getOrdinate() {
    	return y;
    }
}
