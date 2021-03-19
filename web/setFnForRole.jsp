<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%request.setCharacterEncoding("utf-8");%>
<html>
    <head>
        <script src="js/jquery.js"></script>
        <style>
            #menuBox{
                width: 85%;
                margin:10px auto;
            }
            ul,li{
                list-style-type: none;
            }
            .fntarget,.fnFlag{
                float: right;
                cursor: pointer;
                margin-bottom: 10px;
                margin-right: 100px;
                width:100px;
            }
            .fnName{
                margin:10px 10px;
                cursor: pointer;
            }
            .ful li{
                font-weight: 700;
            }
            .clearfix::after{
                content:'';
                display:block;
                clear:both;
            }
            #menuBox div {
                border-bottom: 1px dotted #cccccc;
                cursor: pointer;
            }

        </style>
        <script>
            //加载所有的功能
            $(loadData());


            function loadData(){
                $.ajax({
                    //加载所有的功能，并予以展示
                    url:'funs.do',
                    type:'post',
                    data:{},
                    async:true,
                    success:function(result){
                        var fns = result.jsonObject;
                        showFns($('#menuBox'),fns);

                        $('#menuBox ul:gt(0) div').dblclick(function(i ,div){
                            $(this).next('ul').slideToggle(600);
                        });

                        $("#menuBox :checkbox").click(function(){
                            if(this.checked){
                                //当点击之后的状态时true
                                checkedChildren($(this));

                                //选子集
                                function checkedChildren($parent){
                                    var ul = $parent.parent().next();
                                    if(ul.length > 0){
                                        var inputs = ul.children().children().children('input');
                                        inputs.prop('checked',true);
                                        checkedChildren(inputs);
                                    }
                                }

                                checkParent($(this));
                                //选父级
                                function checkParent($child){
                                    var div = $child.parent().parent().parent().prev('div');
                                    if(div.length > 0){
                                        var input = div.children('input');
                                        input.prop('checked',true);
                                        checkParent(div);
                                    }
                                }

                            }else{
                                //当点击之后的状态时false
                                cancelChildren($(this));

                                //取消子集
                                function cancelChildren($parent){
                                    var ul = $parent.parent().next();
                                    if(ul.length > 0){
                                        var inputs = ul.children().children().children('input');
                                        inputs.prop('checked',false);
                                        cancelChildren(inputs);
                                    }
                                }

                                cancelParent($(this));
                                //取消父级
                                function cancelParent($child){
                                    var div = $child.parent().parent().parent().prev('div');
                                    if(div.length > 0){
                                        var checked = div.next().children().children().children('input:checked');
                                        if(checked.length > 0) return ;
                                        var input = div.children('input');
                                        input.prop('checked',false);
                                        cancelParent(input);
                                    }
                                }

                            }
                        });
                    },
                    dataType:'json',
                });
            }

            function showFns(position,fns){
                var ul = $('<ul class="clearfix"></ul>');
                position.append(ul);

                for(var i = 0;i<fns.length;i ++){
                    var li = $('<li>');
                    ul.append(li);
                    var div = $('<div class="clearfix"></div>');
                    div.appendTo(li);

                    var fn = fns[i];
                    var fKind = fn.flag==1?'菜单':'按钮';
                    div.append('<input type="checkbox" value="'+fn.fno+'"><span class="fnName">'+fn.fname+'</span>' +
                        '<span class="fntarget">'+fn.ftarget+'</span>' +
                        '<span class="fnFlag">'+fKind+'</span>');

                    if(fn.children) showFns(li,fn.children);
                }
            }

            //给保存按钮添加点击事件
            $(function(){
                $('#save').click(function(){
                    var fns = $('#menuBox input:checked');
                    var fnos = '';
                    for(var i = 0;i< fns.length;i++){
                        var fn = fns[i];
                        fnos += $(fn).val() + ',';
                    }

                    $.ajax({
                        url:'saveFnForRole.do',
                        type:'post',
                        data:{'rno':$('#hidden').val(),'fnos':fnos},
                        async:true,
                        success:function(result){
                            alert(result);
                        },
                    });
                });
            });


            //查询角色已经赋的功能
            $(function(){
                $.post('findLinkedFunction.do',{'rno':$('#hidden').val()},function(result){
                    var fnos = result.jsonObject;
                    console.log(fnos);
                    for (var i = 0; i < fnos.length; i++) {
                        var fno = fnos[i];
                        $('input[value='+fno+']').prop('checked',true);
                    }
                },'json')
            })

        </script>
    </head>
    <body>
        <input id="hidden" type="hidden" value="${param.rno}">
        <h2 align="center">为【${param.rname}】分配功能</h2>
        <button id="save">保存</button>
        <div id="menuBox">
            <ul class="ful clearfix">
                <li>
                    <div class="clearfix">
                        <span class="fnName">功能名称</span>
                        <span class="fntarget">功能请求</span>
                        <span class="fnFlag">功能类型</span>
                    </div>
                </li>
            </ul>
        </div>
    </body>
</html>
