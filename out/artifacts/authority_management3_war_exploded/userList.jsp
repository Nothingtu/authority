<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <style>
            table{
                border-collapse: collapse;
                text-align: center;
                margin: 7px auto;
                width:95%;
            }

            th,td{
                border: 1px solid rgba(0, 0, 0, 1);
            }

            th{
                margin:0  3px;
                width: 100px;
            }

            ul ,li{
                list-style-type: none;
            }

            .title{
                text-align: center;
                font-size: 35px;
            }

            .items{
                float:left;
                margin:0 5px;
            }
            .foot>ul>li{
                float:right;
                border:1px solid #000;
                margin:0 8px;
            }
            .foot{
                margin:0 20px;
            }

            .ls{
                margin-top:20px;
                margin-bottom:5px;
            }
            .clear::after{
                content:"";
                display:block;
                clear:both;
            }

            #choose{
                border-radius: 15px;
                width:400px;
                height: 260px;
                box-shadow: 6px 6px #cccccc;
                background: aquamarine;
                position:absolute;
                left:0;
                right:0;
                top:0;
                bottom:0;
                margin:auto auto;
                z-index: 2;
                display:none;
            }

            #background{
                width:100%;
                height: 100%;
                position:absolute;
                z-index:1;
                background: #000;
                opacity: 66%;
                left:0;
                right:0;
                top:0;
                bottom:0;
                display:none;
            }

            #choose form input{
                diaplay:block;
                margin:10px auto;
                magrgin-left:120px;
            }
            #closeBtn{
                width:22px;
                height: 22px;
                border: 1px solid black;
                background: lightcoral;
                border-radius: 50%;
                line-height: 22px;
                position:absolute;
                right:-8px;
                top:-20px;
            }


            #closeBtn:hover{
                cursor: pointer;
            }

            form{
                padding:50px 130px;
            }
            #choose form #template{
                margin:0 auto;
            }
        </style>
        <script>
            window.onload = function () {
                // ??????????????????????????????
                var sex = document.getElementById("r_usex").value;
                document.getElementById("sexSelectBtn").value= sex;


                var uno = document.getElementById("uno");
                uno.value = document.getElementById("r_uno").value;

                var uname = document.getElementById("uname");
                uname.value = document.getElementById("r_uname").value;

                var page = document.getElementById("pageSelectBtn");
                page.value = document.getElementById("r_page").value;

                var row = document.getElementById("rowSelectBtn");
                row.value = document.getElementById("r_row").value;


                //?????????????????????
                var selectBtn = document.getElementById("selectBtn");
                selectBtn.onclick = function(){
                    loadData();
                }

                var clearSelectBtn = document.getElementById("clearSelectBtn");
                clearSelectBtn.onclick = function(){
                    document.getElementById("sexSelectBtn").value='';
                    document.getElementById("uno").value='';
                    document.getElementById("uname").value='';
                    document.getElementById("pageSelectBtn").value='';
                    document.getElementById("sexSelectBtn").value='';
                    loadData();
                }

                var preBtn = document.getElementById("preBtn");
                preBtn.onclick = function(){
                    var page = document.getElementById("pageSelectBtn");
                    var value = parseInt(page.value);
                    if(value == 1) return;
                    page.value = value--;
                    loadData();
                }

                var nextBtn = document.getElementById("nextBtn");
                nextBtn.onclick = function(){
                    var page = document.getElementById("pageSelectBtn");
                    var value = parseInt(page.value);
                    var maxPage = document.getElementById("r_maxPage");
                    if(value == parseInt(maxPage.value)) return;
                    page.value = value++;
                    loadData();
                }


                var pageSelectBtn = document.getElementById("pageSelectBtn");
                pageSelectBtn.onchange = function(){
                    loadData();
                }

                var rowSelectBtn = document.getElementById("rowSelectBtn");
                rowSelectBtn.onchange = function(){
                    loadData();
                }

                var create = document.getElementById("createBtn");
                create.onclick = function () {
                    location.href = 'createUser.html';
                }


                //????????????id="checkBtn1"
                var checkBtn1 = document.getElementById("checkBtn1");
                checkBtn1.onclick = function () {
                    var checkBtns = document.getElementsByClassName("checkBtn");
                    for(var i = 0;i<checkBtns.length;i++){
                        checkBtns[i].checked=checkBtn1.checked;
                    }
                }

                //???????????????????????????????????????deletesBtn
                var deletesBtn = document.getElementById("deletesBtn");
                deletesBtn.onclick = function () {
                    var result='';
                    var checkBtns = document.getElementsByClassName("checkBtn");
                    for(var i = 0;i<checkBtns.length;i++){
                        if(checkBtns[i].checked) {
                            result += checkBtns[i].value + ',';
                        }
                    }

                    if(result == null) return;
                    var f = confirm("?????????????????????");
                    if(f == true) {
                        location.href='deletesUser.do?unos='+result;
                    }
                    return;
                }

                //????????????
                var addsBtn = document.getElementById("addsBtn");
                addsBtn.onclick = function () {
                    addsBtn.value = '';
                    var Btn1 = document.getElementById("background");
                    var Btn2 = document.getElementById("choose");
                    Btn1.style.display = 'block';
                    Btn2.style.display = 'block';
                }

                //??????x??????????????????????????????
                var closeBtn = document.getElementById("closeBtn");
                closeBtn.onclick = function () {
                    var Btn1 = document.getElementById("background");
                    var Btn2 = document.getElementById("choose");
                    Btn1.style.display = 'none';
                    Btn2.style.display = 'none';
                }

                //?????????????????????????????????template
                var userTemplateBtn = document.getElementById("template");
                userTemplateBtn.onclick = function () {
                    location.href = "userTemplateDownload.do?fileName=userTemplate.xlsx";
                }

                //???????????????????????????exportsBtn
                var exportsBtn = document.getElementById("exportsBtn");
                exportsBtn.onclick = function () {
                    location.href = "exportsUser.do"

                }


            }
    // =============================?????????????????????window.onload??????===============================================
            function loadData(){
                var rowSelectBtn = document.getElementById("rowSelectBtn");
                var pageSelectBtn = document.getElementById("pageSelectBtn");
                var sexSelectBtn = document.getElementById("sexSelectBtn");
                var uno = document.getElementById("uno");
                var uname = document.getElementById("uname");
                location.href='userList.do?uno='+uno.value+'&uname='+uname.value+
                    '&row='+rowSelectBtn.value+ '&page='+pageSelectBtn.value+'&usex='+sexSelectBtn.value;
            }

            //???????????????
            function deleteUser(uno){
                if(uno == null) return;
                var f = confirm("?????????????????????")
                if(f == true) location.href='deleteUser.do?uno='+uno;
                return;
            }

        </script>
    </head>
    <body>
        <%--    //?????????????????????????????????--%>
        <input type="hidden" id="r_uno" value="${sessionScope.uno}">
        <input type="hidden" id="r_uname" value="${sessionScope.uname}">
        <input type="hidden" id="r_usex" value="${sessionScope.usex}">
        <input type="hidden" id="r_page" value="${sessionScope.pageInfo.page}">
        <input type="hidden" id="r_row" value="${sessionScope.pageInfo.row}">
        <input type="hidden" id="r_maxPage" value="${sessionScope.pageInfo.maxPage}">

        <div class="title">????????????</div>
        <div >
            <div class="ls">
                <ul class="clear">
                    <li class="items"><input  id="uno" type="number" placeholder="???????????????" value="${sessionScope.uno}"></li>
                    <li class="items" ><input id="uname" placeholder="???????????????" value="${sessionScope.uname}"></li>
                    <li class="items">
                        ??????
                        <select id="sexSelectBtn">
                            <option value="">==</option>
                            <option>???</option>
                            <option>???</option>
                        </select>
                    </li>
                    <li class="items" id="selectBtn">
                        <button>??????</button>
                    </li>
                    <li class="items" id="clearSelectBtn">
                        <button>????????????</button>
                    </li>
                    <li class="items" id="createBtn">
                        <button>????????????</button>
                    </li>
                    <li class="items" id="addsBtn">
                        <button>????????????</button>
                    </li>
                    <li class="items" id="exportsBtn">
                        <button>????????????</button>
                    </li>
                    <li class="items" id="deletesBtn">
                        <button>????????????</button>
                    </li>
                </ul>
            </div>
            <table >
                <tr>
                    <th><input type="checkbox" id="checkBtn1"></th><th>????????????</th><th>????????????</th><th>????????????</th><th>????????????</th><th>????????????</th><th>????????????</th><th>??????</th>
                </tr>
                <tbody>
                    <c:choose>
                        <c:when test="${sessionScope.pageInfo.list.size()  == null}">
                            <tr><td text-align="center" colspan="8">???????????????????????????</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="user" items="${sessionScope.pageInfo.list}">
                                <tr>
                                    <td><input type="checkbox" class="checkBtn" value="${user.uno}"></td>
                                    <td>${user.uno}</td>
                                    <td>${user.uname}</td>
                                    <td>${user.urealname}</td>
                                    <td>${user.usex}</td>
                                    <td>${user.uage}</td>
                                    <td>${user.createtime}</td>
                                    <td>
                                        <a href="javascript:deleteUser(${user.uno})">??????</a> |
                                        <a href="selectOneUser.do?uno="+${user.uno}>??????</a>  |
                                        <a href="roles.jsp?uno=${user.uno}&uname=${user.uname}">????????????</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
            </div>
            <div class="foot">
                <ul class="clear">
                    <li id="nextBtn">?????????</li>
                    <li id="preBtn">?????????</li>
                    <li>
                        ??????
                        <select id="pageSelectBtn" >
                            <c:forEach begin="1" end="${sessionScope.pageInfo.maxPage}" varStatus="i" step="1">
                                <option>${i.index}</option>
                            </c:forEach>
                        </select>
                        ???
                    </li>
                    <li>
                        ??????
                        <select id="rowSelectBtn" >
                            <option>5</option>
                            <option>10</option>
                            <option>15</option>
                            <option>20</option>
                        </select>
                        ???
                    </li>

                </ul>
            </div>
        </div>
        <div id="choose">
            <pre id="closeBtn" > X</pre>
            <form action="addsUser.do" method="post" enctype="multipart/form-data" >
                <tr align="center"><td><input type="file" width="350px" name="excel" padding="50px"></td></tr>
                <tr align="center"><td><input type="button" id="template" value="????????????"></td></tr>
                <tr align="center"><td><input type="submit" id="cmt" value="??????"  width="12px" padding="50px"></td></tr>
            </form>
        </div>
        <div id="background"></div>


    </body>
</html>
