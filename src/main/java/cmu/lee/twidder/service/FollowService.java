package cmu.lee.twidder.service;

import cmu.lee.twidder.dao.FollowDAO;
import cmu.lee.twidder.entity.Follow;
import cmu.lee.twidder.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by lee on 4/5/16.
 */

/**
 * Operates on follow relations.
 */
@Service
@Transactional
public class FollowService {
    private FollowDAO followDAO;

    @Autowired
    public void setFollowDAO(FollowDAO followDAO) {
        this.followDAO = followDAO;
    }

    @Transactional(readOnly = true)
    public Set<User> getFollowers(User user) {
        if (user.getFollowers() == null) {
            user.setFollowers(followDAO.getFollowers(user));
        }

        return user.getFollowers();
    }

    @Transactional(readOnly = true)
    public Set<User> getFollowees(User user) {
        if (user.getFollowees() == null) {
            user.setFollowees(followDAO.getFollowees(user));
        }

        return user.getFollowees();
    }

    public void follow(User user1, User user2) {
        if (user1.equals(user2)) {
            return;
        }

        if (isFollowing(user1, user2)) {
            return;
        }

        followDAO.insertOrUpdateFollow(user1, user2, true);

        if (user1.getFollowees() != null) {
            user1.getFollowees().add(user2);
        }
    }

    public void unfollow(User user1, User user2) {
        if (user1.equals(user2)) {
            return;
        }

        if (!isFollowing(user1, user2)) {
            return;
        }

        followDAO.insertOrUpdateFollow(user1, user2, false);

        if (user1.getFollowees() != null) {
            user1.getFollowees().remove(user2);
        }
    }

    @Transactional(readOnly = true)
    public boolean isFollowing(User user1, User user2) {
        Follow follow = followDAO.getFollowByPair(user1, user2);

        return follow != null && follow.isFollowing();
    }
}
