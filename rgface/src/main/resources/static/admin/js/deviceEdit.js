layui.use(['form', 'layer'], function () {
    var form = layui.form
    layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.verify({
        pass: [/(.+){6,12}$/, '密码必须6到12位'],
        length: [/^[a-zA-Z0-9]{6,10}$/, '用户名必须6-10位(字母、数字)']
    });

    var index;
    var deviceUpdateData;

    form.on("submit(updateAdminDevice)", function (data) {
        //弹出loading
        index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});

        var deviceId = $(".deviceId").val();
        var deviceName = $(".deviceName").val();
        var deviceCode = $(".deviceCode").val();
        var dataServerIp = $("#dataServerIp_0").val() + "." + $("#dataServerIp_1").val() + "." + $("#dataServerIp_2").val() + "." + $("#dataServerIp_3").val();
        var dataServerPort = parseInt($(".dataServerPort").val());
        var configServerIp = $("#configServerIp_0").val() + "." + $("#configServerIp_1").val() + "." + $("#configServerIp_2").val() + "." + $("#configServerIp_3").val();
        var configServerPort = parseInt($(".configServerPort").val());
        var deviceFirmwareVersion = $(".deviceFirmwareVersion").val();
        var deviceNote = $(".deviceNotes").val();
        var deviceType = parseInt(data.field.deviceType);

        deviceUpdateData = {
            deviceName: deviceName,
            deviceCode: deviceCode,
            dataServerIp: dataServerIp,
            dataServerPort: dataServerPort,
            configServerIp: configServerIp,
            configServerPort: configServerPort,
            deviceFirmwareVersion: deviceFirmwareVersion,
            deviceNote: deviceNote,
            deviceType: deviceType
        };

        $.api("updateDevice", deviceUpdateData, updateDeviceCallBack, "admin/device");

        return false;
    });

    var type;
    var statusVal;
    var Load;
    var checkedObj;

    //对比开关
    form.on('switch(device_contrast)', function (data) {

        type = 0;
        checkedObj = $(this);
        Load = layer.msg('修改中...', {icon: 16, time: false, shade: 0.8});

        var deviceCode = $(".deviceCode").val();

        if (data.elem.checked) {

            statusVal = 0;

        } else {

            statusVal = 1;
        }

        $.api("updateDeviceParam", {
            type: type,
            status: statusVal,
            deviceCode: deviceCode
        }, updateDeviceStatusCallback, "admin/device");
    });

    //去除重复
    form.on('switch(device_repeat)', function (data) {

        type = 1;
        checkedObj = $(this);
        Load = layer.msg('修改中...', {icon: 16, time: false, shade: 0.8});

        var deviceCode = $(".deviceCode").val();

        if (data.elem.checked) {
            //开
            statusVal = 0;

        } else {
            //关
            statusVal = 1;
        }

        $.api("updateDeviceParam", {
            type: type,
            status: statusVal,
            deviceCode: deviceCode
        }, updateDeviceStatusCallback, "admin/device");
    });

    //设置重复时间
    $('.btn_setRepeatTime').click(function () {

        var repeatTimeInterval = $('.repeatTimeInterval').val();

        checkParam(repeatTimeInterval, 2);

        Load = layer.msg('修改中...', {icon: 16, time: false, shade: 0.8});

        $.api("updateDeviceParam", {
            type: 2,
            status: 2,
            deviceCode: $(".deviceCode").val(),
            repeatTimeInterval: repeatTimeInterval
        }, updateDeviceStatusCallback, "admin/device");

    });

    //设置相似度
    $('.btn_setContrastLike').click(function () {

        var contrastLike = $('.contrastLike').val();

        checkParam(contrastLike, 3);

        Load = layer.msg('修改中...', {icon: 16, time: false, shade: 0.8});

        $.api("updateDeviceParam", {
            type: 3,
            status: 3,
            deviceCode: $(".deviceCode").val(),
            contrastLike: contrastLike
        }, updateDeviceStatusCallback, "admin/device");

    });

    function updateDeviceCallBack(ret) {
        top.layer.close(index);
        top.layer.msg(ret.message);

        if (ret.status === "SUCCESS") {
            setTimeout(function () {

                parent.layer.closeAll("iframe");

                //刷新父页面
                parent.updateTable($('.deviceName').attr("curr-page"));
                //parent.location.reload();
                //parent.layui.table.reload('adminListTable',{page: {curr: $('.accountId').attr("curr-page")}});
            }, 1000)
        }
    }

    function updateDeviceStatusCallback(ret) {

        layer.close(Load);
        layer.msg(ret.message);

        if (ret.status === "ERROR") {

            console.log($(checkedObj).attr("checked"));

            changeCheckbox();
        }

        if (ret.status === "SUCCESS") {

            if (type === 0 && statusVal === 0) {
                $('.contrastLike').removeAttr("disabled");
            }

            if (type === 0 && statusVal === 1) {
                $('.contrastLike').attr("disabled", "disabled");
            }

            if (type === 1 && statusVal === 0) {
                $('.repeatTimeInterval').removeAttr("disabled");
            }

            if (type === 1 && statusVal === 1) {
                $('.repeatTimeInterval').attr("disabled", "disabled");
            }
        }
    }

    function changeCheckbox() {
        if ($(checkedObj).attr("checked") === "checked") {

            $(checkedObj).prop("checked", true);
        } else {

            $(checkedObj).prop("checked", false);
        }

        form.render();
    }

    function checkParam(value, type) {

        //重复间隔
        if (type === 2) {

            if (value === null || value === '' || parseInt(value) > 255 || parseInt(value) < 1) {

                layer.open({
                    title: '系统提示'
                    , content: '重复时间间隔介于1-255之间,请重新输入'
                });

                $('.repeatTimeInterval').val("1");

                return;
            }
        }

        //相似度
        if (type === 3) {

            if (value === null || value === '' || parseInt(value) > 100 || parseInt(value) < 10) {

                layer.open({
                    title: '系统提示'
                    , content: '相似度介于10-100之间,请重新输入'
                });

                $('.contrastLike').val("10");

                return;
            }

        }
    }
});