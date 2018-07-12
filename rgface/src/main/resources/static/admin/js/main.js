//获取系统时间
var newDate = '';
getLangDate();

//值小于10时，在前面补0
function dateFilter(date) {
    if (date < 10) {
        return "0" + date;
    }
    return date;
}

function getLangDate() {
    var dateObj = new Date(); //表示当前系统时间的Date对象
    var year = dateObj.getFullYear(); //当前系统时间的完整年份值
    var month = dateObj.getMonth() + 1; //当前系统时间的月份值
    var date = dateObj.getDate(); //当前系统时间的月份中的日
    var day = dateObj.getDay(); //当前系统时间中的星期值
    var weeks = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
    var week = weeks[day]; //根据星期值，从数组中获取对应的星期字符串
    var hour = dateObj.getHours(); //当前系统时间的小时值
    var minute = dateObj.getMinutes(); //当前系统时间的分钟值
    var second = dateObj.getSeconds(); //当前系统时间的秒钟值
    var timeValue = "" + ((hour >= 12) ? (hour >= 18) ? "晚上" : "下午" : "上午"); //当前时间属于上午、晚上还是下午
    newDate = dateFilter(year) + "年" + dateFilter(month) + "月" + dateFilter(date) + "日 " + " " + dateFilter(hour) + ":" + dateFilter(minute) + ":" + dateFilter(second);
    document.getElementById("nowTime").innerHTML = timeValue + "好! 当前时间为： " + newDate + "　" + week;
    setTimeout("getLangDate()", 1000);
}

layui.use(['form', 'element', 'layer', 'jquery'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        element = layui.element;
    $ = layui.jquery;
    //上次登录时间【此处应该从接口获取，实际使用中请自行更换】
    $(".loginTime").html(newDate.split("日")[0] + "日</br>" + newDate.split("日")[1]);
    //icon动画
    $(".panel a").hover(function () {
        $(this).find(".layui-anim").addClass("layui-anim-scaleSpring");
    }, function () {
        $(this).find(".layui-anim").removeClass("layui-anim-scaleSpring");
    })
    $(".panel a").click(function () {
        parent.addTab($(this));
    })

    //系统基本参数
    $.api("getSystemTotalOrMsg", {}, getSystemTotalOrMsgCallBack, "admin/system");

    //填充数据方法
    function fillParameter(data) {
        var systemTotal = data.systemTotal;
        var systemMessage = data.systemMessage;

        $(".version").text(nullData(systemMessage.version));      //当前版本
        $(".author").text(nullData(systemMessage.author));        //开发作者
        $(".homePage").text(nullData(systemMessage.homePage));    //网站首页
        $(".server").text(nullData(systemMessage.server));        //服务器环境
        $(".dataBase").text(nullData(systemMessage.dataBase));    //数据库版本
        $(".maxUpload").text(nullData(systemMessage.maxUpload));    //最大上传限制
        $(".userRights").text(nullData(systemMessage.accountType)); //当前用户权限

        $(".adminAccountTotal span").text(systemTotal.adminAccountCount);
        $(".personnelTotal span").text(systemTotal.personnelCount);
        $(".deviceTotal span").text(systemTotal.deviceCount);
        $(".userTotal span").text(systemTotal.userCount);
        $(".recordingTotal span").text(systemTotal.recordingCount);
    }

    function getSystemTotalOrMsgCallBack(ret) {

        if (ret.status === "SUCCESS") {

            var systemTotal = ret.data.systemTotal;
            var systemMessage = ret.data.systemMessage;

            fillParameter(ret.data);

            //window.sessionStorage.setItem("systemParameter", JSON.stringify(ret.data));
        }
    }

    function nullData(data) {

        if (data == '' || data == "undefined") {

            return "未定义";
        } else {

            return data;
        }
    }

    //最新识别列表
    $.api("getNewestRecordingList", {pageSize: 10}, function (ret) {

        var hotNewsHtml = '';

        if (ret.status === "SUCCESS") {

            ret.data.forEach(function (item, index) {
                hotNewsHtml += '<tr>'
                    + '<td style="text-align: center;"><a href="javascript:;"> ' + item.personnelPo.truename + '</a></td>'
                    + '<td style="text-align: center;"><a href="javascript:;"> ' + item.personnelPo.personnelGrade + '</a></td>'
                    + '<td style="text-align: center;"><a href="javascript:;"> ' + item.personnelPo.personnelClassCode + '</a></td>'
                    + '<td style="text-align: center;"><a href="javascript:;"> ' + item.personnelPo.personnelCode + '</a></td>'
                    + '<td style="text-align: center;"><a href="javascript:;"> ' + personnelType(item.personnelPo.personnelType) + '</a></td>'
                    + '<td style="text-align: center;"><a href="javascript:;"> ' + item.devicePo.deviceCode + '</a></td>'
                    + '<td style="text-align: center;">' + sh.TOOL.timeTool.timestampToTime(item.adminRecordingPo.createTime) + '</td>'
                    + '</tr>';

            });
            $(".hot_news").html(hotNewsHtml);
            // $(".userAll span").text(data.length);
        }
    }, "admin/recording");

    //最新操作日志
    $.api("getNewestRequestLogList", {}, function (ret) {

        var newsHtml = '';

        if (ret.status === "SUCCESS") {

            ret.data.forEach(function (item, index) {
                newsHtml += '<tr>'
                    + '<td style="text-align: center;"><a href="javascript:;"> ' + item.ip + '</a></td>'
                    + '<td style="text-align: center;"><a href="javascript:;"> ' + item.url + '</a></td>'
                    + '<td style="text-align: center;">' + requestMethod(item.method) + '</td>'
                    + '<td style="text-align: center;">' + requestStatusCode(item.actionStatus) + '</td>'
                    + '<td style="text-align: center;">' + sh.TOOL.timeTool.timestampToTime(item.createTime) + '</td>'
                    + '</tr>';
            });
            $(".new_log").html(newsHtml);

        }
    }, "admin/log");

    function personnelType(type) {

        if (type === 0) {
            return '学生';
        }

        if (type === 1) {
            return '教职工';
        }

        if (type === 2) {
            return '外来人员';
        }

        if (type === 3) {
            return '其他';
        }

    }

    function requestMethod(data) {
        if(data === "GET"){
            return '<span class="layui-blue"> GET </span>'
        }else{
            return '<span class="layui-red"> POST </span>'
        }
    }

    function requestStatusCode(data) {
        if(data === 1){
            return '<span class="layui-btn layui-btn-green layui-btn-xs"> 成功 </span>'
        }else{
            return '<span class="layui-btn layui-btn-danger layui-btn-xs">失败</span>'
        }
    }

    //用户数量
    // $.get("static/admin/json/userList.json",function(data){
    //     $(".userAll span").text(data.count);
    // });

    //外部图标
    // $.get(iconUrl,function(data){
    //     $(".outIcons span").text(data.split(".icon-").length-1);
    // })

});
