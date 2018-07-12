layui.use(['form','layer','table','laytpl', 'upload', 'laydate'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        upload = layui.upload,
        laytpl = layui.laytpl,
        table = layui.table,
        laydate = layui.laydate;

    var currPage;

    var loadMsg = layer.msg('加载中，请稍候',{icon: 16,time:false,shade:0.8});

    //时间
    laydate.render({
        elem: '.searchTime',
        type: 'datetime',
        range: '~',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

    //用户列表
    var tableIns = table.render({
        elem: '#personnelList',
        method: 'post',
        url : '/api/admin/personnel/getPersonnelList',
        where : {type:0, startTime:0, endTime:0, searchParam:null},
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        id : "personnelListTable",
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
            {type: "checkbox", fixed:"left", width:50, style: "height:110px;"},
            {field: 'file', title: '标准头像', width:95, style: "height:81px;", templet:function(d){
                    return '<img class="fileImg" src="' + d.ptoPo.file.fileHost + '/file/' + d.ptoPo.file.fileName + '" height="81" width="60"/>';
            }},
            {field: 'face', title: '标准人脸', width:95, style: "height:81px;", templet:function(d){
                    return '<img class="faceImg" src="' + d.ptoPo.face.fileHost + '/face/' + d.ptoPo.face.fileName + '" height="81" width="60"/>';
            }},
            {field: 'truename', title: '真实姓名',minWidth:100,align:"center",templet:function(d){
                    return d.personnelPo.truename;
            }},
            {field: 'sex', title: '性别', minWidth:80,align:'center',templet:function (d) {
                    return d.personnelPo.sex === 0 ? '<i class="layui-icon" data-icon="&#xe661;">&#xe661;</i>女' : '<i class="layui-icon" data-icon="&#xe662;">&#xe662;</i>男';
            }},
            {field: 'mobile', title: '手机号码',minWidth:120,align:"center",templet:function(d){
                    return d.personnelPo.mobile;
            }},
            {field: 'icCard', title: '身份证号码',minWidth:180,align:'center',templet:function(d){
                    return d.personnelPo.icCard;
            }},
            {field: 'personnelGrade', title: '年级', align:'center', minWidth:80,templet:function(d){
                    return d.personnelPo.personnelGrade;
            }},
            {field: 'personnelClassCode', title: '班级', align:'center',minWidth:110,templet:function(d){
                    return d.personnelPo.personnelClassCode;
            }},
            {field: 'personnelCode', title: '学号', align:'center',minWidth:150,templet:function(d){
                    return d.personnelPo.personnelCode;
            }},
            {field: 'personnelType', title: '人员类型', align:'center', minWidth:100, templet:function(d){
                    if(d.personnelPo.personnelType === 0){
                        return "学生";
                    } else if(d.personnelPo.personnelType === 1){
                        return "教职工";
                    }
                    else if(d.personnelPo.personnelType === 2){
                        return "外来人员";
                    }
                    else {
                        return "其他";
                    }
            }},
            {field: 'studyType', title: '就读类型', align:'center',minWidth:90, templet:function (d) {
                    return d.personnelPo.studyType === 0 ? "住读生" : "走读生";

            }},
            {field: 'passTag', title: '识别状态',  align:'center',templet:function(d){
                    return d.personnelPo.passTag === 0 ? "允许识别" : "已被限制";
            }},
            {field: 'deleteTag', title: '人员状态',  align:'center',templet:function(d){
                    return d.personnelPo.personnelStart === 0 ? "正常使用" : "限制使用";
            }},
            {field: 'createTime', title: '添加时间', align:'center', minWidth:160, maxWidth:160, templet:function (d) {
                    return sh.TOOL.timeTool.timestampToTime(d.personnelPo.createTime);
                }},
            // {field: 'personnelByAccountName', title: '所属账号(单位)',minWidth:94,align:"center",templet:function(d){
            //         return d.personnelPo.personnelByAccountName;
            // }},
            {title: '操作', maxWidth:175, minWidth:155, style:'width:125px',fixed:"right",align:"center",style: "height:110px;", templet:function (d) {

                    var htmlDeleteTag, passTag;

                    d.personnelPo.personnelStart === 0 ? htmlDeleteTag = '禁用' : htmlDeleteTag = "启用";
                    d.personnelPo.passTag === 0 ? passTag = '限制' : passTag = "允许";

                    return '<div>' +
                        '<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>' +
                        '<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="pass">' + passTag + '</a>' +
                        '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="usable">' + htmlDeleteTag + '</a>' +
                        '</div>';
                }
            }
        ]],
        done: function(res, curr, count){

            //去除图片表格样式
            // $("#fileImg").parent().removeClass("layui-table-cell");
            // $("#faceImg").parent().removeClass("layui-table-cell");
            // $("#fileImg").parent().attr("style", "text-align: center;");
            // $("#faceImg").parent().attr("style", "text-align: center;");

            $('.fileImg').each(function () {
                $(this).parent().removeClass("layui-table-cell");
                $(this).parent().attr("style", "text-align: center;");
            });

            $('.faceImg').each(function () {
                $(this).parent().removeClass("layui-table-cell");
                $(this).parent().attr("style", "text-align: center;");
            });

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

    $('#fileImg').each(function () {
        $(this).parent().removeClass("layui-table-cell");
        $(this).parent().attr("style", "text-align: center;");
    });

    $('#faceImg').each(function () {
        $(this).parent().removeClass("layui-table-cell");
        $(this).parent().attr("style", "text-align: center;");
    });

    //搜索
    $(".search_btn").on("click",function(){

        var type = 0;
        var startTime = 0;
        var endTime = 0;
        var searchVal = $(".searchVal").val();
        var searchTime = $(".searchTime").val();

        if (searchVal !== ''){
            type = 1;
        }

        if(searchTime !== ''){

            type = 1;

            var params = searchTime.split('~');

            startTime = sh.TOOL.timeTool.stringDateFormatTimestamps(params[0]);
            endTime = sh.TOOL.timeTool.stringDateFormatTimestamps(params[1]);

            if ((startTime - endTime) > 0) {
                startTime = endTime;
                endTime = startTime;
            }

            console.log("开始:" + startTime + "结束:" + endTime);
        }

        table.reload("personnelListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {//搜索的关键字
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
            content : "personnelAdd.html",
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

    //批量禁用
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

    //导出
    $('.exportPersonnelXLS').click(function () {

        layer.confirm('是否导出？',{icon: 3, title: '提示信息'}, function(index) {

            $.api("getExcel", {excelType: 0}, function (ret) {

                if (ret.status === "SUCCESS") {
                    window.location.href = ret.data + "?t=" + Math.random();
                }

                if (ret.status === "ERROR") {
                    layer.msg(ret.message);
                }

            }, "admin/excel");

            layer.close(index);
        });
    });

    //导入
    upload.render({
        elem: '.importPersonnelXLS',
        url: '/api/admin/excel/importPersonnelExcel',
        method : 'post',
        accept: 'file',
        exts: 'xlsx|xls',
        size: 0,
        drag: false,
        auto: true, //自动上传
        //bindAction: '#uploadAvatarBtn', //提交
        data: {
            type: 1 //personnel
        },
        choose: function(obj) {

            // obj.preview(function(index, file, result){
            //     $('#userFace').attr('src', result); //预览
            // });
        },
        before: function (obj) {
            layer.msg('数据提交中...',{icon: 16,time:false,shade:0.8});
        },
        done: function(res, index, upload){

            //console.log(res);
            top.layer.closeAll();

            if (res === null) {
                return;
            }

            top.layer.close(index);
            top.layer.msg(res.message);

            if (res.status === "SUCCESS") {

                var errorList = '';
                if (res.data.length > 0) {
                    $.each(res.data, function (index, item) {

                        errorList += item + ", ";
                    });

                    layer.open({
                        title: '系统提示'
                        ,content: '部分操作失败：id = [ ' + errorList + ']'
                    });

                    layer.confirm('操作失败：id = [ ' + errorList + ']', {icon: 3, title: '系统提示',
                        btn: ['确定', '取消']
                    }, function(index, layero){

                        $(".layui-laypage-btn").click(); //刷新表格当前页
                    }, function(index){

                        $(".layui-laypage-btn").click(); //刷新表格当前页
                    });
                }
            }
        },
        error: function (index, upload) {
            layer.close(loadMsg);
            top.layer.msg("上传失败")
        }
    });

    //编辑用户
    function editUser(edit, ptoPo){
        var index = layui.layer.open({
            title : "编辑信息",
            type : 2,
            area: ['80%', '80%'],
            moveType: 0 , //拖拽模式，0或者1
            content : "personnelEdit.html",
            success : function(layero, index){ //初始化数据

                var body = layui.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];

                if(edit){
                    body.find(".fileId").attr("personnel-id", edit.personnelId);  //pageCode
                    body.find(".fileId").attr("file-id", ptoPo.fileId);  //file
                    body.find(".fileId").attr("face-id", ptoPo.faceId);  //face
                    body.find(".fileId").attr("curr-page", currPage);  //pageCode
                    body.find("#userFace").attr("src", ptoPo.file.fileHost + "/file/" + ptoPo.file.fileName);
                    body.find(".userFace").attr("src", ptoPo.face.fileHost + "/face/" + ptoPo.face.fileName);
                    body.find(".personnelName").val(edit.truename);  //id
                    body.find(".icCard").val(edit.icCard);  //登录名
                    body.find(".mobile").val(edit.mobile);  //邮箱

                    body.find(".password").val(edit.password);  //简介
                    body.find(".personnelClassCode").val(edit.personnelClassCode);  //联系人
                    body.find(".personnelCode").val(edit.personnelCode);  //联系方式
                    body.find(".personnelGrade").val(edit.personnelGrade);  //备注
                    body.find(".personnelByAccountName").val(edit.personnelByAccountName);  //简介

                    iframeWin.initPersonnelSex(edit.sex); //执行personnelEdit.html js
                    iframeWin.initPersonnelType(edit.personnelType); //执行personnelEdit.html js
                    iframeWin.initStudyType(edit.studyType); //执行personnelEdit.html js
                    iframeWin.initPersonnelStatus(edit.personnelStart); //执行personnelEdit.html js
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
    table.on('tool(personnelList)', function(obj){
        var layEvent = obj.event,
            tr = obj.tr,
            data = obj.data.personnelPo,
            ptoPo = obj.data.ptoPo;

        var _this,
            usableText,
            btnText,
            type,
            passTagText,
            deleteTagText;

        var personnelId = data.personnelId;

        //编辑
        if(layEvent === 'edit'){

            editUser(data, ptoPo);

        }

        //启用禁用
        else if (layEvent === 'usable'){
            _this = $(this),
                usableText = "是否确定启用此人员？",
                btnText = "禁用",
                type = 0,
                deleteTagText = "正常使用";

            if(_this.text() === "禁用"){
                usableText = "是否确定禁用此人员？",
                btnText = "启用",
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

                layer.close(index);
                var updateLoad = top.layer.msg('修改中...',{icon: 16,time:false,shade:0.8});

                $.api(
                    "updatePersonnelStatus",
                    {type:type, id:personnelId},
                    function (ret) {

                        layer.closeAll(updateLoad);
                        layer.msg(ret.message);

                        if (ret.status === "SUCCESS") {
                            _this.text(btnText);

                            //更新状态字段
                            tr.children('td').eq(13).find('div').text(deleteTagText);

                        }
                    },
                    "admin/personnel"
                );

            },function(index){

                layer.close(index);
            });

        }

        //人脸识别状态
        else if (layEvent === 'pass') {
            _this = $(this),
                usableText = "是否确定允许此人员进行人脸识别？",
                btnText = "限制",
                type = 0,
                passTagText = "允许识别";

            if(_this.text() === "限制"){
                usableText = "是否确定限制此人员进行人脸识别？",
                    btnText = "允许",
                    type = 1,
                    passTagText = "已被限制";
            }

            layer.confirm(usableText,{
                icon: 3,
                title:'系统提示',
                cancel : function(index){
                    layer.close(index);
                }
            },function(index){

                layer.close(index);
                var updateLoad = top.layer.msg('修改中...',{icon: 16,time:false,shade:0.8});
                $.api(
                    "updatePersonnelFaceRecognition",
                    {type:type, id:personnelId},
                    function (ret) {

                        layer.closeAll(updateLoad);
                        layer.msg(ret.message);

                        if (ret.status === "SUCCESS") {
                            _this.text(btnText);

                            //更新状态字段
                            tr.children('td').eq(12).find('div').text(passTagText);

                        }
                    },
                    "admin/personnel"
                );

            },function(index){

                layer.close(index);
            });
        }
    });

    // function timestampToTime(timestamp) {
    //     var date = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    //     var Y = date.getFullYear() + '-';
    //     var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    //     var D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ';
    //     var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    //     var m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    //     var s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
    //     return Y + M + D + h + m + s;
    // }

});
