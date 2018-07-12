layui.use(['table', 'laydate'],function(){
	var table = layui.table,
        laydate = layui.laydate;


    var loadMsg = layer.msg('数据加载中... ',{icon: 16,time:false,shade:0.8});

    //时间
    laydate.render({
        elem: '.searchTimeInput',
        type: 'datetime',
        range: '~',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

	//系统日志
    table.render({
        elem: '#logs',
        method: 'post',
        url : '/api/admin/log/getRequestLogList',
        where : {type: 0, startTime:0, endTime:0, searchParam:null}, //参数
        cellMinWidth : 95,
        page : true,
        height : "full-20",
        limit : 20,
        limits : [10,15,20,25],
        id : "systemLogTable",
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
            {field: 'id', title: '序号', width:60, align:"center", templet:function (d) {
                    return d.adminRequestLogPo.id;
                }},
            {field: 'url', title: '请求地址', width:400, align:'center', templet:function (d) {
                   return d.adminRequestLogPo.url;
                }},
            {field: 'ip', title: '操作IP',  align:'center',minWidth:130, templet:function (d) {
                    return d.adminRequestLogPo.ip;
                }},
            {field: 'method', title: '操作方式', align:'center',templet:function(d){
                    if(d.adminRequestLogPo.method === "GET"){
                        return '<span class="layui-blue"> GET </span>'
                    }else{
                        return '<span class="layui-red"> POST </span>'
                    }
                }},
            {field: 'statusCode', title: '请求状态', align:'center',templet:function(d){
                    if(d.adminRequestLogPo.statusCode === 200){
                        return '<span class="layui-btn layui-btn-green layui-btn-xs"> 200 </span>'
                    }else{
                        return '<span class="layui-btn layui-btn-radius layui-btn-xs">' + d.adminRequestLogPo.method +'</span>'
                    }
                }},
            {field: 'timeConsuming', title: '耗时', align:'center',templet:function(d){
                return '<span class="layui-btn layui-btn-normal layui-btn-xs">' + (d.adminRequestLogPo.endTime - d.adminRequestLogPo.statTime) + '</span>'
            }},
            {field: 'actionStatus', title: '是否成功', align:'center',templet:function(d){
                if(d.adminRequestLogPo.actionStatus === 1){
                    return '<span class="layui-btn layui-btn-green layui-btn-xs"> 成功 </span>'
                } else{
                    return '<span class="layui-btn layui-btn-danger layui-btn-xs"> 失败 </span>'
                }
            }},
            {field: 'accountName',title: '操作人', minWidth:100, align:"center", templet:function (d) {
                    return d.personnelPo.accountName;
                }},
            {field: 'operatingTime', title: '操作时间', align:'center', width:170, templet:function (d) {
                    return sh.TOOL.timeTool.timestampToTime(d.adminRequestLogPo.createTime);
                }}

        ]],
        done: function(res, curr, count) {

            layer.close(loadMsg);
        }
    });


    //搜索
    $(".search_btn").on("click",function(){

        var type = 0;
        var startTime = 0;
        var endTime = 0;
        var searchVal = $(".searchVal").val();
        var searchTime = $(".searchTimeInput").val();

        if (searchVal !== ''){
            type = 1;

            if (searchVal === '成功') {
                searchVal = 1;
            }

            if (searchVal === '失败') {
                searchVal = 0;
            }
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

        table.reload("systemLogTable",{
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
});
