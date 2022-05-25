package ru.itmo.server.src.GivenClasses;


public enum Status {
    FIRED("1"),
    HIRED("300"),
    RECOMMENDED_FOR_PROMOTION("50000"),
    REGULAR("4000"),
    PROBATION("20");
	//private String title;
	private int value;
	Status(String title){
		//this.title = title;
		value = title.length();
	}
	public static String strConvert() {
		return "FIRED, HIRED, RECOMMENDED_FOR_PROMOTION, REGULAR, PROBATION";
	}
	public boolean isBetter(Status state) {
		if(value <= state.value) {
			return false;
		}
		else {
			return true;
		}
	}
}
