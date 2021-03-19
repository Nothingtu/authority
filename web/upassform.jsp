<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
    <style>
        form{
            background: lightblue;
            width: 600px;
            height: 350px;
            padding:30px;
            margin:50px auto;
            font-size: 30px;
        }
        input{
            width: 300px;
            height: 50px;
            margin：20px;
        }

        ul li{
           list-style-type: none;
            margin: 20px;
        }
        h2{
            text-align: center;
        }

        .lastinp{
            width: 80px;
            font-size:22px ;
            margin:10px 210px;
        }
    </style>
        <script>
            function cfpwsd(){
                var npass = document.getElementById("p1");
                var cpass = document.getElementById("p2");
                if(npass.value == cpass.value){
                    var opass = document.getElementById("p3");
                    location.href='confirmPswd.do?opass='+opass.value+"&npass="+npass.value;
                }else{
                    confirm("新密码和确认密码不一致");
                    return;
                }
            }
        </script>
    </head>
    <body>
        <div>
            <h2>修改密码</h2>
            <form action="javascript:cfpwsd()" method="post">
                <ul>
                    <li>新密码：<input id="p1" name="npass" type="password"></li>
                    <li>确认密码：<input id="p2" name="cpass" type="password"></li>
                    <li>原密码：<input id="p3" name="opass" type="password"></li>
                    <li><input type="submit" class="lastinp" value="保存"></li>
                </ul>
            </form>
        </div>
    </body>
</html>
