package cmu.lee.twidder.controller;

import cmu.lee.twidder.entity.AjaxBasicReturn;
import cmu.lee.twidder.entity.User;
import cmu.lee.twidder.service.FollowService;
import cmu.lee.twidder.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Set;

/**
 * Created by lee on 4/5/16.
 */

@Controller
@RequestMapping("/follow")
@SessionAttributes("me")
public class FollowController {
    private FollowService followService;
    private UserUtils userUtils;

    @Autowired
    public void setFollowDAO(FollowService followService) {
        this.followService = followService;
    }

    @Autowired
    public void setUserUtils(UserUtils userUtils) {
        this.userUtils = userUtils;
    }

    @ModelAttribute("me")
    public User setUser(HttpSession session) {
        return (User) session.getAttribute("me");
    }

    @RequestMapping(value = "/getFollowers", method = RequestMethod.GET)
    @ResponseBody
    public Set<User.UserVO> getFollowers(@RequestParam("id") Long id,
                                         @ModelAttribute("me") User me) {
        User targetUser = me;

        if (!id.equals(me.getId())) {
            targetUser = new User(id);
        }

        return userUtils.usersToUserVOs(followService.getFollowers(targetUser));
    }

    @RequestMapping(value = "/getFollowees", method = RequestMethod.GET)
    @ResponseBody
    public Set<User.UserVO> getFollowees(@RequestParam("id") Long id,
                                         @ModelAttribute("me") User me) {
        User targetUser = me;

        if (!id.equals(me.getId())) {
            targetUser = new User(id);
        }

        return userUtils.usersToUserVOs(followService.getFollowees(targetUser));
    }

    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public AjaxBasicReturn follow(@RequestParam("id") Long id,
                                  @RequestParam("name") String name,
                                  @ModelAttribute("me") User me) {
        try {
            User targetUser = new User(id, name);
            followService.follow(me, targetUser);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new AjaxBasicReturn(false, e.getMessage());
        }

        return new AjaxBasicReturn(true, "");
    }

    @RequestMapping(value = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public AjaxBasicReturn unfollow(@RequestParam("id") Long id,
                                    @RequestParam("name") String name,
                                    @ModelAttribute("me") User me) {
        try {
            User targetUser = new User(id, name);
            followService.unfollow(me, targetUser);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new AjaxBasicReturn(false, e.getMessage());
        }

        return new AjaxBasicReturn(true, "");
    }
}
