package cmu.lee.twidder.service;

import cmu.lee.twidder.dao.UserDAO;
import cmu.lee.twidder.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lee on 4/3/16.
 */

/**
 * Used as the user service for Spring Security Authentication Provider.
 */
@Service("userService")
@Transactional
public class UserService implements UserDetailsService {
    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }

    public User getUserByUsername(String username) {
        User user = userDAO.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("No user found named " + username);
        }

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public void saveUser(User user) {
        userDAO.saveUser(user);
    }
}
