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
	*/
	protected static DAO<Worker> dao = new DataDAO();
	//LinkedHashSet database = new LinkedHashSet<>();
}
