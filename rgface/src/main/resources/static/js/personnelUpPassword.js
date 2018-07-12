layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.verify({
        pass:[/(.+){6,12}$/, '密码必须6到12位'],
        Determine: function (value) {

            if (value !== $('.NPassword').val()) {
                return '两次输入密码不一致,请重新输入';
            }
        }
    });

    form.on("submit(updatePassword)",function(data){

        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});

        var password = $('.password').val();
        var RPassword = $('.RPassword').val();

        $.api('updatePersonnelPassword', {password:RPassword, oldPassword:password}, function (ret) {

            top.layer.close(index);

            if (ret.status === "SUCCESS") {
                top.layer.msg(ret.message);
                setTimeout(function () {
                    parent.layer.closeAll("iframe");

                }, 500)

            } else {
                layui.layer.tips(ret.message, '.password', {
                    tips: [2, '#c00']
                });
            }

        }, 'personnel');

        return false;
    });

});