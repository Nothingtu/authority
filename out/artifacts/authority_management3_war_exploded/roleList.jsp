<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <style>
        h2 {
            text-align: center;
        }

        .tableBox {
            width: 80%;
            collapse: 0;
            text-align: center;
            border-collapse: collapse;
        }

        table th, td {
            border: 1px #000 solid;
        }

        .filter {
            width: 80%;
            margin: 0 auto;

        }

        .filter > ul {
            margin: 0;
            padding: 0;
        }

        .filter > ul li {
            float: left;
            margin: 10px auto;
        }

        .filter > ul li input[type=text] {
            width: 90px;
            height: 24px;
            border: 1px solid #000;
            padding: 0;

        }

        .filter > ul li input {
            margin-right: 10px;

        }

        li {
            list-style: none;
        }

        .clear::after {
            content: '';
            display: block;
            clear: both;
        }

        .pageBox ul li {
            float: right;
            margin-left: 10px;
            width: 98px;

        }

        #preBtn, #nextBtn {
            float: right;
            margin-left: 10px;
            width: 60px;
            border: 1px solid #000;
            cursor: pointer;
        }
    </style>
    <script>
        //当DOM加载完毕时，就触发的事件
        window.onload = function () {
            //主动触发的查询
            loadData();

            // 点击清空查询noneSelectBtn
            var noneSelectBtn = document.getElementById('noneSelectBtn');
            noneSelectBtn.onclick = function () {
                document.getElementById("rno").value = '';
                document.getElementById("rname").value = '';
                document.getElementById("rdesc").value = '';
                loadData();
            }

            // 点击查询selectBtn
            var selectBtn = document.getElementById('selectBtn');
            selectBtn.onclick = function () {
                loadData();
            }

            // 点击上一页preBtn
            var preBtn = document.getElementById('preBtn');
            preBtn.onclick = function () {
                var page = document.getElementById("pageSelectBtn").value;
                if (page == 1) return;
                page = parseInt(page);
                page--;
                document.getElementById("pageSelectBtn").value = page;
                loadData();
            }

            // 点击下一页nextBtn
            var nextBtn = document.getElementById("nextBtn");
            nextBtn.onclick = function () {
                var page = document.getElementById("pageSelectBtn").value;
                var options = document.getElementById("pageSelectBtn").children;
                var maxPage = options[options.length - 1].value;
                if (page === maxPage) return;
                page = parseInt(page);
                page++;
                document.getElementById("pageSelectBtn").value = page;
                loadData();

            }
            // 跳转第几页pageSelectBtn
            var pageSelectBtn = document.getElementById("pageSelectBtn");
            pageSelectBtn.onchange = function () {
                loadData();
            }

            // 每页显示多少条rowSelectBtn
            var rowSelectBtn = document.getElementById("rowSelectBtn");
            rowSelectBtn.onchange = function () {
                loadData();
            }

        }
        //定义loadData()函数
        function loadData() {
            var rno = document.getElementById("rno").value;
            var rname = document.getElementById("rname").value;
            var description = document.getElementById("rdesc").value;
            var page = document.getElementById("pageSelectBtn").value;
            var row = document.getElementById("rowSelectBtn").value;

            //ajax异步请求
            var xhr = new XMLHttpRequest();

            xhr.open('post', 'selectRoles.do', true);

            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    doBack(xhr.responseText);
                }
            }

            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhr.send('rno=' + rno + '&rname=' + rname + '&description=' + description + '&page=' + page + '&row=' + row);

            function doBack(result) {
                var pageSelectBtn = document.getElementById("pageSelectBtn");

                var obj = eval('('+result+')');
                var pageInfo = obj.jsonObject;
                var maxPage = pageInfo.maxPage;


                //判断跳页框中的值与查询得到的最大页数之间的关系
                if (parseInt(pageSelectBtn.value) > parseInt(maxPage)) pageSelectBtn.value = maxPage;

                //给selcet增加option标签
                for(var i = 2;i <= maxPage;i++){
                    var option = document.createElement('option');
                    option.innerHTML = i;
                    pageSelectBtn.appendChild(option);
                }

                //判断此次查询是否有查到信息
                var roleList = pageInfo.list;
                console.log(roleList);

                var tbody = document.getElementById("tbody");
                tbody.innerHTML ='';
                if(roleList.length == 0){
                    tbody.innerHTML = '<tr><td text-align="center" colspan="4">没有查询到用户信息</td></tr>';
                }

                //给tbody赋值内容
                for (var i = 0; i < roleList.length; i++) {

                    var role = roleList[i];
                    var tr = document.createElement('tr');
                    tbody.appendChild(tr);

                    var td1 = document.createElement('td');
                    td1.innerHTML = role.rno;
                    tr.appendChild(td1);

                    var td2 = document.createElement('td');
                    td2.innerHTML = role.rname;
                    tr.appendChild(td2);

                    var td3 = document.createElement('td');
                    td3.innerHTML = role.description;
                    tr.appendChild(td3);
                    var td4 = document.createElement('td');

                    td4.innerHTML = '<a href="updateRole.jsp?rno='+role.rno+'&rname='+role.rname+'&description='+role.description+'" target="right">修改 |</a>'+
                        '<a target="right" href="javascript:deleteRole('+role.rno+')">删除 '+
                        '|</a><a target="right" href="setFnForRole.jsp?rno='+role.rno+'&rname='+role.rname+'">分配功能</a> ';
                    tr.appendChild(td4);
                }


            }
        }
        function deleteRole(rno){
            var f = confirm('确认删除？');
            if(f){
                location.href = 'deleteRole.do?rno='+rno;
                alert('删除成功');
            }
        }

    </script>
</head>
<body>
<div class="container">
    <h2>角色列表</h2>
    <div class="filter">
        <ul class="clear">
            <li><input type="text" id="rno" placeholder='角色编号'></li>
            <li><input type="text" id="rname" placeholder='角色名称'></li>
            <li><input type="text" id="rdesc" placeholder='角色描述'></li>
            <li><input type="button" id="selectBtn" value='查询'></li>
            <li><input type="button" id="noneSelectBtn" value='清空查询'></li>
        </ul>
    </div>
    <table class="tableBox" align="center">
        <thead>
        <tr>
            <th>角色编号</th>
            <th>角色名称</th>
            <th>角色描述</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody id = "tbody">

        </tbody>
    </table>
    <div class="pageBox">
        <ul class="clear">
            <li id="nextBtn">下一页</li>
            <li id="preBtn">上一页</li>
            <li>
                跳转
                <select id="pageSelectBtn">
                    <option>1</option>
                </select>
                页
            </li>
            <li>
                每页
                <select id="rowSelectBtn">
                    <option>5</option>
                    <option>10</option>
                    <option>15</option>
                    <option>20</option>
                </select>
                条
            </li>
        </ul>
    </div>
</div>
</body>
</html>
