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

    var loadMsg,
        fileId = 0,
        faceId = 0;

    form.verify({
        // pass: [/(.+){6,12}$/, '密码必须6到12位'],
        // fileId: function(value) {
        //     if (value === null || value === '') {
        //         return '请选择头像图片上传';
        //     }
        // },
        // faceId: function(value) {
        //     if (value === null || value === '') {
        //         return '请选择人脸图片上传(建议使用清晰的面部图片)';
        //     }
        // },
    });

    //初始化设备列表
    initDeviceList();

    //上传头像
    upload.render({
        elem: '.personnelAvatarBtn',
        url: '/api/upload/uploadPersonnelFile?adr=ad',
        method : 'post',
        accept: 'images',
        exts: 'jpg',
        size: 2000,
        drag: true,
        auto: false, //自动上传
        acceptMime: 'image/jpg',
        bindAction: '#uploadAvatarBtn', //提交
        data: {
            type: 1,
            personnelId: function () {
                return parseInt($(".fileId").attr("personnel-id"));
            },
            deviceId: 0
        },
        choose: function(obj) {

            // var files = obj.pushFile();
            //
            // if (files === null) {
            //     top.layer.msg('请选择上传图片', {
            //         anim: 3,
            //         time: 2000
            //     });
            //     return;
            // }

            obj.preview(function(index, file, result){
                $('#userFace').attr('src', result); //预览
            });
        },
        before: function (obj) {
            top.layer.msg('数据提交中...',{icon: 16,time:false,shade:0.8});
        },
        done: function(res, index, upload){

            console.log(res);

            if (res === null) {
                return;
            }

            top.layer.close(index);
            top.layer.msg(res.message);

            if (res.status === "SUCCESS") {

                fileId = res.data.fileId;
                //$('.fileId').val(fileId); //验证
            }
        },
        error: function (index, upload) {
            layer.close(loadMsg);
            top.layer.msg("上传失败")
        }
    });

    //上传人脸
    upload.render({
        elem: '.personnelFaceBtn',
        url: '/api/upload/uploadPersonnelFile?adr=ad',
        method : 'post',
        accept: 'images',
        exts: 'jpg',
        size: 2000,
        drag: true,
        auto: false, //自动上传
        bindAction: '#uploadFaceBtn', //提交
        data: {
            type: 2,
            personnelId: function () {
                return parseInt($(".fileId").attr("personnel-id"));
            },
            deviceId: function () {
                var deviceCode = $(".uploadFaceBtn").attr("device-code");
                if (deviceCode === undefined || deviceCode === '') {
                    return '';
                }
                return deviceCode;
            }
        },
        choose: function(obj) {

            // var files = obj.pushFile();
            //
            // if (files === null) {
            //     top.layer.msg('请选择上传图片', {
            //         anim: 3,
            //         time: 2000
            //     });
            //     return;
            // }

            obj.preview(function(index, file, result) {
                $('.userFace').attr('src', result); //预览
            });
        },
        before: function (obj) {
            top.layer.msg('数据提交中...',{icon: 16,time:false,shade:0.8});
        },
        done: function(res, index, upload){

            console.log(res);

            if (res === null) {
                return;
            }

            top.layer.closeAll(index);
            top.layer.msg(res.message);

            if (res.status === "SUCCESS") {

                faceId = res.data.fileId;
                //$('.faceId').val(faceId); //验证
            }
        },
        error: function (index, upload) {
            layer.close(loadMsg);
            top.layer.msg("上传失败")
        }
    });

    form.on("submit(updatePersonnel)",function(data) {


        var personnelId, mobile, icCard, studyType,
            truename,
            personnelSex,
            personnelType,
            personnelStatus,
            personnelClassCode,
            personnelCode,
            personnelGrade,
            personnelByAccountName;

        //弹出loading
        loadMsg = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});

        personnelId = parseInt($(".fileId").attr("personnel-id"));
        mobile = $(".mobile").val();
        icCard = $(".icCard").val();
        truename = $(".personnelName").val();
        personnelSex = parseInt(data.field.personnelSex);
        studyType = parseInt(data.field.studyType);
        personnelType = parseInt(data.field.personnelType);
        personnelStatus = parseInt(data.field.personnelStatus);
        personnelClassCode = $(".personnelClassCode").val();
        personnelCode = $(".personnelCode").val();
        personnelGrade = $(".personnelGrade").val();
        personnelByAccountName = $(".personnelByAccountName").val();

        $.api("updatePersonnel", {
            personnelId:personnelId,
            fileId:fileId,
            faceId:faceId,
            mobile:mobile,
            icCard:icCard,
            truename:truename,
            studyType:studyType,
            sex:personnelSex,
            personnelType:personnelType,
            personnelCode:personnelCode,
            personnelGrade:personnelGrade,
            personnelStart:personnelStatus,
            personnelClassCode:personnelClassCode,
            personnelByAccountName:personnelByAccountName
        }, addPersonnelCallBack, "admin/personnel");

        // setTimeout(function(){
        //     top.layer.close(index);
        //     top.layer.msg("添加成功！");
        //     layer.closeAll("iframe");
        //     //刷新父页面
        //     parent.location.reload();
        // },2000);

        return false;
    });

    form.on('select(deviceNameList)', function(data){

        $('.uploadFaceBtn').attr('device-code', data.value);
    });

    function initDeviceList() {

        $.api("getDeviceNameList", {}, function(ret) {

            if (ret.status === "SUCCESS") {

                if (ret.data.length > 0) {
                    $.each(ret.data, function (index, item) {

                        $('.deviceNameList').append('<option value="' + item.deviceCode + '">'+ item.deviceName +'</option> ');

                    });
                }
            }

            else {
                initDeviceNameList('', '');
            }

            layui.form.render(); //渲染form

        }, "base");
    }

    function addPersonnelCallBack(ret) {
        top.layer.close(loadMsg);

        top.layer.msg(ret.message);
        if (ret.status === "SUCCESS") {

            setTimeout(function () {

                parent.layer.closeAll("iframe");

                //刷新父页面
                parent.updateTable($('.fileId').attr("curr-page"));
                //parent.location.reload();
                //parent.layui.table.reload('adminListTable',{page: {curr: $('.accountId').attr("curr-page")}});
            }, 500)
        }
    }
});