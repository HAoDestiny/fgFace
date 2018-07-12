layui.use(['form','layer','table','laytpl', 'laydate'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table,
        laydate = layui.laydate;

    var currPage;

    var loadMsg = layer.msg('加载种，请稍候',{icon: 16,time:false,shade:0.8});

    //时间
    laydate.render({
        elem: '.searchTime',
        type: 'datetime',
        range: '~',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

    //用户列表
    var tableIns = table.render({
        elem: '#userList',
        method: 'post',
        url : '/api/admin/account/getAdminAccountList',
        where : {accountType:0, type: 0, startTime:0, endTime:0, searchParam:null},
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        id : "userListTable",
        request: {
            pageName: 'pageCode' //当前页
            ,limitName: 'pageSize' //没有记录数
        },
        response: {
            statusName: 'status' //数据状态的字段名称，默认：code
            ,statusCode: 200 //成功的状态码，默认：0
            ,msgName: 'message' //状态信息的字段名称，默认：msg
            ,countName: 'pageTotal' //数据总数的字段名称，默认：count
            ,dataName: 'data' //数据列表的字段名称，默认：data
        },
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'accountName', title: '用户名', minWidth:150, align:"center"},
            {field: 'email', title: '用户邮箱', minWidth:200, align:'center',templet:function(d){
                    return '<a class="layui-blue" href="mailto:'+d.email+'">'+d.email+'</a>';
                }},
            // {field: 'accountType', title: '用户类型', minWidth:100, maxWidth:100, align:'center',templet:function(d){
            //         if(d.accountType === 1){
            //             return "普通管理员";
            //         }else if(d.accountType === 2){
            //             return "超级管理员";
            //         }
            //     }},
            {field: 'accountIntroduction', title: '简介', align:'center',minWidth:80},
            {field: 'accountContacts', title: '联系人', align:'center',minWidth:80},
            {field: 'accountContactInformation', title: '联系方式', align:'center',minWidth:150},
            {field: 'registerIp', title: '注册IP', align:'center', minWidth:120, maxWidth:120},
            {field: 'registerTime', title: '注册时间', align:'center',minWidth:180, templet:function (d) {
                    return sh.TOOL.timeTool.timestampToTime(d.registerTime);
                }},
            {field: 'lastLoginIp', title: '最后登录IP', align:'center', minWidth:120, maxWidth:120, templet:function (d) {
                    return d.lastLoginIp === null ? "尚未登录" : d.lastLoginIp;
                }},
            {field: 'lastLoginTime', title: '最后登录时间', align:'center',minWidth:180, templet:function (d) {
                    if (d.lastLoginTime === 0) {
                        return "尚未登录";
                    }
                    return sh.TOOL.timeTool.timestampToTime(d.lastLoginTime);
                }},
            {field: 'deleteTag', title: '用户状态',  align:'center',templet:function(d){
                    return d.deleteTag === 0 ? "正常使用" : "限制使用";
                }},
            {title: '操作', minWidth:175, style:'width:185px',fixed:"right",align:"center"
                ,templet:function (d) {

                    var htmlDeleteTag;

                    d.deleteTag === 0 ? htmlDeleteTag = '禁用' : htmlDeleteTag = "启用";

                    return '<div>' +
                        '<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>' +
                        '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="usable">' + htmlDeleteTag + '</a>' +
                        '</div>';
                }
            }
        ]],
        done: function(res, curr, count){
            //如果是异步请求数据方式，res即为你接口返回的信息。
            //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
            console.log(res);

            //得到当前页码
            console.log(curr);
            currPage = curr;

            //得到数据总量
            console.log(count);

            layer.close(loadMsg);
        }
    });

    //搜索
    $(".search_btn").on("click",function(){

        var type = 0;
        var startTime = 0;
        var endTime = 0;
        var searchVal = $(".searchVal").val();
        // var searchTime = $(".searchTime").val();

        if (searchVal !== ''){
            type = 1;
        }

        // if(searchTime !== ''){
        //
        //     type = 1;
        //
        //     var params = searchTime.split('~');
        //
        //     startTime = sh.TOOL.timeTool.stringDateFormatTimestamps(params[0]);
        //     endTime = sh.TOOL.timeTool.stringDateFormatTimestamps(params[1]);
        //
        //     if ((startTime - endTime) > 0) {
        //         startTime = endTime;
        //         endTime = startTime;
        //     }
        //
        //     console.log("开始:" + startTime + "结束:" + endTime);
        // }

        table.reload("userListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {//搜索的关键字
                accountType: 0,
                type: type,
                startTime: startTime,
                endTime: endTime,
                searchParam: searchVal
            }
        });
    });

    //添加用户
    function addUser(edit){
        var index = layui.layer.open({
            title : "添加用户",
            type : 2,
            content : "adminAdd.html",
            success : function(layero, index) {
                setTimeout(function(){
                    layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        });

        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    }

    $(".addAccount_btn").click(function(){
        addUser();
    });

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('userListTable'),
            data = checkStatus.data,
            newsId = [];
        if(data.length > 0) {
            for (var i in data) {
                newsId.push(data[i].newsId);
            }
            layer.confirm('确定删除选中的用户？', {icon: 3, title: '提示信息'}, function (index) {
                // $.get("删除文章接口",{
                //     newsId : newsId  //将需要删除的newsId作为参数传入
                // },function(data){
                tableIns.reload();
                layer.close(index);
                // })
            })
        }else{
            layer.msg("请选择需要删除的用户");
        }
    });

    //编辑用户
    function editUser(edit){
        var index = layui.layer.open({
            title : "编辑用户",
            type : 2,
            area: ['800px', '600px'],
            moveType: 0 , //拖拽模式，0或者1
            content : "adminEdit.html",
            success : function(layero, index){ //初始化数据

                var body = layui.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];

                if(edit){
                    body.find(".accountId").attr("curr-page", currPage);  //id
                    body.find(".accountId").val(edit.accountId);  //id
                    body.find(".accountName").val(edit.accountName);  //登录名
                    body.find(".accountEmail").val(edit.email);  //邮箱

                    //iframeWin.initAccountTypeList(edit.accountType); //执行adminEdit.html js

                    body.find(".accountIntroduction").val(edit.accountIntroduction);  //简介
                    body.find(".accountContacts").val(edit.accountContacts);  //联系人
                    body.find(".accountContactInformation").val(edit.accountContactInformation);  //联系方式
                    body.find(".accountNotes").val(edit.accountNotes);  //备注
                    form.render();
                }
                //tip
                setTimeout(function(){
                    layui.layer.tips('点击此处关闭', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500)
            }
        });
    }

    //列表操作
    table.on('tool(userList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        var _this,
            usableText,
            btnText,
            type,
            deleteTagText;

        var accountId = data.accountId;

        if(layEvent === 'edit'){ //编辑

            editUser(data);

        } else if (layEvent === 'usable'){ //启用禁用

            _this = $(this),
                usableText = "是否确定禁用此用户？",
                btnText = "禁用",
                type = 0,
                deleteTagText = "正常使用";

            if(_this.text() === "禁用"){
                usableText = "是否确定启用此用户？",
                btnText = "禁用",
                type = 1,
                deleteTagText = "已被限制";
            }

            layer.confirm(usableText,{
                icon: 3,
                title:'系统提示',
                cancel : function(index){
                    layer.close(index);
                }
            },function(index){

                $.api(
                    "setAdminAccountStartUpOrDisabled",
                    {type:type, id:accountId},
                    function (ret) {

                        layer.close(index);
                        layer.msg(ret.message);

                        if (ret.status === "SUCCESS") {
                            _this.text(btnText);

                            //更新状态字段
                            obj.update({
                                deleteTag : deleteTagText
                            });
                        }
                    },
                    "admin/account"
                );

            },function(index){

                layer.close(index);
            });

        } else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此用户？',{icon:3, title:'提示信息'},function(index){
                // $.get("删除文章接口",{
                //     newsId : data.newsId  //将需要删除的newsId作为参数传入
                // },function(data){
                    tableIns.reload();
                    layer.close(index);
                // })
            });
        }
    });

});
