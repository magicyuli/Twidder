package cmu.lee.twidder.service;

import cmu.lee.twidder.dao.PostDAO;
import cmu.lee.twidder.entity.Post;
import cmu.lee.twidder.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lee on 4/6/16.
 */

/**
 * Operates on posts operations.
 */
@Service
@Transactional
public class PostService {
    private PostDAO postDAO;

    @Autowired
    public void setPostDAO(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

    public void publishNewPost(Post post) {
        postDAO.createNewPost(post);
    }

    @Transactional(readOnly = true)
    public List<Post> getAllRelatedPosts(User user, String search) {
        return postDAO.getAllRelatedPosts(user, search);
    }
}
