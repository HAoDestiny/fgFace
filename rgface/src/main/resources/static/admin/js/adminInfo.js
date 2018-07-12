var form, $,areaData;
layui.config({
    base : "static/admin/js/"
}).extend({
    "address" : "address"
})
layui.use(['form','layer','upload','laydate',"address"],function(){
    form = layui.form;
    $ = layui.jquery;
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        laydate = layui.laydate,
        address = layui.address;

    var loadMsg = layer.msg('加载种，请稍候',{icon: 16,time:false,shade:0.8});

    //上传头像
    upload.render({
        elem: '.userFaceBtn',
        url: 'static/admin/json/userface.json',
        method : "get",  //此处是为了演示之用，实际使用中请将此删除，默认用post方式提交
        done: function(res, index, upload){
            layer.close(loadMsg);
            var num = parseInt(4*Math.random());  //生成0-4的随机数，随机显示一个头像信息
            $('#userFace').attr('src',res.data[num].src);
            window.sessionStorage.setItem('userFace',res.data[num].src);
        }
    });

    //添加验证规则
    // form.verify({
    //     userBirthday : function(value){
    //         if(!/^(\d{4})[\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|1[0-2])([\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/.test(value)){
    //             return "出生日期格式不正确！";
    //         }
    //     }
    // })
    //选择出生日期
    // laydate.render({
    //     elem: '.userBirthday',
    //     format: 'yyyy年MM月dd日',
    //     trigger: 'click',
    //     max : 0,
    //     mark : {"0-12-15":"生日"},
    //     done: function(value, date){
    //         if(date.month === 12 && date.date === 15){ //点击每年12月15日，弹出提示语
    //             layer.msg('今天是马哥的生日，也是layuicms2.0的发布日，快来送上祝福吧！');
    //         }
    //     }
    // });

    //获取省信息
    //address.provinces();

    //初始化个人信息
    if (window.sessionStorage.getItem("adminAccountInfo")) {
        layer.close(loadMsg);
        updateInfo(JSON.parse(window.sessionStorage.getItem("adminAccountInfo")))
    } else {
        $.api("getAdminAccountInfo", {}, initAdminInfo, "admin/account");
    }

    function initAdminInfo(ret) {

        layer.close(loadMsg);

        if (ret.status === "SUCCESS") {
            updateInfo(ret.data);
        }
    }

    function updateInfo(data) {
        $(".accountName").val(data.accountName);
        $(".accountType").val(data.accountType === 2 ? "超级管理员":"普通管理员");
        $(".accountEmail").val(data.accountEmail);
        $(".accountNotes").val(data.accountNotes);
        $(".accountContacts").val(data.accountContacts);
        $(".accountIntroduction").val(data.accountIntroduction);
        $(".accountContactInformation").val(data.accountContactInformation);
    }

    //提交个人资料
    form.on("submit(updateAdminInfo)",function(data){

        var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});

        //将填写的用户信息存到session以便下次调取
        var key, adminAccountInfoParam;

        adminAccountInfoParam = {
            'accountId' : window.localStorage.getItem("accountId"),
            'accountName' : $(".accountName").val(),
            'accountEmail' : $(".accountEmail").val(),
            'accountType' : $('.accountType').val(),
            'accountNotes' : $(".accountNotes").val(),
            'accountContacts' :  $(".accountContacts").val(),
            'accountContactInformation' :  $(".accountContactInformation").val(),
            'accountIntroduction' :  $(".accountIntroduction").val()
        };

        $.api("updateAdminAccountInfo", adminAccountInfoParam, function (ret) {

            layer.close(index);
            layer.msg(ret.message);

        }, "admin/account");
        window.sessionStorage.removeItem("adminAccountInfo");
        window.sessionStorage.setItem("adminAccountInfo",JSON.stringify(adminAccountInfoParam));

        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //修改密码
    form.on("submit(changePwd)",function(data){
        var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
        setTimeout(function(){
            layer.close(index);
            layer.msg("密码修改成功！");
            $(".pwd").val('');
        },2000);
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    })
});