<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="/static/js/jquery-2.2.3.min.js"></script>
    <script src="/static/js/common.js"></script>
    <title>Title</title>
    <style>
        label {
            display: block;
        }
    </style>
</head>
<body>
    <h1>All users</h1>
    <div id="allUsers"></div>

    <h1>Create user</h1>
    <form action="/admin/create" method="post" id="createUserForm">
        <label for="username">Username: <input type="text" id="username" name="username"></label>
        <label for="name">Name: <input type="text" id="name" name="name"></label>
        <label for="password">Password: <input type="password" id="password" name="password"></label>
        <label for="roles">Roles:
            <select type="text" id="roles" name="roles" multiple>
                <option value="ROLE_ADMIN">admin</option>
                <option value="ROLE_USER">user</option>
            </select>
        </label>
        <button id="submit">Create</button>
    </form>

    <script>
        $(function() {
            ajaxWrapper("/user/allUsers", "GET", {}, function(data) {
                var $container = $("#allUsers");

                for (var i in data) {
                    $container.append($("<p></p>").text((JSON.stringify(data[i]))));
                }
            });

            $(document).on("submit", "#createUserForm", function(e) {
                e.preventDefault();

                ajaxWrapper("/admin/create", "POST", {
                    username: $("#username").val(),
                    name: $("#name").val(),
                    password: $("#password").val(),
                    roles: $("#roles").val().join(",")
                }, function(data) {
                    if (data.success) {
                        location.reload();
                    }
                });
            })
        });
    </script>
</body>
</html>
