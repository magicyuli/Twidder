package cmu.lee.twidder.entity;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lee on 4/6/16.
 */
public class Post {
    private static final SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:SS Z");

    private Long id;
    private User publisher;
    private User.UserVO publisherVO;
    private String content;
    private Date createdAt;
    private boolean deleted;

    public Post(User publisher, String content) {
        this(null, publisher, content, null, false);
    }

    public Post(Long id, User publisher, String content, Date createdAt, boolean deleted) {
        this.id = id;
        this.publisher = publisher;
        this.publisherVO = publisher.getVO();
        this.content = content;
        this.createdAt = createdAt;
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    @JsonIgnore
    public User getPublisher() {
        return publisher;
    }

    public User.UserVO getPublisherVO() {
        return publisherVO;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return format.format(createdAt);
    }

    @JsonIgnore
    public boolean isDeleted() {
        return deleted;
    }
}
