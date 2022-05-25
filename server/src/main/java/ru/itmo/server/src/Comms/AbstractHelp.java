package ru.itmo.server.src.Comms;

import java.util.ArrayList;

public class AbstractHelp {
	private static Commands cmd = new Add();
	private static Commands cmd1 = new AddIfMin();
	private static Commands cmd2 = new Clear();
	private static Commands cmd3 = new FilterStatus();
	private static Commands cmd4 = new Help();
	private static Commands cmd5 = new Info();
	private static Commands cmd6 = new PrintDescending();
	private static Commands cmd7 = new PrintUniqueStatus();
	private static Commands cmd8 = new Remove();
	private static Commands cmd9 = new RemoveLower();
	private static Commands cmd10 = new Save();
	private static Commands cmd11 = new Show();
	private static Commands cmd12 = new Update();
	private static Commands cmd13 = new History();
	private static Commands cmd14 = new ExecuteScript();
	private static Commands cmd15 = new Exit();
	/** 
	 *Just a help class for
	 *@param All the available commands
	 *@author AP  
	*/
	protected static ArrayList<Commands> lst = new ArrayList<Commands>();
	
	protected static void addToList() {
		//lst = new ArrayList<Commands>(Stream.of(cmd, cmd1, cmd2, cmd3, cmd4, cmd5, cmd6, cmd7, cmd8, cmd9, cmd10, cmd11, cmd12, cmd13, cmd14, cmd15).collect(Collectors.toList()));
		lst.add(cmd);
		lst.add(cmd1);
		lst.add(cmd2);
		lst.add(cmd3);
		lst.add(cmd4);
		lst.add(cmd5);
		lst.add(cmd6);
		lst.add(cmd7);
		lst.add(cmd8);
		lst.add(cmd9);
		lst.add(cmd10);
		lst.add(cmd11);
		lst.add(cmd12);
		lst.add(cmd13);
		lst.add(cmd14);
		lst.add(cmd15);
	}
}
