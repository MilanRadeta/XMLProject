package rs.skupstinans.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
	public boolean login(User user) {
		if (User.getUsers().contains(user)) {
			return true;
		}
		return false;
	}
}
