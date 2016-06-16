package rs.skupstinans.users;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String username;
	private String password;
	private UserType userType;
	
	private static List<User> users = new ArrayList<>();
	
	static {
		User user = new Odbornik();
		user.setUsername("odbornik");
		user.setPassword("odbornik");
		users.add(user);
		user = new Predsednik();
		user.setUsername("predsednik");
		user.setPassword("predsednik");
		users.add(user);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public static List<User> getUsers() {
		return users;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User user = (User) obj;
			return user.username.equals(username) && user.password.equals(password);
		}
		return super.equals(obj);
	}
	
	
}
