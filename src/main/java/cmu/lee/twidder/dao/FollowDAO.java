package cmu.lee.twidder.dao;

import cmu.lee.twidder.entity.Follow;
import cmu.lee.twidder.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lee on 4/5/16.
 */

@Repository
public class FollowDAO {
    private static final String GET_FOLLOW_BY_IDS_SQL =
            "SELECT follower_id, followee_id, is_following FROM follows " +
                    "WHERE follower_id = :id1 AND followee_id = :id2";
    private static final String INSERT_FOLLOW_SQL =
            "INSERT INTO follows (follower_id, followee_id, is_following) " +
                    "VALUES ( :id1, :id2, :toFollow)";
    private static final String UPDATE_FOLLOW_SQL =
            "UPDATE follows SET is_following = :toFollow " +
                    "WHERE follower_id = :id1 AND followee_id = :id2";
    private static final String GET_FOLLOWERS_BY_ID_SQL =
            "SELECT u.id, name FROM follows JOIN users u ON (follower_id = u.id) " +
                    "WHERE followee_id = :id AND is_following = TRUE";
    private static final String GET_FOLLOWEES_BY_ID_SQL =
            "SELECT u.id, name FROM follows JOIN users u ON (followee_id = u.id) " +
                    "WHERE follower_id = :id AND is_following = TRUE";

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public FollowDAO(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Set<User> getFollowers(User user) {
        return getFollowersOrFollowees(user.getId(), GET_FOLLOWERS_BY_ID_SQL);
    }

    public Set<User> getFollowees(User user) {
        return getFollowersOrFollowees(user.getId(), GET_FOLLOWEES_BY_ID_SQL);
    }

    public void insertOrUpdateFollow(User follower, User followee, boolean toFollow) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id1", follower.getId());
        namedParams.addValue("id2", followee.getId());
        namedParams.addValue("toFollow", toFollow);

        Follow follow = getFollowByPair(follower, followee);

        if (follow == null) {
            jdbcTemplate.update(INSERT_FOLLOW_SQL, namedParams);
        }
        else {
            jdbcTemplate.update(UPDATE_FOLLOW_SQL, namedParams);
        }
    }

    public Follow getFollowByPair(User follower, User followee) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id1", follower.getId());
        namedParams.addValue("id2", followee.getId());

        Follow follow = null;

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(GET_FOLLOW_BY_IDS_SQL, namedParams);

        if (rowSet.next()) {
            follow = new Follow(rowSet.getLong("follower_id"), rowSet.getLong("followee_id"),
                    rowSet.getBoolean("is_following"));
        }

        return follow;
    }

    private Set<User> getFollowersOrFollowees(Long userId, String sql) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", userId);

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, namedParameters);

        Set<User> followers = new HashSet<>();

        while (rowSet.next()) {
            followers.add(new User(rowSet.getLong("id"), rowSet.getString("name")));
        }

        return followers;
    }
}
