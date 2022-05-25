package ru.itmo.server.src.Comms;

import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.io.BufferedReader;
import java.io.IOException;
import ru.itmo.common.connection.Request;
//import java.util.Scanner;
import ru.itmo.server.src.GivenClasses.Worker;

public interface Commands {
	/** 
	 *Interface which connects functions
	 *@author AP  
	*/
	String getName();
	String getGist();
	ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on) throws IOException;
	ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException;
	//ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, Scanner scn);
	//ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q);
}
