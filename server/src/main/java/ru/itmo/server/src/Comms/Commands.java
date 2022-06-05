package ru.itmo.server.src.Comms;

import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.io.BufferedReader;
import java.io.IOException;
import ru.itmo.common.connection.Request;
//import java.util.Scanner;
import ru.itmo.server.src.GivenClasses.Worker;
import ru.itmo.server.src.containers.stringQueue;

public interface Commands {
	/** 
	 *Interface which connects functions
	 *@author AP  
	*/
	String getName();
	String getGist();
	stringQueue executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, String user_login) throws IOException, SQLException;
	stringQueue requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, Request request) throws IOException, SQLException;
	//ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, Scanner scn);
	//ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q);
}
