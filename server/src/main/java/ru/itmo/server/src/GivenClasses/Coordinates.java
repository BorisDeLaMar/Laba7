package ru.itmo.server.src.GivenClasses;

import org.json.*;
import ru.itmo.server.src.Comms.GistStaff;

public class Coordinates {
    private long x; 
    private double y; 
    private static boolean flag = true;
    public static boolean getFlag() {
    	return flag;
    }
    public static void setFlag() {
    	flag = true;
    }
    public Coordinates(JSONArray cord) {
    	try {
	    	x = cord.optLong(0);
	    	y = cord.optDouble(1); //разобраться с double default value
    	}
    	catch(Exception e) {
    	    flag = false;
    		e.printStackTrace();
    	}
    }
    public Coordinates(String x, String y) {
		String reply = GistStaff.getReply();
    	try {
    		this.y = Double.valueOf(y);
    	}
    	catch(IllegalArgumentException e) {
    		flag = false;
    		reply += "\ny should be type double" + "\n";
    	}
    	try {
    		this.x = Long.valueOf(x);
    	}
    	catch(IllegalArgumentException e) {
    		flag = false;
    		reply += "\nx should be type long" + "\n";
    	}
		GistStaff.setReply(reply);
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
