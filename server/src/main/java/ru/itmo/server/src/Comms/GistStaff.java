package ru.itmo.server.src.Comms;
//import java.util.LinkedHashSet;
import ru.itmo.server.src.GivenClasses.*;

public abstract class GistStaff{
	String hname;
	long hsalary;
	Position hpos; //h - heroe's
	Status hstatus;
	Organization horganization;
	Coordinates hcoordinates;
	/** 
	 *Just a help class for Gist
	 *@param hname, hsalary, hpos, hstatus, horganization, hcoordinates
	 *@author BARIS
	*/
	protected static DAO<Worker> dao = new DataDAO();
	private static boolean isExecute = false;
	public static boolean getFlag() {
		return isExecute;
	}
	public static void setFlag(boolean isExecute) {
		GistStaff.isExecute = isExecute;
	}
	private static String reply = "";
	public static String getReply(){
		return reply;
	}
	public static void setReply(String repl){
		reply = repl;
	}
	//LinkedHashSet database = new LinkedHashSet<>();
}
