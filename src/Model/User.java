package Model;

import Util.Connect;

public class User {
	 static Connect connect = Connect.getInstance();
	    public static String userId;
	    private String role; 

	    public String getRole() {
	        return role;
	    }

	    public void setRole(String role) {
	        this.role = role;
	    }

	    public static void setUserId(String userId) {
	       User.userId = userId;
	    }

	    public static User currentUser;

	    public static User getCurrentUser() {
	        return currentUser;
	    }
	    public static void setCurrentUser(User user) {
	        currentUser = user;
	    }
}
