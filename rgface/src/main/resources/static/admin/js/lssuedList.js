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
        elem: '#personnelList',
        method: 'post',
        url : '/api/admin/faceReview/getFaceReviewList',
        where : {type:0, startTime:0, endTime:0, searchParam:null, actionType:'lssued'},
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
            {field: 'face', title: '标准人脸', width:95, style: "height:81px;", templet:function(d){
                    return '<img class="faceImg" src="' + d.filePo.fileHost + '/face/' + d.filePo.fileName + '" height="81" width="60"/>';
            }},
            {field: 'truename', title: '真实姓名',minWidth:100,align:"center",templet:function(d){
                    return d.adminPersonnelPo.truename;
            }},
            {field: 'sex', title: '性别', minWidth:80,align:'center',templet:function (d) {
                    return d.adminPersonnelPo.sex === 0 ? '<i class="layui-icon" data-icon="&#xe661;">&#xe661;</i>女' : '<i class="layui-icon" data-icon="&#xe662;">&#xe662;</i>男';
            }},
            {field: 'mobile', title: '手机号码',minWidth:120,align:"center",templet:function(d){
                    return d.adminPersonnelPo.mobile;
            }},
            {field: 'icCard', title: '身份证号码',minWidth:180,align:'center',templet:function(d){
                    return d.adminPersonnelPo.icCard;
            }},
            {field: 'personnelGrade', title: '年级', align:'center', minWidth:80,templet:function(d){
                    return d.adminPersonnelPo.personnelGrade;
            }},
            {field: 'personnelClassCode', title: '班级', align:'center',minWidth:110,templet:function(d){
                    return d.adminPersonnelPo.personnelClassCode;
            }},
            {field: 'personnelCode', title: '学号', align:'center',minWidth:150,templet:function(d){
                    return d.adminPersonnelPo.personnelCode;
            }},
            {field: 'personnelType', title: '人员类型', align:'center', minWidth:100, templet:function(d){
                    if(d.adminPersonnelPo.personnelType === 0){
                        return "学生";
                    } else if(d.adminPersonnelPo.personnelType === 1){
                        return "教职工";
                    }
                    else if(d.adminPersonnelPo.personnelType === 2){
                        return "外来人员";
                    }
                    else {
                        return "其他";
                    }
            }},
            {field: 'studyType', title: '就读类型', align:'center',minWidth:90, templet:function (d) {
                    return d.adminPersonnelPo.studyType === 0 ? "住读生" : "走读生";

            }},
            // {field: 'passTag', title: '识别状态',  align:'center',templet:function(d){
            //         return d.adminPersonnelPo.passTag === 0 ? "允许识别" : "已被限制";
            // }},
            {field: 'passTag', title: '审核状态',  align:'center',templet:function(d) {

                    var status;
                    if (d.adminFaceReviewPo.lssuedStatus === 0) {
                        status = '<span class="layui-btn layui-btn-warm layui-btn-xs"> 未授权 </span>';
                    }
                    if (d.adminFaceReviewPo.lssuedStatus === 1) {
                        status = '<span class="layui-btn layui-btn-green layui-btn-xs"> 已授权 </span>';
                    }
                    if (d.adminFaceReviewPo.lssuedStatus === 2) {
                        status = '<span class="layui-btn layui-btn-danger layui-btn-xs"> 授权失败 </span>';
                    }

                    return status;
            }},
            {field: 'deleteTag', title: '人员状态',  align:'center',templet:function(d){
                    return d.adminPersonnelPo.personnelStart === 0 ? "正常使用" : "限制使用";
            }},
            {field: 'createTime', title: '创建时间', align:'center', minWidth:160, maxWidth:160, templet:function (d) {
                    return sh.TOOL.timeTool.timestampToTime(d.adminFaceReviewPo.createTime);
            }},
            {title: '操作', maxWidth:175, minWidth:155, style:'width:125px',fixed:"right",align:"center",style: "height:110px;", templet:function (d) {

                    var txTip = '授 权';

                    if (d.adminFaceReviewPo.lssuedStatus === 1) {
                        txTip = '撤销权限';
                    }

                    if (d.adminFaceReviewPo.lssuedStatus === 2) {
                        txTip = '重新权限';
                    }
                    
                    return '<div>' +
                        '<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="pass"> ' + txTip + ' </a>' +
                        '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="renew"> 重新审核 </a>' +
                        '</div>';
                }
            }
        ]],
        done: function(res, curr, count) {

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

        table.reload("personnelListTable", {
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
                tableIns.reload();
                layer.close(index);
            })
        }else{
            layer.msg("请选择需要删除的用户");
        }
    });

    //列表操作
    table.on('tool(personnelList)', function(obj){
        var layEvent = obj.event,
            tr = obj.tr,
            data = obj.data,
            faceReview = obj.data.adminFaceReviewPo,
            personnelInfo = obj.data.adminPersonnelPo;

        var _this, usableText, btnText, status, tagText;

        var personnelId = personnelInfo.personnelId;

        //pass
        if (layEvent === 'pass') {

            _this = $(this);

            if ($.trim(_this.text()) === '授 权') {

                review(null, 1, 1, null, null, null, data);
            }


            if ($.trim(_this.text()) === '撤销权限') {

                layer.confirm('是否撤销该人员权限',{
                    icon: 3,
                    title:'系统提示',
                    cancel : function(index){
                        layer.close(index);
                    }
                },function(index) {

                    layer.close(index);
                    var updateLoad = top.layer.msg('提交中...',{icon: 16,time:false,shade:0.8});

                    review(faceReview.id, 1, 3, personnelId, updateLoad, tr, _this);

                },function(index){

                    layer.close(index);
                });
            }

        }

        //renew
        else if (layEvent === 'renew') {
            _this = $(this);

            layer.confirm('是否退回重新审核资料',{
                icon: 3,
                title:'系统提示',
                cancel : function(index){
                    layer.close(index);
                }
            },function(index){

                layer.close(index);
                var updateLoad = top.layer.msg('提交中...',{icon: 16,time:false,shade:0.8});

                review(faceReview.id, 1, 2, personnelId, updateLoad, tr);

            },function(index){

                layer.close(index);
            });
        }
    });

    function review(id, type, status, personnelId, lay, tr, obj) {

        var param;

        //选择设备 - 授权
        if (type === 1 && status === 1) {

            selectDevice(obj);
        }

        //重新审核
        if (type === 1 && status === 2) {
            param = {type:type, status:status, personnelId:personnelId, faceReviewId:id};
        }

        //撤销授权
        if (type === 1 && status === 3) {
            param = {type:type, status:status, personnelId:personnelId, faceReviewId:id, deviceId:20180523};
        }

        if (status !== 1) {
            $.api(
                "lssuedFace",
                param,
                function (ret) {
                    layer.closeAll(lay);
                    layer.msg(ret.message);

                    if (ret.status === "SUCCESS") {

                        if (status === 1 || status === 3) {
                            var btnText = "撤销权限", text = '已授权';

                            if($.trim(obj.text()) === "撤销权限") {
                                btnText = "授 权", text = '未授权';
                            }

                            obj.text(btnText);

                            //更新状态字段
                            tr.children('td').eq(12).find('div').text(text);
                        }

                        if (status === 2) {
                            //移除
                            tr.empty()
                        }

                    }
                },
                "admin/faceReview"
            );
        }
    }

    function selectDevice(edit) {
        layui.layer.open({
            title : "选择设备",
            type : 2,
            area: ['1267px', '570px'],
            moveType: 0 , //拖拽模式，0或者1
            content : "faceDeviceList.html",
            success : function(layero, index){ //初始化数据

                var body = layui.layer.getChildFrame('body', index);

                if(edit){

                    body.find(".searchVal").attr("data-faceReviewId", edit.adminFaceReviewPo.id); //审核记录
                    body.find(".searchVal").attr("data-personnelId", edit.adminPersonnelPo.personnelId); //人员
                    body.find(".searchVal").attr("curr-page", currPage); //当前页

                    form.render();
                }

                //tip
                setTimeout(function(){
                    layui.layer.tips('点击此处关闭', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500)
            },cancel: function(){
                //右上角关闭回调
                updateTable(currPage)
                //return false 开启该代码可禁止点击该按钮关闭
            }

        });
    }
});
