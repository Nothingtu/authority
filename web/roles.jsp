<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<% request.setCharacterEncoding("utf-8"); %>
<!DOCTYPE html>
<html>
    <head>
        <style>
            #container {
                background: aquamarine;
                width: 506px;
                margin: 30px auto;
                opacity: 70%;

            }

            .clearfix::after {
                content: '';
                display: block;
                clear: both;
            }

            ul, li {
                list-style: none;
                cursor: pointer;
            }

            #container > div {
                float: left;
            }

            #leftBox {
                border: 1px solid #000;
                width: 200px;
            }

            #midBox {
                width: 100px;
            }

            #rightBox {
                border: 1px solid #000;
                width: 200px;
            }

            button {
                display: block;
                width: 35px;
                margin: 15px auto;
                font-size: 1em;
            }

            #active {
                background: #ccc;
            }

            .txt {
                text-align: center;
                margin-left: 47px;

            }
        </style>
        <script src="js/jquery.js"></script>
        <script>
            //加载数据
            $(function () {
                //查询已分配的角色
                $.ajax({
                    url: 'findLinkedRoles.do',
                    type: 'post',
                    data: {uno: $('#hidden').val()},
                    async: true,
                    success: function (roles) {
                        var rols = roles.jsonObject;
                        for (var i = 0; i < rols.length; i++) {
                            var role = rols[i];
                            $('#linkedBox').append($('<li rno="'+role.rno+'">' + role.rname + '</li>'));
                        }
                        offsetToRight();
                    },
                    dataType: 'json'
                });


                //查询为分配的角色
                $.ajax({
                    url: 'findUnlinkedRoles.do',
                    type:'post',
                    async:true,
                    data:{uno:$('#hidden').val()},
                    success:function(ros){
                        var roles = ros.jsonObject;
                        for(var i = 0; i< roles.length;i++){
                            var role = roles[i];
                            $('#unLinkedBox').append($('<li rno="'+role.rno+'">'+role.rname+'</li>'));
                        }
                        offsetToLeft();
                    },
                    dataType:'json'
                });

            });


            //双击左边的移到右边未分配
            function offsetToRight(){
                $('#linkedBox li').off('dblclick').dblclick(function(){
                    $(this).appendTo($('#unLinkedBox'));
                    offsetToRight();
                });
            }

            //双击右边的移到左边分配
            function offsetToLeft(){
                $('#unLinkedBox li').off('dblclick').dblclick(function(){
                    $(this).appendTo($('#linkedBox'));
                    offsetToRight();
                });
            }

            //添加保存
            $(function(){
                $('#save').click(function () {
                    //获取所有的设置的角色
                    var rnos ='';
                    $('#linkedBox li').each(function(i,li){
                        rnos += $(li).attr('rno') + ',';
                    });

                    $.ajax({
                        url:'saveSetRoles.do',
                        type:'post',
                        data:{
                            'uno':$('#hidden').val(),
                            'rnos':rnos,
                        },
                        async:true,
                        success:function(result){
                            alert(result);
                        },
                    });
                })
            });

            //给button增加点击事件
            $(function(){
                $('#toRight').click(function(){
                    $('#linkedBox > li').appendTo($('#unLinkedBox'));
                });

                $('#toLeft').click(function(){
                    $('#unLinkedBox > li').appendTo($('#linkedBox'));
                });
            });

            //给点击的li和button改变背景颜色
            $(function () {
                $('ul').on('click','li',function () {

                    var active = $('#active');
                    if (active.length > 0) {
                        active[0].id = '';
                    }
                    this.id = 'active';
                });

                $('button').click(function () {
                    var active = $('#active');
                    if (active.length > 0) {
                        active[0].id = '';
                    }
                    this.id = 'active';
                });
            });

        </script>
    </head>
    <body>
        <input id="hidden" type="hidden" value="${param.uno}">
        <h2 align="center">为【${param.uname}】分配角色 </h2>
        <input type="button" id="save" value="保存编辑">
        <div id="container" class="clearfix">
            <div id="leftBox">
                <div style="border-bottom:2px solid #000"><span class="txt">已分配的功能</span></div>
                <ul id="linkedBox">
                </ul>
            </div>
            <div id="midBox">
                <button id="toRight">&gt;&gt;</button>
                <button id="toLeft">&lt;&lt;</button>
            </div>
            <div id="rightBox">
                <div style="border-bottom:2px solid #000"><span class="txt">未分配的功能</span></div>
                <ul id="unLinkedBox">
                </ul>
            </div>
        </div>
    </body>
</html>
