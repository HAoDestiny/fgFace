layui.use(['form', 'layer', 'jquery'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        laypage = layui.laypage,
        $ = layui.jquery;

    var systemParameter;
    form.on("submit(systemParameter)", function (data) {

        var systemParamS = {
            cmsName: $(".cmsName").val(),
            homePage: $(".homePage").val(),
            version: $(".version").val(),
            author: $(".author").val(),
            server: $(".server").val(),
            dataBase: $(".dataBase").val(),
            maxUpload: $(".maxUpload").val(),
            keywords: $(".keywords").val(),
            powerby: $(".powerby").val(),
            description: $(".description").val(),
            record: $(".record").val()
        };

        var index = top.layer.msg('数据提交中...', {icon: 16, time: false, shade: 0.8});

        $.api("uploadSystemParam", systemParamS, function (ret) {
            layer.close(index);
            layer.msg(ret.message);
            if (ret.status === "SUCCESS") {
                fillData(ret.data);
            }

        }, "admin/system");

        return false;
    });


    //加载默认数据
    $.api("getSystemParams", {}, function (ret) {

        if (ret.status === "SUCCESS") {
            fillData(ret.data);
        }

    }, "admin/system");

    //填充数据方法
    function fillData(data) {

        $(".version").val(data.version);      //当前版本
        $(".author").val(data.author);        //开发作者
        $(".homePage").val(data.homePage);    //网站首页
        $(".server").val(data.server);        //服务器环境
        $(".dataBase").val(data.dataBase);    //数据库版本
        $(".maxUpload").val(data.maxUpload);  //最大上传限制
        $(".userRights").val(data.accountType);//当前用户权限
        $(".cmsName").val(data.cms);      //模版名称
        $(".description").val(data.description);//站点描述
        $(".powerby").val(data.powerby);      //版权信息
        $(".record").val(data.record);      //网站备案号
        $(".keywords").val(data.keywords);    //默认关键字

        // systemParameter = '{"cmsName":"' + data.cms + '",';  //模版名称
        // systemParameter += '"version":"' + data.version + '",';	 //当前版本
        // systemParameter += '"author":"' + data.author + '",'; //开发作者
        // systemParameter += '"homePage":"' + data.homePage + '",'; //网站首页
        // systemParameter += '"server":"' + data.server + '",'; //服务器环境
        // systemParameter += '"dataBase":"' + data.dataBase + '",'; //数据库版本
        // systemParameter += '"maxUpload":"' + data.maxUpload + '",'; //最大上传限制
        // systemParameter += '"userRights":"'+ data.accountType +'",'; //用户权限
        // systemParameter += '"description":"' + data.description + '",'; //站点描述
        // systemParameter += '"powerby":"' + data.powerby + '",'; //版权信息
        // systemParameter += '"record":"' + data.record + '",'; //网站备案号
        // systemParameter += '"keywords":"' + data.keywords + '"}'; //默认关键字

        window.sessionStorage.setItem("systemParameter", '{' +
            '"cmsName":"' + data.cms + '", "powerby" : "' + data.powerby +'", ' +
            '"record":"' + data.record + '"' +
            '}');
    }

});
