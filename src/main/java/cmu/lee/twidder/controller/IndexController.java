package cmu.lee.twidder.controller;

import cmu.lee.twidder.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * Created by lee on 4/6/16.
 */

@Controller
@RequestMapping("/")
@SessionAttributes("me")
public class IndexController {

    @ModelAttribute("me")
    public User setUser(HttpSession session) {
        return (User) session.getAttribute("me");
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getIndexPage(@ModelAttribute("me") User me) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("me", me);

        return mav;
    }
}
