package ru.itmo.server.src.Comms;
import ru.itmo.server.src.GivenClasses.Worker;

import java.util.*;

public class Sort{
	/** 
	 *Sorts the collection
	 *@author AP
	*/
	
	public TreeSet<Worker> sort(DAO<Worker> dao){
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(dao.getAll());

		return new TreeSet<Worker>(bd);
	}
}
