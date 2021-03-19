<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>登录主页</title>
        <style>
            .loginBox {
                background: #008c8c;
                width: 700px;
                height: 400px;
                border-radius: 12px;
                text-align: center;
                margin-top: 100px;
                margin-left: auto;
                margin-right: auto;
                padding: 10px;
                font-size: 30px;
            }

            input {
                width: 300px;
                height: 44px;
                padding-top: 5px;
                padding-left: 10px;
            }

            #pid {
                font-size: 0.5em;
                color:red;
                height:20px
            }

            .ipc {
                display: -webkit-flex;
                width: 450px;
                margin: 15px auto
            }

        </style>

    </head>
    <body>
        <div class="loginBox">
            <h3>登录</h3>
            <p id="pid">${sessionScope.flag==false?"用户名或密码错误":""}</p>
            <form action="login.do" method="get">
                <div class="ipc">
                    <label>账 号：<input type="text" name="uname"  value=""></label>
                </div>
                <div class="ipc">
                    <label>密 码：<input type="password" name="upassword"  value=""></label>
                </div>
                <input type="submit" style="height: 50px;width: 100px ;margin-top:20px;font-size: 0.8em" value="登录">
            </form>
        </div>



    </body>
</html>
