package ru.itmo.server.src.GivenClasses;
import org.json.*;
import ru.itmo.server.src.Comms.GistStaff;

public class Organization {
    private String fullName;
    private OrganizationType type;
    public Organization(JSONArray org) {
    	//setName(org.getString(0)); //разобраться в чем разница между optString и getString
    	try {
    		setName(org.getString(0));
    		setType(OrganizationType.valueOf(org.getString(1)));
    	}
    	catch(IllegalArgumentException e) {
    		System.out.println("For " + fullName + ": Available organization types are: " + OrganizationType.strConvert());
    	}
    	catch(JSONException e) {
    		System.out.println(e.getMessage());
    	}
    }
	public Organization(String fullname, OrganizationType type){
		this.fullName = fullname;
		this.type = type;
	}
	public Organization(){}
    public Organization getOrganization(String fullName, String type) {
    	setName(fullName);
    	try {
    		setType(OrganizationType.valueOf(type));
    	}
    	catch(IllegalArgumentException e) {
    		setType(null);
    	}
		return new Organization(fullName, this.type);
    }
    
    public String getName() {
    	return fullName;
    }
    public void setName(String fullName) {
    	this.fullName = fullName;
    }
    
    public OrganizationType getType() {
    	return type;
    }
    public void setType(OrganizationType type) {
    	this.type = type;
    }
}
