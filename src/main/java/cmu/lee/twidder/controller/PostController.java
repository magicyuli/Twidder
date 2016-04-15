package cmu.lee.twidder.controller;

import cmu.lee.twidder.entity.AjaxBasicReturn;
import cmu.lee.twidder.entity.Post;
import cmu.lee.twidder.entity.User;
import cmu.lee.twidder.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by lee on 4/6/16.
 */

@Controller
@RequestMapping("/post")
@SessionAttributes("me")
public class PostController {
    private PostService postService;

    @Autowired
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    @ModelAttribute("me")
    public User setUser(HttpSession session) {
        return (User) session.getAttribute("me");
    }

    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseBody
    public AjaxBasicReturn publishNewPost(@RequestParam("content") String content,
                                          @ModelAttribute("me") User me) {
        Post post = new Post(me, content);
        try {
            postService.publishNewPost(post);
        }
        catch (Exception e) {
            e.printStackTrace();

            return new AjaxBasicReturn(false, e.getMessage());
        }

        return new AjaxBasicReturn(true, "");
    }

    @RequestMapping(value = "/allPosts", method = RequestMethod.GET)
    @ResponseBody
    public List<Post> getAllRelatedPosts(@RequestParam(value = "search", required = false) String search,
                                  @ModelAttribute("me") User me) {

        return postService.getAllRelatedPosts(me, search);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxBasicReturn getAllRelatedPosts(@RequestBody Post post,
                                         @ModelAttribute("me") User me) {
        try {
            postService.deletePost(post);
        }
        catch (Exception e) {
            e.printStackTrace();

            return new AjaxBasicReturn(false, e.getMessage());
        }

        return new AjaxBasicReturn(true, "");
    }
}
