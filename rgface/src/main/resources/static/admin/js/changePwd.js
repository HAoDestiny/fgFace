layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    $('.accountName').val(window.sessionStorage.getItem("accountName"));

    //添加验证规则
    form.verify({
        // oldPwd : function(value, item){
        //     if(value != "123456"){
        //         return "密码错误，请重新输入！";
        //     }
        // },
        newPwd : function(value, item){
            if(value.length < 6){
                return "密码长度不能小于6位";
            }
        },
        confirmPwd : function(value, item){
            if(!new RegExp($("#oldPwd").val()).test(value)){
                return "两次输入密码不一致，请重新输入！";
            }
        }
    });

    $('.oldPwd').blur(function () {

        var vlaue = $('.oldPwd').val();


    });
    
    form.on('submit(changePwd)', function(data){

        var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});

        var param = {
            "oldPassword": $('.oldPwd').val(),
            "newPassword": $('.confirmPwd').val()
        };

        $.api("updateAdminAccountPassword", param, function (ret) {

            layer.close(index);

            if (ret.status === "SUSSES") {

                layer.msg(ret.message, {time: 2000});

                var indexs = parent.layer.getFrameIndex(window.name); //当前窗口

                setTimeout(function () {
                    parent.layer.close(indexs);
                }, 1000)

            } else {
                layui.layer.tips('密码错误', '.oldPwd', {
                    tips: [1, '#c00'],
                    time: 2000
                });

                $('.oldPwd').attr("style", "border-color:#FF5722!important;");

            }

        }, "admin/account");

        return false;
    });

    //控制表格编辑时文本的位置【跟随渲染时的位置】
    $("body").on("click",".layui-table-body.layui-table-main tbody tr td",function(){
        $(this).find(".layui-table-edit").addClass("layui-"+$(this).attr("align"));
    });

});