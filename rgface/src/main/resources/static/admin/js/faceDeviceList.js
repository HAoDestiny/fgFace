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

    //设备列表
    var tableIns = table.render({
        elem: '#faceDeviceList',
        method: 'post',
        url: '/api/admin/device/getDeviceList',
        where : {type: 0, startTime:0, endTime:0, searchParam:null}, //参数
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        id : "faceDeviceListTable",
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
            {field: 'deviceFirmwareVersion', title: '固件版本',width:101,minWidth:100, maxWidth:100, align:'center'},

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
            {title: '操作',fixed:"right",align:"center", templet:function (d) {

                    return '<div>' +
                        '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="add"> 选 择 </a>' +
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

        table.reload("faceDeviceListTable",{
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
    table.on('tool(faceDeviceList)', function(obj){
        var layEvent = obj.event,
            tr = obj.tr,
            data = obj.data;

        var _this;

        var deviceId = data.deviceCode;

        //pass
        if (layEvent === 'add') {

            _this = $(this);

            layer.confirm('是否确定授权到 ' + deviceId + ' 设备',{
                icon: 3,
                title:'系统提示',
                cancel : function(index){
                    layer.close(index);
                }
            },function(index) {

                var personnelId = $('.searchVal').attr('data-personnelId');
                var faceReviewId = $('.searchVal').attr('data-faceReviewId');

                layer.close(index);
                var updateLoad = top.layer.msg('提交中...',{icon: 16,time:false,shade:0.8});
                var param = {type:1, status:1, personnelId:personnelId, faceReviewId:faceReviewId, deviceId:deviceId};

                $.api(
                    "lssuedFace",
                    param,
                    function (ret) {
                        layer.closeAll();
                        layer.msg(ret.message);

                        if (ret.status === "SUCCESS") {
                            parent.layer.closeAll("iframe");

                            //刷新父页面
                            parent.updateTable($('.searchVal').attr("curr-page"));
                        }
                    },
                    "admin/faceReview"
                );

            },function(index){

                layer.close(index);
            });

        }
    });
});
