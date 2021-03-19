<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            table {
                width: 400px;
                margin: 30px auto;
                font-size: 2em;
            }

            tr {
                margin: 3px auto;
            }

            form {
                width: 600px;
                border: 1px solid #ccc;
                background: #008c8c;
                border-radius: 10px;
                margin: 30px auto;
            }

            .inputTag {
                width: 220px;
                height: 27px;
            }

            #submit{
                height: 50px;
                width: 80px;
                font-size: 20px;
                margin:15px auto
            }
        </style>
    </head>
    <body>
        <form action="updateUser.do" method="post">
            <table>
                <caption>用户修改</caption>
                <input type="hidden" name="uno" value="${sessionScope.user.uno}">
                <tr>
                    <td>用户名</td>
                    <td><input class="inputTag" name="urealname" value="${sessionScope.user.urealname}"></td>
                </tr>
                <tr>
                    <td>昵称</td>
                    <td><input class="inputTag" name="uname" value="${sessionScope.user.uname}"></td>
                </tr>
                <tr>
                    <td>年龄</td>
                    <td><input class="inputTag" name="uage" value="${sessionScope.user.uage}"></td>
                </tr>
                <tr>
                    <td>性别</td>
                    <td>
                        <c:choose>
                            <c:if test="${sessionScope.user.uage} == '男'">
                                <input type="radio" name="usex" style="zoom:2" value="男" checked="checked">男
                                <input type="radio" name="usex" style="zoom:2" value="女">女
                            </c:if>
                            <c:otherwise>
                                <input type="radio" name="usex" style="zoom:2" value="男" >男
                                <input type="radio" name="usex" style="zoom:2" value="女" checked="checked">女
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" value="保存" id="submit">
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
