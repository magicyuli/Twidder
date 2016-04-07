package cmu.lee.twidder.dao;

import cmu.lee.twidder.entity.Post;
import cmu.lee.twidder.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lee on 4/6/16.
 */

@Repository
public class PostDAO {
    private static final String INSERT_POST_SQL =
            "INSERT INTO posts (publisher_id, content, created_at, deleted) " +
                    "VALUES (:publisherId, :content, :createdAt, :deleted)";
    private static final String GET_POSTS_WITHOUT_SEARCH_SQL =
            "SELECT p.id as post_id, publisher_id, name, content, created_at " +
                    "FROM posts p JOIN users u ON (publisher_id = u.id) WHERE (publisher_id = :id " +
                    "OR publisher_id IN (SELECT followee_id FROM follows WHERE follower_id = :id AND is_following = TRUE)) " +
                    "AND deleted = FALSE " +
                    "ORDER BY created_at DESC";
    private static final String GET_POSTS_WITH_SEARCH_SQL =
            "SELECT p.id as post_id, publisher_id, name, content, created_at " +
                    "FROM posts p JOIN users u ON (publisher_id = u.id) WHERE (publisher_id = :id " +
                    "OR publisher_id IN (SELECT followee_id FROM follows WHERE follower_id = :id AND is_following = TRUE)) " +
                    "AND deleted = FALSE " +
                    "AND MATCH(content) AGAINST(:search IN BOOLEAN MODE) " +
                    "ORDER BY created_at DESC";

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void createNewPost(Post post) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("publisherId", post.getPublisher().getId());
        namedParams.addValue("content", post.getContent());
        namedParams.addValue("createdAt", new Date());
        namedParams.addValue("deleted", false);

        jdbcTemplate.update(INSERT_POST_SQL, namedParams);
    }

    public List<Post> getAllRelatedPosts(User user, String search) {
        if (search != null && search.length() > 0) {
            return getAllRelatedPosts(GET_POSTS_WITH_SEARCH_SQL, user, search);
        }
        else {
            return getAllRelatedPosts(GET_POSTS_WITHOUT_SEARCH_SQL, user, search);
        }
    }

    private List<Post> getAllRelatedPosts(String sql, User user, String search) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", user.getId());
        namedParams.addValue("search", search);

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, namedParams);

        List<Post> posts = new ArrayList<>();

        while (rowSet.next()) {
            posts.add(new Post(rowSet.getLong("post_id"),
                    new User(rowSet.getLong("publisher_id"), rowSet.getString("name")),
                    rowSet.getString("content"),
                    rowSet.getDate("created_at"), true));
        }

        return posts;
    }
}
