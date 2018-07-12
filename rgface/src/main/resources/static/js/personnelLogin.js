(function () {

    initCode();

    var publicKey = '';
    $(function () {
        $.api("getPublicKey", {mch_id:"hkPNn9kXoLurGY67", nonce_str:"bGUqI45sIug4R3mQlZs7nRnhxHF2"}, function (ret) {
            if (ret.status === "SUCCESS" && ret.data !== null) {
                publicKey = ret.data.toString();
            }
        }, "base");
    });

    $('.login-button').click(function () {

        var personnel = $('#username').val();
        var password = $('#userpwd').val();
        var code = $('#v_code').val();

        if (personnel === null || personnel === '') {
            sh.UI.Tip("请输入用户名");
            return;
        }

        if (password === null || password === '') {
            sh.UI.Tip("请输入密码");
            return;
        }

        if (code === null || code === '') {
            sh.UI.Tip("请输入验证码");
            return;
        }

        sh.Business.Personnel.login(personnel, encryptPw(password), code);
    });

    $('#imgCode').click(function () {
        initCode();
    });

    function initCode() {
        $('#imgCode').attr('src', '//192.168.2.121:8001/api/verification/getVerificationImg?v=p&t=' + Math.random());
    }

    function encryptPw(val) {
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(publicKey);
        return encrypt.encrypt(val);
    }
})();