package cmu.lee.twidder.entity;

/**
 * Created by lee on 4/5/16.
 */

/**
 * Represents the follow relations between pairs of users.
 */
public class Follow {
    private Long id1;
    private Long id2;
    private boolean isFollowing;

    public Follow(Long id1, Long id2, boolean isFollowing) {
        this.id1 = id1;
        this.id2 = id2;
        this.isFollowing = isFollowing;
    }

    public Long getId1() {
        return id1;
    }

    public Long getId2() {
        return id2;
    }

    public boolean isFollowing() {
        return isFollowing;
    }
}
