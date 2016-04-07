package cmu.lee.twidder.dao;

import cmu.lee.twidder.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

/**
 * Created by lee on 4/3/16.
 */

@Repository
public class UserDAO {
    private static final String GET_USER_BY_NAME_SQL =
            "SELECT u.id, username, name, password, enabled, authority " +
            "FROM users u JOIN authorities a USING (username) WHERE username = :username";
    private static final String GET_ALL_USERS_SQL =
            "SELECT id, username, name, enabled FROM users";
    private static final String INSERT_USER_SQL =
            "INSERT INTO users (username, name, password, enabled)" +
                    "VALUES (:username, :name, :password, :enabled)";
    private static final String INSERT_ROLE_SQL =
            "INSERT INTO authorities (username, authority)" +
                    "VALUES (:username, :role)";


    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAO(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public User getUserByUsername(String username) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("username", username);

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(GET_USER_BY_NAME_SQL, namedParameters);

        int rowIdx = 0;
        Long id = null;
        String name = null;
        String password = null;
        boolean enabled = false;
        Set<String> roles = new HashSet<>();

        while (rowSet.next()) {
            if (rowIdx == 0) {
                id = rowSet.getLong("id");
                name = rowSet.getString("name");
                password = rowSet.getString("password");
                enabled = rowSet.getBoolean("enabled");
            }

            roles.add(rowSet.getString("authority"));

            rowIdx++;
        }

        if (rowIdx == 0) {
            return null;
        }

        return new User(id, username, name, password, enabled, roles);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(GET_ALL_USERS_SQL, (Map<String, ?>) null);

        while (rowSet.next()) {
            users.add(new User(rowSet.getLong("id"), rowSet.getString("username"),
                    rowSet.getString("name"), "", rowSet.getBoolean("enabled"), null));
        }

        return users;
    }

    public void saveUser(User user) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("username", user.getUsername());
        namedParams.addValue("name", user.getName());
        namedParams.addValue("password", user.getPassword());
        namedParams.addValue("enabled", user.isEnabled());

        jdbcTemplate.update(INSERT_USER_SQL, namedParams);

        MapSqlParameterSource[] batchParams = new MapSqlParameterSource[user.getAuthorities().size()];
        int i = 0;

        for (GrantedAuthority role : user.getAuthorities()) {
            namedParams = new MapSqlParameterSource();
            namedParams.addValue("username", user.getUsername());
            namedParams.addValue("role", role.getAuthority());

            batchParams[i++] = namedParams;
        }

        jdbcTemplate.batchUpdate(INSERT_ROLE_SQL, batchParams);
    }
}
