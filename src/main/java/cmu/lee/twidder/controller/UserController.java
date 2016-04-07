package cmu.lee.twidder.controller;

import cmu.lee.twidder.entity.User;
import cmu.lee.twidder.service.UserService;
import cmu.lee.twidder.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by lee on 4/3/16.
 */

@Controller
@RequestMapping("/user")
@SessionAttributes("me")
public class UserController {
    private UserService userService;
    private UserUtils userUtils;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserUtils(UserUtils userUtils) {
        this.userUtils = userUtils;
    }

    @ModelAttribute("me")
    public User setUser(HttpSession session) {
        return (User) session.getAttribute("me");
    }

    @RequestMapping(value = "/allUsers", method = RequestMethod.GET)
    @ResponseBody
    public List<User.UserVO> getAllUsers() {
        return userUtils.usersToUserVOs(userService.getAllUsers());
    }

}
