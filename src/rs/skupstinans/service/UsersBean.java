package rs.skupstinans.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import rs.skupstinans.users.User;

/**
 * Session Bean implementation class UsersBean
 */
@Stateless
@LocalBean
@Path("/user")
public class UsersBean implements UsersBeanRemote {

	@Context
	private HttpServletRequest request;
	
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
		if (User.getUsers().contains(user)) {
			user = User.getUsers().get(User.getUsers().indexOf(user));
			request.getSession().setAttribute("user", user);
			User retval = new User();
			retval.setUsername(user.getUsername());
			retval.setUserType(user.getUserType());
			return retval;
		}
		return null;
	}

	@POST
	@Path("/logout")
	public void logout() {
		request.getSession().setAttribute("user", null);
	}
}
