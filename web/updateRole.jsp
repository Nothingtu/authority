<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%request.setCharacterEncoding("utf-8");%>
<html>
    <head>
        <title>Title</title>
        <style>
            #container{
                width: 40%;
                margin:30px auto;
            }
            input{
                margin:15px;
                width: 150px;
            }
        </style>
    </head>
    <script>

        window.onload=function () {
            var sub = document.getElementById('sub');
            sub.onclick = function () {
                alert('修改成功');
            }
        }

    </script>
    <body>
        <h2>角色编辑</h2>
        <div id="container">
            <form action="updRole.do" method="post">
                <input name="rno" type="hidden" value="${param.rno}">
                <input name="rname" value="${param.rname}" placeholder="角色名称" type="text"><br>
                <input name="description" value="${param.description}" placeholder="角色描述" type="text"><br>
                <input type="submit" width="50px" id="sub">
            </form>
        </div>
    </body>
</html>
