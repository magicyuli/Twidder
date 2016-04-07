package cmu.lee.twidder.controller;

import cmu.lee.twidder.entity.AjaxBasicReturn;
import cmu.lee.twidder.entity.User;
import cmu.lee.twidder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

/**
 * Created by lee on 4/4/16.
 */

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBcryptEncoder(BCryptPasswordEncoder bcryptEncoder) {
        this.bcryptEncoder = bcryptEncoder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "admin";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public AjaxBasicReturn createUser(@RequestParam("username") String username,
                                      @RequestParam("name") String name,
                                      @RequestParam("password") String password,
                                      @RequestParam("roles") Set<String> roles) {
        User user = new User(null, username, name, bcryptEncoder.encode(password), true, roles);

        try {
            userService.saveUser(user);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new AjaxBasicReturn(false, e.getMessage());
        }

        return new AjaxBasicReturn(true, "");
    }
}
