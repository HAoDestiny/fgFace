var form, $,areaData;
layui.config({
    base : "static/admin/js/"
}).extend({
    "address" : "address"
})
layui.use(['element','form','layer','upload','laydate',"address"],function(){
    form = layui.form;
    $ = layui.jquery;
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        element = layui.element,
        laydate = layui.laydate,
        address = layui.address;

    element.init();

    if(window.sessionStorage.getItem("showNotice") !== "true"){
        showNotice();
    }

    var loadMsg, fileId = 1, faceId = 1;

    var load = layer.msg('数据获取中...',{icon: 16,time:false,shade:0.8});

    $.api("getPersonnelInfo", {}, function (ret) {

        if (ret.status === "SUCCESS") {

            layer.close(load);

            initPersonnelInfo(ret.data);

            initPersonnelSex(ret.data.sex);
            initPersonnelType(ret.data.personnelType);
            initStudyType(ret.data.studyType);

            //initDeviceNameList();
        }

    }, "personnel");

    //初始化信息
    function initPersonnelInfo(data) {
        $('.personnelName').attr("personnelId", data.personnelId);
        $('.personnelName').val(data.truename);
        $('.icCard').val(data.icCard);
        $('.mobile').val(data.mobile);
        $('.personnelGrade').val(data.personnelGrade);
        $('.personnelClassCode').val(data.personnelClassCode);
        $('.personnelCode').val(data.personnelCode);

        $(".fileId").attr("personnel-id", data.personnelId);

        fileId = data.avatarId;
        faceId = data.faceId;

        $('.fileId').val(fileId);
        $('.faceId').val(faceId);

        var reviewStatus;
        if (data.faceReviewStatus === 0) {
            reviewStatus = '(人脸图片已提交审核, 请耐心等待审核结果)';

        }

        if (data.faceReviewStatus === 2) {
            reviewStatus = '(人脸图片审核失败, 请重新上传人脸提交)';

        }

        //提示
        $('.Notify').text(reviewStatus);

        $('#userFace').attr("src", data.avatar.fileHost + '/file/' + data.avatar.fileName);
        $('.userFace').attr("src", data.face.fileHost + '/face/' + data.face.fileName);
    }

    //初始化personnelSex
    function initPersonnelSex(value) {

        var html;

        if (value === 0) {
            html = '<input type="radio" lay-filter="erweima" name="personnelSex" value="1" title="男">' +
                '<input type="radio" lay-filter="erweima" name="personnelSex" value="0" title="女" checked>';
        }

        if (value === 1) {
            html = '<input type="radio" lay-filter="erweima" name="personnelSex" value="1" title="男" checked>' +
                '<input type="radio" lay-filter="erweima" name="personnelSex" value="0" title="女">';
        }

        $('.personnelSex').append(html);
    }

    //初始化personnelType
    function initPersonnelType(value) {
        var html;

        if (value === 0) {
            html = '<option value="0" selected="selected">学生</option>' +
                '<option value="1">教职工</option>' +
                '<option value="2">外来人员</option>' +
                '<option value="3">其他</option>';
        }

        if (value === 1) {
            html = '<option value="0">学生</option>' +
                '<option value="1" selected="selected">教职工</option>' +
                '<option value="2">外来人员</option>' +
                '<option value="3">其他</option>';
        }

        if (value === 2) {
            html = '<option value="0">学生</option>' +
                '<option value="1">教职工</option>' +
                '<option value="2" selected="selected">外来人员</option>' +
                '<option value="3">其他</option>';
        }

        if (value === 3) {
            html = '<option value="0">学生</option>' +
                '<option value="1">教职工</option>' +
                '<option value="2">外来人员</option>' +
                '<option value="3" selected="selected">其他</option>';
        }

        $('.personnelType').append(html);

    }

    //初始化studyType
    function initStudyType(value) {
        var html;

        if (value === 0) {
            html = '<option value="0" selected="selected">住读生</option>' +
                '<option value="1">走读生</option>';
        }

        if (value === 1) {
            html = '<option value="0">住读生</option>' +
                '<option value="1" selected="selected">走读生</option>';
        }

        $('.studyType').append(html);

        layui.form.render();
    }


    //初始化设备列表
    // function initDeviceNameList() {
    //
    //     $.api("getDeviceNameList", {}, function (ret) {
    //
    //         if (ret.status === "SUCCESS") {
    //
    //             if (ret.data.length > 0) {
    //                 $.each(ret.data, function (index, item) {
    //
    //                     $('.deviceNameList').append('<option value="' + item.deviceCode + '">'+ item.deviceName +'</option> ');
    //
    //                 });
    //             }
    //         }
    //
    //         layui.form.render();
    //
    //     }, "base");
    //
    // }

    form.verify({
        // pass: [/(.+){6,12}$/, '密码必须6到12位'],
        fileId: function(value) {
            if (value === '1' || value === null || value === '') {
                return '请选择头像图片上传';
            }
        }
        // faceId: function(value) {
        //     if (value === '1' || value === null || value === '') {
        //         return '请选择人脸图片上传(建议使用清晰的面部图片)';
        //     }
        // }
    });

    //上传头像
    upload.render({
        elem: '.personnelAvatarBtn',
        url: '/api/upload/uploadPersonnelFile?adr=pr',
        method : 'post',
        accept: 'images',
        exts: 'jpg',
        size: 2000,
        drag: true,
        auto: false, //自动上传
        bindAction: '#uploadAvatarBtn', //提交
        data: {
            type: 1,
            personnelId: 0,
            deviceId: 0
        },
        choose: function(obj) {
            //将每次选择的文件追加到文件队列-多文件上传操作
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
            layer.msg('数据提交中...',{icon: 16,time:false,shade:0.8});
        },
        done: function(res, index, upload){

            //console.log(res);
            top.layer.closeAll();

            if (res === null) {
                return;
            }

            top.layer.close(index);
            top.layer.msg(res.message);

            if (res.status === "SUCCESS") {

                fileId = res.data.fileId;
                $('.fileId').val(fileId); //验证
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
        url: '/api/upload/uploadPersonnelFile?adr=pr',
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
                return $('.personnelName').attr("personnelId");
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

            //将每次选择的文件追加到文件队列
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
                $('.userFace').attr('src', result); //预览
            });
        },
        before: function (obj) {
            layer.msg('数据提交中...',{icon: 16,time:false,shade:0.8});
        },
        done: function(res, index, upload){

            //console.log(res);
            top.layer.closeAll();
            if (res === null) {
                return;
            }

            top.layer.close(index);
            top.layer.msg(res.message);

            if (res.status === "SUCCESS") {

                $('.Notify').text('(人脸图片已提交审核, 请耐心等待审核结果)');
                // faceId = res.data.fileId;
                // $('.faceId').val(faceId); //验证
            }
        },
        error: function (index, upload) {
            layer.close(loadMsg);
            top.layer.msg("上传失败")
        }
    });

    form.on("submit(updatePersonnel)",function(data){

        //弹出loading
        var personnelId,
            mobile,
            icCard,
            studyType,
            truename,
            personnelSex,
            personnelType,
            personnelClassCode,
            personnelCode,
            personnelGrade;

        loadMsg = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});

        personnelId = $(".fileId").attr("personnel-id");
        mobile = $(".mobile").val();
        icCard = $(".icCard").val();
        truename = $(".personnelName").val();
        personnelSex = parseInt(data.field.personnelSex);
        studyType = parseInt(data.field.studyType);
        personnelType = parseInt(data.field.personnelType);
        personnelClassCode = $(".personnelClassCode").val();
        personnelCode = $(".personnelCode").val();
        personnelGrade = $(".personnelGrade").val();

        $.api("updatePersonnelInfo", {
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
            personnelClassCode:personnelClassCode
        }, addPersonnelCallBack, "personnel");

        return false;
    });

    form.on('select(deviceNameList)', function(data){

        $('.uploadFaceBtn').attr('device-code', data.value);
    });

    function addPersonnelCallBack(ret) {
        top.layer.close(loadMsg);
        top.layer.msg(ret.message);

        if (ret.status === "SUCCESS") {
            setTimeout(function () {

                //刷新页面
                location.reload();

            }, 500)
        }
    }

    $('.updatePassword').click(function () {
        updatePassword();
    });

    //修改密码
    function updatePassword(){
        var index = layui.layer.open({
            title : "修改密码",
            type : 2,
            area: ['470px', '280px'],
            moveType: 0 , //拖拽模式，0或者1
            content : "personnelPassword.html",
            success : function(layero, index){ //初始化数据

                var body = layui.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];

                //tip
                setTimeout(function(){
                    layui.layer.tips('点击此处关闭', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500)
            }
        });
    }

    //公告层
    function showNotice(){
        layer.open({
            type: 1,
            title: "系统公告",
            area: '300px',
            shade: 0.8,
            id: 'LAY_SH_PRO',
            btn: ['立即修改'],
            moveType: 1,
            content: '<div style="padding:15px 20px; text-align:justify; line-height: 22px; text-indent:2em;border-bottom:1px solid #e2e2e2;"><p class="layui-red">如果是第一次登录,建议更改密码</p></pclass></p><p>头像是指个人上半身照片(用于显示个人信息),人脸是指面部(要求图片清晰,小于5M)<span class="layui-red">请认真填写相关内容,否则会影响识别</span></p></div>',
            success: function(layero){
                var btn = layero.find('.layui-layer-btn');
                btn.css('text-align', 'center');
                btn.on("click",function(){
                    updatePassword();
                });
            },
            cancel: function(index, layero){
                tipsShow();
            }
        });
    }
    function tipsShow(){
        window.sessionStorage.setItem("showNotice","true");
        if($(window).width() > 432){  //如果页面宽度不足以显示顶部“系统公告”按钮，则不提示
            layer.tips('系统公告躲在了这里', '#systemNotice', {
                tips: 3,
                time : 1000
            });
        }
    }
    $("#systemNotice").on("click",function(){
        showNotice();
    });

    //退出
    $('#signOut').click(function () {
        layer.confirm("是否确定退出",{
            icon: 3,
            title:'系统提示',
            cancel : function(index){
                layer.close(index);
            }

        },function(index){

            $.api("logout", {}, function (ret) {

                layer.msg(ret.message);

                if (ret.status === "SUCCESS") {
                    window.location.href = ret.data;
                }
            }, "personnel");

        },function(index){
            layer.close(index);
        });
    });
});