package cmu.lee.twidder.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lee on 4/3/16.
 */

/**
 * Represents a user.
 * Holds Spring Security authentication information.
 */
public class User implements Comparable<User>, UserDetails {
    private Long id;
    private String username;
    private String name;
    private String password;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
    private Set<UserRole> roles;
    private Set<User> followers;
    private Set<User> followees;

    public User(Long id) {
        this.id = id;
    }

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(Long id, String username, String name, String password, boolean isEnabled, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.isAccountNonExpired = isEnabled;
        this.isAccountNonLocked = isEnabled;
        this.isCredentialsNonExpired = isEnabled;
        this.isEnabled = isEnabled;
        this.roles = new HashSet<>();

        if (roles != null) {
            for (String role : roles) {
                this.roles.add(new UserRole(role));
            }
        }
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public Set<User> getFollowees() {
        return followees;
    }

    public void setFollowees(Set<User> followees) {
        this.followees = followees;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public UserVO getVO() {
        return new UserVO();
    }

    public int compareTo(User user) {
        return id.compareTo(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof User) && id.equals(((User) obj).getId());
    }

    /**
     * Represents Spring Security user authorities.
     */
    private static class UserRole implements GrantedAuthority {
        private String role;

        private UserRole(String role) {
            this.role = role;
        }

        public String getAuthority() {
            return role;
        }
    }

    /**
     * For transferring information to view for display, on the purpose of
     * avoiding exposing too much information to the views.
     */
    public class UserVO {
        private UserVO() {
        }

        public Long getId() {
            return User.this.getId();
        }

        public String getName() {
            return User.this.getName();
        }
    }
}
