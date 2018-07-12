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

    var loadMsg, fileId, faceId;

    form.verify({
        pass: [/(.+){6,12}$/, '密码必须6到12位'],
        fileId: function(value) {
            if (value === null || value === '') {
                return '请选择头像图片上传';
            }
        },
        faceId: function(value) {
            if (value === null || value === '') {
                return '请选择人脸图片上传(建议使用清晰的面部图片)';
            }
        },
        icCard: function(value) {
            var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
            if (reg.test(value) === false) {
                return '身份证输入不合法, 请重新输入'
            }
        },
        mobile:[/^1(3|4|5|7|8)\d{9}$/, '请输入正确手机号'],
        length: [ /^[a-zA-Z0-9_-]{4,12}$/, '用户名必须4-12位(字母、数字、下划线、减号)']
    });

    //上传头像
    // upload.render({
    //     elem: '.personnelAvatarBtn',
    //     url: '/api/upload/uploadPersonnelFile?adr=ad',
    //     method : 'post',
    //     accept: 'images',
    //     exts: 'jpg',
    //     size: 2000,
    //     drag: true,
    //     auto: false, //自动上传
    //     bindAction: '#uploadAvatarBtn', //提交
    //     data: {
    //         type: 1,
    //         personnelId: 0,
    //         deviceId: 0
    //     },
    //     choose: function(obj) {
    //
    //         // var files = obj.pushFile();
    //         //
    //         // if (files === null) {
    //         //     top.layer.msg('请选择上传图片', {
    //         //         anim: 3,
    //         //         time: 2000
    //         //     });
    //         //     return;
    //         // }
    //
    //         obj.preview(function(index, file, result){
    //             $('#userFace').attr('src', result); //预览
    //         });
    //     },
    //     before: function (obj) {
    //         top.layer.msg('数据提交中...',{icon: 16,time:false,shade:0.8});
    //     },
    //     done: function(res, index, upload){
    //
    //         console.log(res);
    //         top.layer.closeAll();
    //         if (res === null) {
    //             return;
    //         }
    //
    //         top.layer.close(index);
    //         top.layer.msg(res.message);
    //
    //         if (res.status === "SUCCESS") {
    //
    //             fileId = res.data.fileId;
    //             $('.fileId').val(fileId);
    //         }
    //     },
    //     error: function (index, upload) {
    //         layer.close(loadMsg);
    //         top.layer.msg("上传失败")
    //     }
    // });
    //
    // //上传人脸
    // upload.render({
    //     elem: '.personnelFaceBtn',
    //     url: '/api/upload/uploadPersonnelFile?adr=ad',
    //     method : 'post',
    //     accept: 'images',
    //     exts: 'jpg',
    //     size: 2000,
    //     drag: true,
    //     auto: false, //自动上传
    //     bindAction: '#uploadFaceBtn', //提交
    //     data: {
    //       type: 2,
    //       personnelId: 0,
    //       deviceId: 0
    //     },
    //     choose: function(obj) {
    //
    //         // var files = obj.pushFile();
    //         //
    //         // if (files === null) {
    //         //     top.layer.msg('请选择上传图片', {
    //         //         anim: 3,
    //         //         time: 2000
    //         //     });
    //         //     return;
    //         // }
    //
    //         obj.preview(function(index, file, result){
    //             $('.userFace').attr('src', result); //预览
    //         });
    //     },
    //     before: function (obj) {
    //         top.layer.msg('数据提交中...',{icon: 16,time:false,shade:0.8});
    //     },
    //     done: function(res, index, upload){
    //
    //         console.log(res);
    //         top.layer.closeAll();
    //         if (res === null) {
    //             return;
    //         }
    //
    //         top.layer.close(index);
    //         top.layer.msg(res.message);
    //
    //         if (res.status === "SUCCESS") {
    //
    //             faceId = res.data.fileId;
    //             $('.faceId').val(faceId);
    //         }
    //     },
    //     error: function (index, upload) {
    //         layer.close(loadMsg);
    //         top.layer.msg("上传失败")
    //     }
    // });

    form.on("select(personnelType)", function (data) {

        changeStudyType(data.value);

    });

    form.on("submit(addPersonnel)",function(data){
        //弹出loading

        var mobile, icCard, password, studyType,
            truename,
            personnelSex,
            personnelType,
            personnelStatus,
            personnelClassCode,
            personnelCode,
            personnelGrade;

        loadMsg = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});

        mobile = $(".mobile").val();
        icCard = $(".icCard").val();
        password = $(".password").val();
        truename = $(".personnelName").val();
        personnelSex = parseInt(data.field.personnelSex);
        personnelType = parseInt(data.field.personnelType);
        studyType = parseInt(data.field.studyType);
        personnelStatus = parseInt(data.field.personnelStatus);
        personnelClassCode = $(".personnelClassCode").val();
        personnelCode = $(".personnelCode").val();
        personnelGrade = $(".personnelGrade").val();

        $.api("addPersonnel", {
            fileId:1, //默认头像
            faceId:1, //默认人脸
            mobile:mobile,
            icCard:icCard,
            password:password,
            truename:truename,
            studyType:studyType,
            sex:personnelSex,
            personnelType:personnelType,
            personnelCode:personnelCode,
            personnelGrade:personnelGrade,
            personnelStart:personnelStatus,
            personnelClassCode:personnelClassCode
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

    function addPersonnelCallBack(ret) {
        top.layer.close(loadMsg);
        top.layer.msg(ret.message);
        if (ret.status === "SUCCESS") {
            layer.closeAll("iframe");
            //刷新父页面
            parent.location.reload();
        }
    }

    function changeStudyType(value) {

        var html;

        $('.studyType').empty(); //清空

        // $('.div_personnelClassCode').show();
        // $('.personnelGrade').show();
        //
        // if (value === '2' || value === '3') {
        //     $('.personnelClassCode').hide();
        //     $('.personnelGrade').hide();
        // }

        if (value === '0') {

            $('.personnelCodeLabel').text("学号");

            html = '<option value="0" selected="selected">住读生</option>' +
                '<option value="1">走读生</option>' +
                '<option value="2">其他</option>';
        }

        else {

            $('.personnelCodeLabel').text("编号");

            html = '<option value="0">住读生</option>' +
                '<option value="1">走读生</option>' +
                '<option value="2" selected="selected">其他</option>';
        }

        $('.studyType').append(html);

        layui.form.render(); //渲染form
    }
});