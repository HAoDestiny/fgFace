layui.use(['form','layer','table','laytpl', 'laydate'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table,
        laydate = layui.laydate;

    var currPage;

    var loadMsg = layer.msg('数据加载中... ',{icon: 16,time:false,shade:0.8});

    //时间
    laydate.render({
        elem: '.searchTime',
        type: 'datetime',
        range: '~',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

    //用户列表
    var tableIns = table.render({
        elem: '#deviceList',
        method: 'post',
        url: '/api/admin/device/getDeviceList',
        where : {type: 0, startTime:0, endTime:0, searchParam:null}, //参数
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        id : "deviceListTable",
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
            {field: 'deviceName', title: '设备名称', minWidth:150, align:"center"},
            {field: 'deviceCode', title: '设备编号', minWidth:200, align:'center'},
            {field: 'dataServerIp', title: '数据服务IP', minWidth:125, align:'center'},
            {field: 'dataServerPort', title: '数据服务端口',minWidth:120, align:'center'},
            {field: 'configServerIp', title: '配置服务IP',minWidth:125, align:'center'},
            {field: 'configServerPort', title: '配置服务端口', minWidth:120, align:'center'},
            {field: 'deviceFirmwareVersion', title: '固件版本',width:101,minWidth:100, maxWidth:100, align:'center'},
            {field: 'createTime', title: '添加时间', align:'center', minWidth:160, maxWidth:160, templet:function (d) {
                    return sh.TOOL.timeTool.timestampToTime(d.createTime);
                }},
            {type: "deviceType", title: '设备类型',width:101,maxWidth:101,align:'center',templet:function (d) {
                   return d.deviceType === 1 ? '出':'进';
                }},
            {field: 'deleteTag', title: '设备状态',  align:'center', minWidth: 126, templet:function(d){

                    var status;

                    d.deleteTag === 0 ? status = "正常使用" : status = "限制使用";

                    d.deviceStatus === 0 ? status = status + "(离线)" : status = status + "(在线)";

                    return status;
                }},
            {field: 'deviceNote', title: '备注',  align:'center',templet:function(d){
                    return d.deviceNote === null || d.deviceNote === '' ? "暂无" : d.deviceNote;
                }},
            // {field: 'accountName', title: '操作员', align:'center',minWidth:110, templet:function (d) {
            //         return  d.accountInfo.accountName;
            //     }},
            {title: '操作', fixed:"right",align:"center"
            ,templet:function (d) {

                    var htmlDeleteTag;
                    d.deleteTag === 0 ? htmlDeleteTag = '禁用' : htmlDeleteTag = "启用";

                    return '<div><a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>' +
                        '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="usable">' + htmlDeleteTag + '</a>' +
                        '</div>';
                }
            }
            // {title: '操作', minWidth:195, style:'width:200px', templet:'#adminListBar',fixed:"right",align:"center"}
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

        table.reload("deviceListTable",{
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
    function addDevice(edit){
        var index = layui.layer.open({
            title : "添加设备",
            type : 2,
            content : "deviceAdd.html"
        });

        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    }

    //编辑用户
    function editDevice(edit){
        var index = layui.layer.open({
            title : "编辑设备",
            type : 2,
            area: ['1267px', '570px'],
            moveType: 0 , //拖拽模式，0或者1
            content : "deviceEdit.html",
            success : function(layero, index){ //初始化数据


                var body = layui.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];

                if(edit){

                    var dataServerIps = edit.dataServerIp.split('.');
                    var configServerIps = edit.configServerIp.split('.');

                    body.find(".deviceName").attr("curr-page", currPage); //当前页
                    body.find(".deviceId").val(edit.deviceId);
                    body.find(".deviceName").val(edit.deviceName);
                    body.find(".deviceCode").val(edit.deviceCode);

                    iframeWin.initContrast(edit.contrastStatus, edit.contrastLike); //执行deviceEdit.html js
                    iframeWin.initRepeat(edit.repeatStatus, edit.repeatTimeInterval); //执行deviceEdit.html js
                    iframeWin.initDeviceType(edit.deviceType); //执行deviceEdit.html js

                    body.find("#dataServerIp_0").val(dataServerIps[0]);
                    body.find("#dataServerIp_1").val(dataServerIps[1]);
                    body.find("#dataServerIp_2").val(dataServerIps[2]);
                    body.find("#dataServerIp_3").val(dataServerIps[3]);

                    body.find("#configServerIp_0").val(configServerIps[0]);
                    body.find("#configServerIp_1").val(configServerIps[1]);
                    body.find("#configServerIp_2").val(configServerIps[2]);
                    body.find("#configServerIp_3").val(configServerIps[3]);

                    body.find(".dataServerPort").val(edit.dataServerPort);
                    body.find(".configServerPort").val(edit.configServerPort);
                    body.find(".deviceFirmwareVersion").val(edit.deviceFirmwareVersion);
                    body.find(".deviceNotes").val(edit.deviceNote);

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
                location.reload();
                //return false 开启该代码可禁止点击该按钮关闭
            }

        });
    }

    //添加
    $(".addDevice_btn").click(function(){
        addDevice();
    });

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('deviceListTable'),
            data = checkStatus.data,
            newsId = [];
        if(data.length > 0) {
            for (var i in data) {
                newsId.push(data[i].newsId);
            }
            layer.confirm('确定删除选中的设备？', {icon: 3, title: '提示信息'}, function (index) {
                // $.get("删除文章接口",{
                //     newsId : newsId  //将需要删除的newsId作为参数传入
                // },function(data){
                tableIns.reload();
                layer.close(index);
                // })
            })
        }else{
            layer.msg("请选择需要删除的设备");
        }
    });

    //列表操作
    table.on('tool(deviceList)', function(obj){

        var layEvent = obj.event,
            data = obj.data,
            trData = obj.tr;//获得当前行 tr 的DOM对象

        var _this;
        var usableText,
            btnText,
            deleteTagText,
            type;

        var deviceId = data.deviceId;

        if(layEvent === 'edit'){ //编辑

            editDevice(data);

        } else if(layEvent === 'usable'){ //启用禁用

                _this = $(this);
                usableText = "是否确定启用此用户？";
                btnText = "禁用";
                deleteTagText = "正常使用";
                type = 0;

            if(_this.text() === "禁用") {
                usableText = "是否确定禁用此设备？";
                btnText = "启用";
                deleteTagText = "限制使用";
                type = 1;
            }

            layer.confirm(usableText,{
                icon: 3,
                title:'系统提示',
                cancel : function(index){
                    layer.close(index);
                }

            },function(index){

                $.api(
                    "updateDeviceStatus",
                    {type:type, id:deviceId},
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
                    "admin/device"
                );

            },function(index){
                layer.close(index);
            });

        }
    });
});
