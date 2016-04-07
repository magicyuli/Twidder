package cmu.lee.twidder.util;

import cmu.lee.twidder.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lee on 4/6/16.
 */

@Component
public class UserUtils {
    public List<User.UserVO> usersToUserVOs(List<User> users) {
        List<User.UserVO> userVOs = new ArrayList<>(users.size());

        for (User user : users) {
            userVOs.add(user.getVO());
        }

        return userVOs;
    }

    public Set<User.UserVO> usersToUserVOs(Set<User> users) {
        Set<User.UserVO> userVOs = new HashSet<>(users.size());

        for (User user : users) {
            userVOs.add(user.getVO());
        }

        return userVOs;
    }
}
