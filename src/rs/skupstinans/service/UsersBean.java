package rs.skupstinans.service;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import rs.skupstinans.session.DatabaseBean;
import rs.skupstinans.users.User;
import rs.skupstinans.users.Users;

/**
 * Session Bean implementation class UsersBean
 */
@Stateless
@LocalBean
@Path("/user")
public class UsersBean implements UsersBeanRemote {

	@Context
	private HttpServletRequest request;
	
	@EJB
	private DatabaseBean database;
	
	private static Users users = null;	
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User login(User user) {
		User loggedInUser = (User) request.getSession().getAttribute("user");
		if (loggedInUser != null) {
			User retval = new User();
			retval.setUsername(loggedInUser.getUsername());
			retval.setUserType(loggedInUser.getUserType());
			return retval;
		}
		if (users == null) {
			users = database.getUsers();
		}
		
		for (User u : users.getUser()) {
			if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
				request.getSession().setAttribute("user", u);
				User retval = new User();
				retval.setUsername(u.getUsername());
				retval.setUserType(u.getUserType());
				return retval;	
			}	
		}
		return null;
	}

	@POST
	@Path("/logout")
	public void logout() {
		request.getSession().setAttribute("user", null);
	}
}
