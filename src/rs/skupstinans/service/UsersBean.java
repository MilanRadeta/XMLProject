package rs.skupstinans.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import rs.skupstinans.users.User;

/**
 * Session Bean implementation class UsersBean
 */
@Stateless
@LocalBean
@Path("/user")
public class UsersBean implements UsersBeanRemote {

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String login(User user) {
		if (User.getUsers().contains(user)) {
			return user.getUsername();
		}
		return null;
	}
}
