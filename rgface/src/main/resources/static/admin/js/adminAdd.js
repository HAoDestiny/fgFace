layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.verify({
        pass: [/(.+){6,12}$/, '密码必须6到12位'],
        repass: function (value) {
            if (value !== $('.accountPassword').val()) {
                return "密码不一致, 请重新输入";
            }
        },
        length: [ /^[a-zA-Z0-9_-]{4,12}$/, '用户名必须4-12位(字母、数字、下划线、减号)']
    });

    var index;
    form.on("submit(addAccount)",function(data){
        //弹出loading
        index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});

        //checkPassword();

        // 提交信息
        // $.post("上传路径",{
        //     userName : $(".userName").val(),  //登录名
        //     userEmail : $(".userEmail").val(),  //邮箱
        //     userSex : data.field.sex,  //性别
        //     userGrade : data.field.userGrade,  //会员等级
        //     userStatus : data.field.userStatus,    //用户状态
        //     newsTime : submitTime,    //添加时间
        //     userDesc : $(".userDesc").text(),    //用户简介
        // },function(res){
        //
        // });

        var accountName = $(".accountName").val();
        var accountPasswordRePass = $(".accountPasswordRePass").val();
        var accountEmail = $(".accountEmail").val();
        var accountIntroduction = $(".accountIntroduction").val();
        var accountContacts = $(".accountContacts").val();
        var accountContactInformation = $(".accountContactInformation").val();
        var accountType = data.field.accountType;
        var accountNotes = $(".accountNotes").val();

        var accountRegData = {
            accountName:accountName,
            accountPasswordRePass:accountPasswordRePass,
            accountEmail:accountEmail,
            accountIntroduction:accountIntroduction,
            accountContacts:accountContacts,
            accountContactInformation:accountContactInformation,
            accountType:parseInt(accountType),
            accountNotes:accountNotes
        };

        $.api("addAccount", accountRegData, addAccountCallBack, "admin/account");

        // setTimeout(function(){
        //     top.layer.close(index);
        //     top.layer.msg("添加成功！");
        //     layer.closeAll("iframe");
        //     //刷新父页面
        //     parent.location.reload();
        // },2000);
        return false;
    });

    function addAccountCallBack(ret) {
        top.layer.close(index);
        top.layer.msg(ret.message);
        if (ret.status === "SUCCESS") {
            layer.closeAll("iframe");
            //刷新父页面
            parent.location.reload();
        }
    }

    //校验密码
    function checkPassword() {
        var password = $(".userPassword").val();
        var passwordM = $(".userPassword_m").val();

        if (password !== passwordM) {
            top.layer.msg("密码不一致,请重新输入!");
            return;
        }
    }

    //格式化时间
    function filterTime(val){
        if(val < 10){
            return "0" + val;
        }else{
            return val;
        }
    }

    //定时发布
    var time = new Date();
    var submitTime = time.getFullYear()+'-'+filterTime(time.getMonth()+1)+'-'+filterTime(time.getDate())+' '+filterTime(time.getHours())+':'+filterTime(time.getMinutes())+':'+filterTime(time.getSeconds());

});