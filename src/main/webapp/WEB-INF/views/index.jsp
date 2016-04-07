<%@ page import="cmu.lee.twidder.entity.User" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: lee
  Date: 4/3/16
  Time: 8:00 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="/static/js/jquery-2.2.3.min.js"></script>
    <script src="/static/js/common.js"></script>
    <title>Homepage</title>
    <style>
        .clear {
            clear: both;
        }
    </style>
</head>
<body>
<form:form action="/logout" method="post" cssStyle="float: right;">
    <input type="submit" value="LOGOUT">
</form:form>
<div class="clear"></div>
<h1>I am <%=((User) request.getAttribute("me")).getName()%>
</h1>

<h1>I'm following</h1>
<div id="followingContainer"></div>

<h1>I'm followed by</h1>
<div id="followedByContainer"></div>

<h1>Follow operations</h1>
<select name="userList" id="userList"></select>
<button id="follow">Follow</button>
<button id="unfollow">Unfollow</button>

<h1>All posts I can see</h1>
<div id="allPosts"></div>
<input type="text" id="search" />
<button id="searchPosts">Search</button>

<h1>New post</h1>
<textarea name="post" id="post" cols="50" rows="10"></textarea>
<button id="newPost">Post</button>


<script>
    var userId = <%=((User) request.getAttribute("me")).getId()%>;

    $(function() {

        // populate following list
        ajaxWrapper("/follow/getFollowees", "GET", {id: userId}, function (data) {
            var $container = $("#followingContainer");

            for (var i in data) {
                $container.append($("<p></p>").text((JSON.stringify(data[i]))));
            }
        });

        // populate followed by list
        ajaxWrapper("/follow/getFollowers", "GET", {id: userId}, function (data) {
            var $container = $("#followedByContainer");

            for (var i in data) {
                $container.append($("<p></p>").text((JSON.stringify(data[i]))));
            }
        });

        // populate all users list
        ajaxWrapper("/user/allUsers", "GET", {}, function(data) {
            var $container = $("#userList");

            for (var i in data) {
                $container.append($("<option></option>")
                        .attr("value", data[i].id + "," + data[i].name)
                        .text(data[i].name));
            }
        });

        // populate all posts
        ajaxWrapper("/post/allPosts", "GET", {}, function(data) {
            var $container = $("#allPosts");

            for (var i in data) {
                $container.append($("<p></p>").text(JSON.stringify(data[i])));
            }
        });

        $(document).on("click", "#follow", function() {
            // follow a user
            var userId = $("#userList").val().split(",")[0];
            var userName = $("#userList").val().split(",")[1];

            ajaxWrapper("/follow/follow", "POST", {id: userId, name: userName}, function(data) {
                if (data.success) {
                    location.reload();
                }
                else {
                    alert(data.message);
                }
            });
        }).on("click", "#unfollow", function() {
            // unfollow a user
            var userId = $("#userList").val().split(",")[0];
            var userName = $("#userList").val().split(",")[1];

            ajaxWrapper("/follow/unfollow", "POST", {id: userId, name: userName}, function(data) {
                if (data.success) {
                    location.reload();
                }
                else {
                    alert(data.message);
                }
            });
        }).on("click", "#searchPosts", function() {
            // follow a user
            var search = $("#search").val();

            ajaxWrapper("/post/allPosts", "POST", {search: search}, function(data) {
                var $container = $("#allPosts");
                $container.empty();

                for (var i in data) {
                    $container.append($("<p></p>").text(JSON.stringify(data[i])));
                }
            });
        }).on("click", "#newPost", function() {
            // follow a user
            var content = $("#post").val();

            ajaxWrapper("/post/publish", "POST", {content: content}, function(data) {
                if (data.success) {
                    location.reload();
                }
                else {
                    alert(data.message);
                }
            });
        });
    });

</script>
</body>
</html>
