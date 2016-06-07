package rs.skupstinans.app;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import rs.skupstinans.service.RestBean;
import rs.skupstinans.service.UsersBean;

@ApplicationPath("/api")
public class App extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(RestBean.class);
        s.add(UsersBean.class);
        return s;
    }
}
