layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.verify({
        pass: [/(.+){6,12}$/, '密码必须6到12位'],
        length: [ /^[a-zA-Z0-9_-]{4,12}$/, '用户名必须4-12位(字母、数字、下划线、减号)']
    });

    var index;
    var accountUpdateData;
    form.on("submit(updateAdminAccountInfo)",function(data){
        //弹出loading
        index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});

        var accountId = $(".accountId").val();
        var accountName = $(".accountName").val();
        //var accountPassword = $(".accountPassword").val();
        var accountEmail = $(".accountEmail").val();
        var accountIntroduction = $(".accountIntroduction").val();
        var accountContacts = $(".accountContacts").val();
        var accountContactInformation = $(".accountContactInformation").val();
        var accountType = data.field.accountType;
        var accountNotes = $(".accountNotes").val();

        accountUpdateData = {
            accountId:parseInt(accountId),
            accountName:accountName,
            //accountPasswordRePass:accountPassword,
            accountEmail:accountEmail,
            accountIntroduction:accountIntroduction,
            accountContacts:accountContacts,
            accountContactInformation:accountContactInformation,
            accountType:parseInt(accountType),
            accountNotes:accountNotes
        };

        $.api("updateAdminAccountInfo", accountUpdateData, updateAdminAccountInfoCallBack, "admin/account");

        // setTimeout(function(){
        //     top.layer.close(index);
        //     top.layer.msg("添加成功！");
        //     layer.closeAll("iframe");
        //     //刷新父页面
        //     parent.location.reload();
        // },2000);
        return false;
    });

    function updateAdminAccountInfoCallBack(ret) {
        top.layer.close(index);
        top.layer.msg(ret.message);

        if (ret.status === "SUCCESS") {
            setTimeout(function () {

                parent.layer.closeAll("iframe");

                    //刷新父页面
                parent.updateTable($('.accountId').attr("curr-page"));
                //parent.location.reload();
                //parent.layui.table.reload('adminListTable',{page: {curr: $('.accountId').attr("curr-page")}});
            }, 1000)
        }
    }
});