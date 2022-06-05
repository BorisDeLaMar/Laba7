package ru.itmo.server.src.GivenClasses;


public enum Position {
    MANAGER("MANAGER"),
    LABORER("LABORER"),
    NULL("NULL"),
    MANAGER_OF_CLEANING("MANAGER_OF_CLEANING");

    private final String manager;
    private Position(String manager) {
        this.manager = manager;
    }
    public String getName(){
        return manager;
    }
    public static String strConvert() {
		return "MANAGER, LABORER, MANAGER_OF_CLEANING, NULL";
	}
}
