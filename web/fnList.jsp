<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <style>
        h2 {
            text-align: center;
        }

        #menuBox {
            width: 80%;
            margin: 50px auto;
            border-style: none;
            cursor: pointer;
        }

        ul, li {
            list-style-type: none;
            margin: 5px auto;

        }

        div {
            border-bottom: 1px dotted #cccccc;
        }

        .mid {
            width: 80px;
            margin-right: 150px;
            float: right;

        }


        button {
            float: right;
            width: 70px;
            height: 30px;
            margin: 0 15px;
        }

        .right {
            width: 130px;
            float: right;
        }

        #editContainer {
            float: left;
        }

        #menuBox div#active {
            background: #08c;
        }
    </style>
    <script>
        //当加载完时，主动地显示数据
        window.onload = function () {
            loadData();

            //新增主功能
            document.oncontextmenu = function () {

                var mark = document.getElementById('fname');
                if (mark) {
                    alert('还有未完成的操作')
                } else {
                    var menuBox = document.getElementById('menuBox');
                    var uls = menuBox.children;
                    var lul = uls[uls.length - 1];
                    var li = document.createElement('li');
                    lul.appendChild(li);
                    li.innerHTML = '<div><input id="fname" placeholder="功能名称"/>' +
                        '<span class="mid"><input id="fhref" placeholder="功能请求"/></span>' +
                        '<span class="right"><select id="flag"><option value="">选择功能</option>' +
                        '<option value="1">菜单</option><option value="2">按钮</option></select></span></div>';

                    //取消功能
                    var cancel = document.getElementById('cancel');
                    cancel.onclick = function () {
                        lul.removeChild(li)
                    }

                    var save = document.getElementById('save');
                    save.onclick = function () {
                        var fname = document.getElementById('fname').value;
                        var fhref = document.getElementById('fhref').value;
                        var flag = document.getElementById('flag').value;
                        var pname = '-1';

                        var xhr = new XMLHttpRequest();

                        xhr.open('post', 'addFunction.do', true);

                        xhr.onreadystatechange = function () {
                            if (xhr.readyState == 4 && xhr.status == 200) {
                                doBack2();
                            }
                        }

                        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                        alert('fname=' + fname + '&fhref=' + fhref + '&flag=' + flag + '&pname=' + pname);
                        xhr.send('fname=' + fname + '&fhref=' + fhref + '&flag=' + flag + '&pname=' + pname);

                        function doBack2() {
                            alert('保存成功');
                            var menuBox = document.getElementById('menuBox');
                            var uls = menuBox.children;
                            menuBox.removeChild(uls[uls.length - 1]);
                            loadData();
                        }
                    }

                }


            }


            //写个函数，在当前div 的元素的父元素的最后一个子元素的位置增加一个新的增加功能的输入框，
            function addFn(current, ev) {
                ev.cancelBubble = true;//阻止事件冒泡
                var mark = document.getElementById('fname');
                if (mark) {
                    alert('还有未完成的操作')
                } else {
                    var sibl = current.nextElementSibling;
                    if (sibl) {
                        var li = document.createElement('li');
                        sibl.appendChild(li);
                        li.innerHTML = '<div><input id="fname" placeholder="功能名称"/>' +
                            '<span class="mid"><input id="fhref" placeholder="功能请求"/></span>' +
                            '<span class="right"><select id="flag"><option value="">选择功能</option>' +
                            '<option value="1">菜单</option><option value="2">按钮</option></select></span></div>';

                        //取消功能
                        var cancel = document.getElementById('cancel');
                        cancel.onclick = function () {
                            sibl.removeChild(li)

                        }

                        var save = document.getElementById('save');
                        save.onclick = function () {
                            var fname = document.getElementById('fname').value;
                            var fhref = document.getElementById('fhref').value;
                            var flag = document.getElementById('flag').value;

                            var span1 = current.firstElementChild;
                            var pname = span1.innerHTML;

                            var xhr = new XMLHttpRequest();

                            xhr.open('post', 'addFunction.do', true);

                            xhr.onreadystatechange = function () {
                                if (xhr.readyState == 4 && xhr.status == 200) {
                                    doBack2();
                                }
                            }

                            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                            alert('fname=' + fname + '&fhref=' + fhref + '&flag=' + flag + '&pname=' + pname);
                            xhr.send('fname=' + fname + '&fhref=' + fhref + '&flag=' + flag + '&pname=' + pname);

                            function doBack2() {
                                alert('保存成功');
                                var menuBox = document.getElementById('menuBox');
                                var uls = menuBox.children;
                                menuBox.removeChild(uls[uls.length - 1]);
                                loadData();
                            }
                        }

                    }
                }
            }

            //增加折叠效果
            function disp() {
                var menuBox = document.getElementById('menuBox');

                var ulChild = menuBox.getElementsByTagName('div');

                for (var i = 0; i < ulChild.length; i++) {
                    var child = ulChild[i];

                    child.ondblclick = function () {
                        var ul = this.nextElementSibling;

                        if (ul) {
                            if (ul.style.display == 'none') {
                                ul.style.display = 'block';
                            } else {
                                ul.style.display = 'none'
                            }
                        }
                    }
                    child.oncontextmenu = function (ev) {
                        addFn(this, ev);
                        return false;

                    }
                }
            }

            //加载功能页面
            function loadData() {
                var xhr = new XMLHttpRequest();

                xhr.open('post', 'funs.do', true);

                xhr.onreadystatechange = function () {
                    if (xhr.status == 200 && xhr.readyState == 4) {
                        doBack(xhr.responseText);
                    }

                }

                xhr.send();

                function doBack(result) {
                    var obj = JSON.parse(result);
                    var fns = obj.jsonObject;
                    var menuBox = document.getElementById('menuBox');
                    setposition(fns, menuBox);

                    //点击改变背景颜色
                    var menuBox = document.getElementById('menuBox');
                    var divs = menuBox.getElementsByTagName('div');
                    for (var i = 0; i < divs.length; i++) {
                        var div = divs[i];
                        div.onclick = function () {
                            var active = document.getElementById('active');
                            if (active) {
                                active.id = '';
                            }
                            this.id = 'active';
                        }
                    }


                    edit();

                    return disp();

                }

                function setposition(funs, position) {
                    var ul = document.createElement('ul');
                    position.appendChild(ul);

                    for (var i = 0; i < funs.length; i++) {
                        li = document.createElement('li');
                        ul.appendChild(li);
                        var div = document.createElement('div');
                        li.appendChild(div);
                        var fn = funs[i];

                        //判断是按钮还是菜单
                        var flg = parseInt(fn.flag);
                        var rf = (flg == 1 ? '菜单' : '按钮');
                        div.innerHTML = '<span>' + fn.fname + '</span>' + '<span class="mid">' + fn.fhref + '</span>' +
                            '<span class="right">' + rf + '</span>';

                        if (fn.children) setposition(fn.children, li);
                    }

                }


                //给编辑按钮增加点击事件
                function edit() {
                    var editBtn = document.getElementById('edit');
                    editBtn.onclick = function () {
                        var mark = document.getElementById('fname');
                        if (mark) {
                            alert('还有未完成的操作')
                        } else {
                            var active = document.getElementById('active');
                            if (!active) {
                                alert('未选择功能');
                                return;
                            }

                            var oldHtml = active.innerHTML;

                            var spans = active.children;
                            var fname = spans[0].innerHTML;
                            var fhref = spans[1].innerHTML;
                            var flag = spans[2].innerHTML;

                            if (flag == '菜单') {
                                active.innerHTML = '<div><input id="fname" placeholder="功能名称" value="' + fname + '"/>' +
                                    '<span class="mid"><input id="fhref" placeholder="功能请求" value="' + fhref + '"/></span>' +
                                    '<span class="right">' +
                                    '<select id="flag"><option value="1" >菜单</option><option value="2">按钮</option></select></span></div>';
                            } else {
                                active.innerHTML = '<div><input id="fname" placeholder="功能名称" value="' + fname + '"/>' +
                                    '<span class="mid"><input id="fhref" placeholder="功能请求" value="' + fhref + '"/></span>' +
                                    '<span class="right">' +
                                    '<select id="flag"><option value="2" >按钮</option><option value="1">菜单</option></select></span></div>';
                            }

                            //确认编辑
                            var saveEdit = document.getElementById('saveEdit');
                            saveEdit.onclick = function () {
                                var f = confirm('确认修改？');
                                if (f) {
                                    var oname = fname;
                                    var fname1 = document.getElementById('fname').value;
                                    var nFhref = document.getElementById('fhref').value;
                                    var flag = document.getElementById('flag').value;

                                    var xhr = new XMLHttpRequest();
                                    xhr.open('post', 'updateFunction.do', true);

                                    xhr.onreadystatechange = function () {
                                        if (xhr.readyState == 4 && xhr.status == 200) {
                                            doRback();
                                        }
                                    }

                                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                                    xhr.send('oname=' + oname + '&fname=' + fname1 + '&fhref=' + nFhref + '&flag=' + flag);

                                    function doRback() {
                                        alert('修改成功');
                                        var menuBox = document.getElementById('menuBox');
                                        var uls = menuBox.children;
                                        menuBox.removeChild(uls[uls.length - 1]);
                                        loadData();
                                    }
                                }
                            }

                            //取消编辑
                            var cancelEdit = document.getElementById('cancelEdit');
                            cancelEdit.onclick = function () {
                                active.innerHTML = oldHtml;
                            }
                        }
                    }
                }

                // ---------增加删除功能事件-----------------
                var deleteEdit = document.getElementById('deleteEdit');
                deleteEdit.onclick = function () {
                    var active = document.getElementById('active');
                    if(!active){
                        alert('请选择想要删除的功能')
                    }else{
                        var f = confirm('确认删除？');
                        if (f) {
                            var spans = active.children;
                            var nfname =spans[0] .innerHTML;

                            var xhr = new XMLHttpRequest();
                            xhr.open('post', 'deleteFunction.do', true);

                            xhr.onreadystatechange = function () {
                                if (xhr.readyState == 4 && xhr.status == 200) {
                                    doRback2();
                                }
                            }

                            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                            xhr.send('fname=' + nfname);

                            function doRback2() {
                                alert('删除成功');
                                var menuBox = document.getElementById('menuBox');
                                var uls = menuBox.children;
                                menuBox.removeChild(uls[uls.length - 1]);
                                loadData();
                            }
                        }
                    }

                }
            }
        }
    </script>
</head>
<body>
<h2>功能列表</h2>
<div>
    <div id="editContainer">
        <button id="deleteEdit">删除功能</button>
        <button id="edit">编辑功能</button>
        <button id="cancelEdit">取消编辑</button>
        <button id="saveEdit">保存编辑</button>
    </div>
    <button id="save">保存新增</button>
    <button id="cancel">取消新增</button>
</div>
<div id="menuBox">
    <ul>
        <li style="font-weight: 700;font-size: 1.1em;">
            <span>功能名称</span>
            <span class="mid">功能请求</span>
            <span class="right">功能类型</span>
        </li>
    </ul>
</div>
</body>
</html>
