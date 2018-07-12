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
        where : {type:0, startTime:0, endTime:0, searchParam:null, actionType:'faceReview'},
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
                    if (d.adminFaceReviewPo.status === 0) {
                        status = '<span class="layui-btn layui-btn-warm layui-btn-xs"> 审核中 </span>';
                    }
                    if (d.adminFaceReviewPo.status === 1) {
                        status = '<span class="layui-btn layui-btn-green layui-btn-xs"> 审核通过 </span>';
                    }
                    if (d.adminFaceReviewPo.status === 2) {
                        status = '<span class="layui-btn layui-btn-danger layui-btn-xs"> 审核失败 </span>';
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
                    return '<div>' +
                        '<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="pass"> 通过 </a>' +
                        '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="refuse"> 拒绝 </a>' +
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

    //列表操作
    table.on('tool(personnelList)', function(obj){
        var layEvent = obj.event,
            tr = obj.tr,
            faceReview = obj.data.adminFaceReviewPo,
            personnelInfo = obj.data.adminPersonnelPo;

        var _this;

        var personnelId = personnelInfo.personnelId;

        //审核通过
        if (layEvent === 'pass') {
            _this = $(this);

            layer.confirm('是否通过资料审核',{
                icon: 3,
                title:'系统提示',
                cancel : function(index){
                    layer.close(index);
                }
            },function(index) {

                layer.close(index);
                var updateLoad = top.layer.msg('提交中...',{icon: 16,time:false,shade:0.8});

                // $.api(
                //     "faceReview",
                //     {type:1, status:1, faceReviewId:faceReview.id},
                //     function (ret) {
                //
                //         layer.closeAll(updateLoad);
                //         layer.msg(ret.message);
                //
                //         if (ret.status === "SUCCESS") {
                //
                //             //更新状态字段
                //             tr.children('td').eq(13).find('div').text(deleteTagText);
                //
                //         }
                //     },
                //     "admin/faceReview"
                // );

                review(faceReview.id, 0, 1, _this, updateLoad, tr);

            },function(index){

                layer.close(index);
            });

        }

        //审核拒绝
        else if (layEvent === 'refuse') {
            _this = $(this);

            layer.confirm('是否拒绝',{
                icon: 3,
                title:'系统提示',
                cancel : function(index){
                    layer.close(index);
                }
            },function(index){

                layer.close(index);
                var updateLoad = top.layer.msg('提交中...',{icon: 16,time:false,shade:0.8});
                // $.api(
                //     "faceReview",
                //     {type:type, status:2, id:personnelId},
                //     function (ret) {
                //
                //         layer.closeAll(updateLoad);
                //         layer.msg(ret.message);
                //
                //         if (ret.status === "SUCCESS") {
                //             _this.text(btnText);
                //
                //             //更新状态字段
                //             tr.children('td').eq(12).find('div').text(passTagText);
                //
                //         }
                //     },
                //     "admin/faceReview"
                // );

                review(faceReview.id, 0, 2, _this, updateLoad, tr);

            },function(index){

                layer.close(index);
            });
        }
    });

    function review(id, type, status, obj, lay, tr) {

        $.api(
            "faceReview",
            {type:type, status:status, faceReviewId:id},
            function (ret) {

                layer.closeAll(lay);
                layer.msg(ret.message);

                if (ret.status === "SUCCESS") {
                    //移除
                    tr.empty();

                }
            },
            "admin/faceReview"
        );

    }

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
