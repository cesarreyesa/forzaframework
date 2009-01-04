package org.forzaframework.security.service;

import org.forzaframework.security.User;
import org.forzaframework.security.Role;

import java.util.List;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 15:00:34
 */
public interface UserManager {

    /**
     * Retrieves a user by username.  An exception is thrown if now user
     * is found.
     *
     * @param username
     * @return User
     */
    public User getUser(String username);

    /**
     * Retrieves a list of users, filtering with parameters on a user object
     * @param user parameters to filter on
     * @return List
     */
    public List getUsers(User user);

    /**
     * Saves a user's information
     *
     * @param user the user's information
     */
    public void saveUser(User user);

    public void newUser(User user);

    /**
     * Removes a user from the database by their username
     *
     * @param username the user's username
     */
    public void removeUser(String username);

    public List getRoles();

    public Role getRole(String rolename);

    public void saveRole(Role role);

    public void removeRole(String rolename);

}
