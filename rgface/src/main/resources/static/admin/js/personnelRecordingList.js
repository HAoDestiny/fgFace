
layui.use(['form','layer','table','laytpl', 'laydate'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table,
        laydate = layui.laydate;

    //时间
    laydate.render({
        elem: '.searchTime',
        type: 'datetime',
        range: '~',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

    var currPage;

    var loadMsg = layer.msg('加载种，请稍候',{icon: 16,time:false,shade:0.8});

    var tableIns = table.render({
        elem: '#recordingList',
        method: 'post',
        url : '/api/admin/recording/getRecordingList',
        where : {type:0, startTime:0, endTime:0, searchParam:null},
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        id : "recordingListTable",
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
            // {field: 'id', title: 'ID',mxnWidth:60,align:"center",templet:function(d){
            //         return d.adminRecordingPo.recordingId;
            //     }},
            {field: 'truename', title: '真实姓名',minWidth:100,align:"center",templet:function(d){
                    return d.personnelPo.truename;
                }},
            // {field: 'sex', title: '性别', minWidth:80,align:'center',templet:function (d) {
            //         return d.personnelPo.sex === 0 ? '<i class="layui-icon" data-icon="&#xe661;">&#xe661;</i>女' : '<i class="layui-icon" data-icon="&#xe662;">&#xe662;</i>男';
            //     }},
            {field: 'mobile', title: '手机号码',maxWidth:121,align:"center",templet:function(d){
                    return d.personnelPo.mobile;
                }},
            {field: 'icCard', title: '身份证号码',minWidth:180,align:'center',templet:function(d){
                    return d.personnelPo.icCard;
                }},
            {field: 'personnelGrade', title: '年级', align:'center', maxWidth:96,templet:function(d){
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
            {field: 'deviceCode', title: '设备编号', align:'center', minWidth:108,templet:function(d){
                    return d.devicePo.deviceCode;
                }},
            // {field: 'deviceFirmwareVersion', title: '固件版本', align:'center', minWidth:80,templet:function(d){
            //         return d.devicePo.deviceFirmwareVersion;
            //     }},
            {field: 'createTime', title: '记录时间', align:'center', minWidth:160, maxWidth:160, templet:function (d) {
                    return sh.TOOL.timeTool.timestampToTime(d.adminRecordingPo.createTime);
                }},
            {field: 'face', title: '匹配人脸', width:95, style: "height:81px;", templet:function(d){
                    return '<img lay-event="imgShow" class="faceImg" src="' + d.facePo.fileHost + '/face/' + d.facePo.fileName + '" height="81" width="80"/>';
                }},
            {field: 'face', title: '实时环境', style: "height:81px;", templet:function(d){
                    return '<img lay-event="imgShow" class="environmentImg" src="' + d.environmentImgPo.fileHost + '/environment/' + d.environmentImgPo.fileName + '" height="80" width="150"/>';
                }}
            // {title: '操作', maxWidth:175, minWidth:155, style:'width:125px',fixed:"right",align:"center",style: "height:110px;", templet:function (d) {
            //
            //         var htmlDeleteTag, passTag;
            //
            //         d.personnelPo.personnelStart === 0 ? htmlDeleteTag = '禁用' : htmlDeleteTag = "启用";
            //         d.personnelPo.passTag === 0 ? passTag = '限制' : passTag = "允许";
            //
            //         return '<div>' +
            //             '<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>' +
            //             '<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="pass">' + passTag + '</a>' +
            //             '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="usable">' + htmlDeleteTag + '</a>' +
            //             '</div>';
            //     }
            // }
        ]],
        done: function(res, curr, count){

            $('.faceImg').each(function () {
                $(this).parent().removeClass("layui-table-cell");
                $(this).parent().attr("style", "text-align: center;");
            });

            $('.environmentImg').each(function () {
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

        table.reload("recordingListTable",{
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
    table.on('tool(recordingList)', function(obj){
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

        //显示大图
        if (layEvent === 'imgShow') {
            _this = $(this);
            console.log(_this.attr('src'));
            _this.imgZoom();
        }

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

    //时间String转时间戳
    function imgClick(obj) {
        alert($(obj).attr("src"));
    }
});
