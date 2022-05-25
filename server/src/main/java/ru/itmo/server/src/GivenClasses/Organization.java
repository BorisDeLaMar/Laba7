package ru.itmo.server.src.GivenClasses;
import org.json.*;
import ru.itmo.server.src.Comms.GistStaff;

public class Organization {
    private String fullName;
    private OrganizationType type;
    private static boolean flag = true;
    public static boolean getFlag() {
    	return flag;
    }
    public static void setFlag(boolean f) {
    	flag = f;
    }
    public Organization(JSONArray org) {
    	//setName(org.getString(0)); //разобраться в чем разница между optString и getString
    	try {
    		setName(org.getString(0));
    		setType(OrganizationType.valueOf(org.getString(1)));
    	}
    	catch(IllegalArgumentException e) {
    		flag = false;
    		System.out.println("For " + fullName + ": Available organization types are: " + OrganizationType.strConvert());
    	}
    	catch(JSONException e) {
    		flag = false;
    		System.out.println(e.getMessage());
    	}
    }
    public Organization(String fullName, String type) {
    	setName(fullName);
		String reply = GistStaff.getReply();
    	try {
    		setType(OrganizationType.valueOf(type));
    	}
    	catch(IllegalArgumentException e) {
    		setFlag(false);
    		reply += "\nAvailable organization types are: " + OrganizationType.strConvert() + "\n";
			GistStaff.setReply(reply);
    	}
    }
	public Organization(String fullName, OrganizationType organizationType){
		this.fullName = fullName;
		this.type = organizationType;
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
