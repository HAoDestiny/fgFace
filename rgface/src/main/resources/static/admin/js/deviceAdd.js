layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.verify({
        port:function(value) {
            if (parseInt(value) > 32767) {
                return "请输入正确的端口号";
            }
        },
        ip: function(value) {

            if (value === null || value === '' || parseInt(value) > 255) {
                return "请输入正确IP";
            }
        },
        pass: [/(.+){6,12}$/, '密码必须6到12位'],
        length: [ /^[a-zA-Z0-9]{6,10}$/, '用户名必须6-10位(字母、数字)']
    });

    var index;
    form.on("submit(addDevice)",function(data){
        //弹出loading
        index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});

        var deviceName = $(".deviceName").val();
        var deviceCode = $(".deviceCode").val();
        var dataServerIp = $("#dataServerIp_0").val() + "." + $("#dataServerIp_1").val() + "." + $("#dataServerIp_2").val() + "." + $("#dataServerIp_3").val();
        var dataServerPort = parseInt($(".dataServerPort").val());
        var configServerIp = $("#configServerIp_0").val() + "." + $("#configServerIp_1").val() + "." + $("#configServerIp_2").val() + "." + $("#configServerIp_3").val();
        var configServerPort = parseInt($(".configServerPort").val());
        var deviceNote = $(".deviceNotes").val();
        var deviceType = parseInt(data.field.deviceType);

        var accountRegData = {
            deviceName:deviceName,
            deviceCode:deviceCode,
            dataServerIp:dataServerIp,
            dataServerPort:dataServerPort,
            configServerIp:configServerIp,
            configServerPort:configServerPort,
            deviceNote:deviceNote,
            deviceType:deviceType
        };

        $.api("addDevice", accountRegData, addDeviceCallBack, "admin/device");

        // setTimeout(function(){
        //     top.layer.close(index);
        //     top.layer.msg("添加成功！");
        //     layer.closeAll("iframe");
        //     //刷新父页面
        //     parent.location.reload();
        // },2000);
        return false;
    });

    function addDeviceCallBack(ret) {
        top.layer.close(index);
        top.layer.msg(ret.message);
        if (ret.status === "SUCCESS") {
            layer.closeAll("iframe");
            //刷新父页面
            parent.location.reload();
        }
    }
});