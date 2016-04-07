DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL,
  name VARCHAR(50) NOT NULL,
  enabled BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB, CHARACTER SET=UTF8;

INSERT INTO users (name, username, password, enabled) VALUES ("lee", "lee@cmu.edu", "$2a$10$oI4JnWQy4GnXSqS2mnvafO2Q4BPFyxk5KGJUNPfhbN2m6kXfXgrfy", TRUE);

DROP TABLE IF EXISTS authorities;

CREATE TABLE IF NOT EXISTS authorities (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50),
  KEY (username)
) ENGINE=InnoDB, CHARACTER SET=UTF8;

INSERT INTO authorities (username, authority) VALUES ("lee@cmu.edu", "ROLE_USER");
INSERT INTO authorities (username, authority) VALUES ("lee@cmu.edu", "ROLE_ADMIN");

DROP TABLE IF EXISTS follows;

CREATE TABLE IF NOT EXISTS follows (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  follower_id BIGINT NOT NULL,
  followee_id BIGINT NOT NULL,
  is_following BOOLEAN NOT NULL,
  KEY (follower_id),
  KEY (followee_id),
  KEY (follower_id, followee_id),
  FOREIGN KEY (follower_id) REFERENCES users (id),
  FOREIGN KEY (followee_id) REFERENCES users (id)
) ENGINE=InnoDB, CHARACTER SET=UTF8;

DROP TABLE IF EXISTS posts;

CREATE TABLE IF NOT EXISTS posts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  publisher_id BIGINT NOT NULL,
  content TEXT NOT NULL,
  created_at DATETIME NOT NULL,
  deleted BOOLEAN DEFAULT FALSE,
  KEY (publisher_id),
  FULLTEXT (content),
  FOREIGN KEY (publisher_id) REFERENCES users (id)
) ENGINE=InnoDB, CHARACTER SET=UTF8;