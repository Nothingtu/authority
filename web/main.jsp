<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>欢迎登录成功</title>
        <style>
            body{
                height: 100%;
                width: 100%;
                margin: 0;
                padding: 0;
                border: 0;
                min-width: 1519.2px;
                overflow: scroll;
            }
            .top{
                background-color:#008c8c;
                height:15%;
                width:100%;
            }

            .left{
                background:#ccc;
                height:85%;
                width:30%;
                float:left;
            }
            .right{
                background:#eee;
                height:85%;
                width:70%;
                float:right;
            }
            #span{
                float:right;
                margin-right:15px;
                margin-top:50px;
                text-align: center;
            }
            #text{
                font-size: 2em;
                line-height: 80px;
                text-align: center;
            }
            ul li{
                list-style-type: none;
                padding:0;
                margin:7px 0;
            }
            li{
                margin-left:15px;
            }
            h4{
                margin-top:20px;
            }
            .left div{
                cursor:pointer;
            }

            a{
                text-decoration: none;
            }
            .clear::after{
                content:"";
                display: block;
                clear:both
            }
            #urgt{
                marign:5px ;
            }
        </style>
        <script>
            //用连接触发js函数
            function toExit() {
                var b = confirm("确认注销？");
                if(!b) return;
                location.href="urgt.do";
            }
        </script>
    </head>
    <body >
        <div>
            <div class="top">
                <span id="text">*****欢迎进入渡一管理系统*****</span>
                <span id="span">欢迎【${sessionScope.uname}】登录<br/>
                    <a id="urgt" href="javascript:toExit()">【注销】</a>
                    <a id="updatePswd" href="updatePswd.do?uno=${sessionScope.unno}" target="right">【修改密码】</a>
                </span>
            </div>
            <div class="left">
                <ul>
                    <li>
                        <div><a href="#" target="right">权限管理</a></div>
                        <ul>
                            <li><div><a href="userList.do" target="right">用户管理</a></div></li>
                            <li><div><a href="roleList.jsp" target="right">角色管理</a></div></li>
                            <li><div><a href="funList.do" target="right">功能管理</a></div></li>
                        </ul>
                    </li>

                    <li>
                        <div>基本信息管理</div>
                    </li>
                    <li>
                        <div>商品管理</div>
                    </li>
                    <li>
                        <div>供应商管理</div>
                    </li>
                    <li>
                        <div>库房管理</div>
                    </li>
                    <li>
                        <div>财务管理</div>
                    </li>
                    <li>
                        <div>系统管理</div>
                    </li>
                </ul>
            </div>
            <div  class="right clear">
                <iframe name="right" width="100%" height="100%" frameborder="0"></iframe>
            </div>
        </div>
    </body>
</html>
