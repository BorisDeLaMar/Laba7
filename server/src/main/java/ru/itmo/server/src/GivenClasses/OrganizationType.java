package ru.itmo.server.src.GivenClasses;


public enum OrganizationType {
    COMMERCIAL,
    GOVERNMENT,
    TRUST,
    NULL,
    PRIVATE_LIMITED_COMPANY;
	public static String strConvert() {
		return "COMMERCIAL, GOVERNMENT, TRUST, PRIVATE_LIMITED_COMPANY, NULL";
	}
}
