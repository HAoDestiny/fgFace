layui.use(['form','layer','jquery'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer
        $ = layui.jquery;

    // $(".loginBody .seraph").click(function(){
    //     layer.msg("这只是做个样式，至于功能，你见过哪个后台能这样登录的？还是老老实实的找管理员去注册吧",{
    //         time:5000
    //     });
    // });

    initVerifyCode();

    //初始化验证码
    $('#verifyCode').click(function () {
        initVerifyCode();
    });

    //登录按钮
    form.on("submit(login)",function(data){
        $(this).text("登录中...").attr("disabled","disabled").addClass("layui-disabled");

        var accountName = $("#accountName").val();
        var password = $("#password").val();
        var verify = $('#code').val();


        $.api("login", {accountName:accountName, password:password, verifyCode: verify}, adminLoginCallBack, "admin/account");

        return false;
    });

    function adminLoginCallBack(ret) {

        $("#adminLogin").text("登录").attr("disabled",false).removeClass("layui-disabled");
        layer.msg(ret.message, {
            anim:6,
            time:2000
        });

        if (ret.status === "SUCCESS") {
            window.location.href = "index.html";
        }

        if (ret.status === "ERROR") {
            initVerifyCode();
        }
    }

    function initVerifyCode() {
        console.log($('#verifyCode').prop("src"));
        $('#verifyCode').attr("src", "//192.168.2.121:8001/api/verification/getVerificationImg?v=a&t=" + Math.random());

        layui.form.render(); //渲染form
    }

    //表单输入效果
    $(".loginBody .input-item").click(function(e){
        e.stopPropagation();
        $(this).addClass("layui-input-focus").find(".layui-input").focus();
    });
    $(".loginBody .layui-form-item .layui-input").focus(function(){
        $(this).parent().addClass("layui-input-focus");
    });
    $(".loginBody .layui-form-item .layui-input").blur(function(){
        $(this).parent().removeClass("layui-input-focus");
        if($(this).val() != ''){
            $(this).parent().addClass("layui-input-active");
        }else{
            $(this).parent().removeClass("layui-input-active");
        }
    })
});
