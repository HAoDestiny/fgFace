var goal = $(".Ip");
// ip输入框的最大值
var ip_max = 255;

$(goal).focus(function () {

    var obj;

    if ($(this).attr("id") === "dataServerIp_0") {
        obj = $("#dataServerIp_1");
    }

    if ($(this).attr("id") === "dataServerIp_1") {
        obj = $("#dataServerIp_2");
    }

    if ($(this).attr("id") === "dataServerIp_2") {
        obj = $("#dataServerIp_3");
    }

    if ($(this).attr("id") === "dataServerIp_3") {
        inputOnclick(this);
        return;
    }

    if ($(this).attr("id") === "configServerIp_0") {
        obj = $("#configServerIp_1");
    }

    if ($(this).attr("id") === "configServerIp_1") {
        obj = $("#configServerIp_2");
    }

    if ($(this).attr("id") === "configServerIp_2") {
        obj = $("#configServerIp_3");
    }

    if ($(this).attr("id") === "configServerIp_3") {
        inputOnclick(this);
        return;
    }

    inputOnclick(this, obj);
});

// 监听键盘输入事件
function inputOnclick(obj, objNext) {

    $(obj).bind("keydown", function(event){
        var code = event.keyCode;

        // 只能输入数字键、删除键、小数点，tab键，其他的都不能输入
        if((code < 48 && 8 !== code && 37 !== code && 39 !== code && 9 !== code) || (code > 57 && code < 96) || (code > 105 && 110 !== code && 190 !== code))
        { return false; }

        if(code === 9 || code === 190) {

            if (objNext === undefined || objNext === null) {
                return;
            }
            objNext.focus();
            return false;
        }
    });

    // 监听键盘离开事件
    $(obj).bind("keyup", function(event){
        // 判断当前输入框的值
        var value = $(this).val();
        var idContent = $(this).attr("id");

        var context;

        if ((idContent === "dataServerIp_0" || idContent === "configServerIp_0") && value != null && value != '' &&  parseInt(value) > 223 ) {

            $(this).css("background-color", "#FF5722");
            $(this).css("border-color", "#FFF");

            context = "请指定一个介于1和223间的值";
            $(this).val("223");
            $(this).focus();

            layer.open({
                title: '系统提示'
                ,anim: 6
                ,content: value + '不是有效项。' + context
            });

            return;
        }

        if(value != null && value != '' && parseInt(value) > 255) {

            $(this).css("background-color", "#FF5722");
            $(this).css("border-color", "#FFF");

            context = "请指定一个介于1和255间的值";
            $(this).val("255");
            $(this).focus();

            layer.open({
                title: '系统提示'
                ,anim: 6
                ,content: value + '不是有效项。' + context
            });

            return;
        }

        // 判断是否是0开头的不规范数字
        if(value != null && value != '' && parseInt(value) != 0) {

            value = parseInt(value);
            if(isNaN(value)){
                $(this).val("");
            }else {
                $(this).val(""+value);
            }
        }

        if (value != null && value != '' && (value+'').length === 3) {

            $(this).css("background-color", "");
            $(this).css("border-color", "");

            if (objNext === undefined || objNext === null) {
                return;
            }

            objNext.focus();
        }
    });

}

// 失去焦点事件
$(goal).bind("blur", function(){
    var value = $(this).val();
    // 如果失去焦点，当前的值为0，则加上红色边框，否则去掉红色边框

    if (parseInt(value) >= 0 && parseInt(value) <= 255) {
        $(this).css("background-color", "");
        $(this).css("border-color", "");
    }

    if(value == null || value == '' || value == undefined) {
        $(this).css("border-color", "#F08080");
    }else {
        $(this).css("border-color", "");
    }
})