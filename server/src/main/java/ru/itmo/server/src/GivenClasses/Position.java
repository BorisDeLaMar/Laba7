package ru.itmo.server.src.GivenClasses;


public enum Position {
    MANAGER,
    LABORER,
    NULL,
    MANAGER_OF_CLEANING;
	public static String strConvert() {
		return "MANAGER, LABORER, MANAGER_OF_CLEANING, NULL";
	}
}
