layui.config({
    base: "static/admin/js/"
}).use(['flow', 'form', 'layer', 'upload'], function () {
    var flow = layui.flow,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        $ = layui.jquery;

    var Load;

    //搜索设备
    $('.searchFaceByDevice').click(function () {

        layer.prompt({
            title: '请输入设备编号',
            value: '20180523'
        }, function (value, index, elem) {

            if (value === null || value === '') {
                layer.msg("请输入设备编号", {
                    anim: 6,
                    time: 2000
                });
                return;
            }

            Load = layer.msg('加载中...', {icon: 16, time: false, shade: 0.8});

            //清空
            $('#Images').empty();

            //加载图片
            loadFaceImage(value);

            layer.close(index);
        });

    });

    //复制人脸
    $('.copyFaceByDevice').click(function () {

        layer.prompt({
            title: '请输入设备编号',
            value: '20180531'
        }, function (value, index, elem) {

            Load = layer.msg('加载中...', {icon: 16, time: false, shade: 0.8});

            //复制
            batchCopyFaceToDevice(value);

            layer.close(index);
        });

    });

    //流加载图片
    function loadFaceImage(deviceCode) {

        var imgNums = 15;  //单页显示图片数量

        flow.load({
            elem: '#Images', //流加载容器
            isLazyimg: true,
            done: function (page, next) { //加载下一页

                layer.close(Load);

                $.api("getDeviceFaceList", {
                    deviceCode: deviceCode,
                    pageCode: page,
                    pageSize: imgNums
                }, function (ret) {

                    var html;
                    var imgList = [];
                    var total = 0;

                    if ("ERROR" === ret.status) {
                        layer.open({
                            title: '系统提示'
                            , content: ret.message
                        });

                        return;
                    }

                    if (ret.data.adminFacePo !== null) {

                        $.each(ret.data.adminFacePo, function (index, item) {

                            html = '<li><img ';

                            if (item.facePo !== null) {

                                html += ' src="' + item.facePo.fileHost + '/face/' + item.facePo.fileName + '"';
                            }

                            else {
                                html += ' src="static/admin/images/d_face.jpg"';
                            }

                            html += ' alt="' + item.faceName + '" face-id="' + item.faceId + '" face-device="' + ret.data.deviceCode +'">';

                            html += '<div class="operate" ><div class="check" >' +
                                '<input type="checkbox" name="belle"  face-id="'+ item.faceId +'" face-device="'+ ret.data.deviceCode +'" lay-filter="choose" lay-skin="primary" title="' + item.faceName + '">' +
                                '</div>' +
                                '<i class="layui-icon img_del">&#xe640;</i>' +
                                '</div>' +
                                '</li>';

                            imgList.push(html);

                        });

                    }

                    total = ret.data.entriesTotal;

                    next(imgList.join(''), page < (total / imgNums));

                    form.render();

                    $("#Images li img").height($("#Images li img").width());

                    $('.device_code_name').removeAttr("disabled");
                    $('.device_code_name').text("当前设备编号 -- " + ret.data.deviceCode);

                }, "admin/face");
            }
        });
    }

    //设置图片的高度
    $(window).resize(function () {
        $("#Images li img").height($("#Images li img").width());
    });

    // var imgNums = 15;  //单页显示图片数量
    // flow.load({
    //     elem: '#Images', //流加载容器
    //     done: function (page, next) { //加载下一页
    //         $.get("static/admin/json/images.json", function (res) {
    //             //模拟插入
    //             var imgList = [], data = res.data;
    //             var maxPage = imgNums * page < data.length ? imgNums * page : data.length;
    //             setTimeout(function () {
    //                 for (var i = imgNums * (page - 1); i < maxPage; i++) {
    //                     imgList.push(
    //                         '<li>' +
    //                         '<img layer-src="static/admin/' + data[i].src + '" src="static/admin/' + data[i].thumb + '" alt="' + data[i].alt + '">' +
    //                         '<div class="operate"><div class="check"><input type="checkbox" name="belle" lay-filter="choose" lay-skin="primary" title="' + data[i].alt + '"></div>' +
    //                         '<i class="layui-icon img_del">&#xe640;</i>' +
    //                         '</div>' +
    //                         '</li>'
    //                     );
    //                 }
    //                 next(imgList.join(''), page < (data.length / imgNums));
    //                 form.render();
    //             }, 500);
    //         });
    //     }
    // });

    //多图片上传
    // upload.render({
    //     elem: '.uploadNewImg',
    //     url: 'static/admin/json/userface.json',
    //     multiple: true,
    //     before: function(obj){
    //         //预读本地文件示例，不支持ie8
    //         obj.preview(function(index, file, result){
    //             $('#Images').prepend('<li><img layer-src="'+ result +'" src="'+ result +'" alt="'+ file.name +'" class="layui-upload-img"><div class="operate"><div class="check"><input type="checkbox" name="belle" lay-filter="choose" lay-skin="primary" title="'+file.name+'"></div><i class="layui-icon img_del">&#xe640;</i></div></li>')
    //             //设置图片的高度
    //             $("#Images li img").height($("#Images li img").width());
    //             form.render("checkbox");
    //         });
    //     },
    //     done: function(res){
    //         //上传完毕
    //     }
    // });

    //显示图 弹出层
    $("body").on("click", "#Images img", function () {
        $(this).imgZoom();
    });

    //删除单张图片
    $("body").on("click", ".img_del", function () {
        var _this = $(this);
        layer.confirm('确定删除图片"' + _this.siblings().find("input").attr("title") + '"吗？', {
            icon: 3,
            title: '提示信息'
        }, function (index) {

            Load = layer.msg('加载中...', {icon: 16, time: false, shade: 0.8});

            _this.parents("li").hide(1000);

            var faceCode = _this.parents("li").find("img").attr("face-id");
            var deviceCode = _this.parents("li").find("img").attr("face-device");

            $.api("removeDeviceFace", {faceCode:faceCode, deviceCode:deviceCode}, function (ret) {

                layer.close(Load);

                layer.msg(ret.message);

                if (ret.status === "SUCCESS") {

                    _this.parents("li").hide(1000);

                    setTimeout(function () {
                        _this.parents("li").remove();
                    }, 950);

                }

            }, "admin/face");

            layer.close(index);
        });
    });

    //全选
    form.on('checkbox(selectAll)', function (data) {
        var child = $("#Images li input[type='checkbox']");
        child.each(function (index, item) {
            item.checked = data.elem.checked;
        });
        form.render('checkbox');
    });

    //通过判断是否全部选中来确定全选按钮是否选中
    form.on("checkbox(choose)", function (data) {
        var child = $(data.elem).parents('#Images').find('li input[type="checkbox"]');
        var childChecked = $(data.elem).parents('#Images').find('li input[type="checkbox"]:checked');
        if (childChecked.length == child.length) {
            $(data.elem).parents('#Images').siblings("blockquote").find('input#selectAll').get(0).checked = true;
        } else {
            $(data.elem).parents('#Images').siblings("blockquote").find('input#selectAll').get(0).checked = false;
        }
        form.render('checkbox');
    })

    //批量复制
    function batchCopyFaceToDevice(value) {
        var $checkbox = $('#Images li input[type="checkbox"]');
        var $checked = $('#Images li input[type="checkbox"]:checked');

        if ($checkbox.is(":checked")) {
            layer.confirm('确定复制选中的图片？', {icon: 3, title: '提示信息'}, function (index) {

                var index = layer.msg('复制中，请稍候', {icon: 16, time: false, shade: 0.8});
                var faceIdList = [];
                var deviceCode = $('.device_code_name').text().split(' ')[2];

                if (parseInt(deviceCode) === value) {
                    layer.msg("不能复制到原来设备", {anim: 6});
                    return;
                }

                $checked.each(function () {
                    faceIdList.push($(this).attr('face-id'));
                });

                console.log(faceIdList);

                $.api("batchCopyToDevice", {
                    deviceCode:deviceCode,
                    toDeviceCode:value,
                    faceIdList:faceIdList
                }, function (ret) {

                    $('#Images li input[type="checkbox"],#selectAll').prop("checked", false);
                    form.render();
                    layer.close(index);
                    layer.msg(ret.message);

                    if (ret.status === 'SUCCESS') {
                        
                        if (ret.data.errorFaceList.length === 0 && ret.data.repeatFaceList.length === 0) {
                            layer.msg(ret.message);
                            return;
                        }

                        var errorFaceId = '';

                        if (ret.data.errorFaceList.length > 0) {

                            $.each(ret.data.errorFaceList, function (index, item) {
                                errorFaceId += '操作失败Id：' + item + ', ';
                            });

                        }

                        if (ret.data.repeatFaceList.length > 0) {
                            $.each(ret.data.repeatFaceList, function (index, item) {
                                errorFaceId += '\n已存在的Id：' + item + ', ';
                            });
                        }

                        layer.open({
                            title: '系统提示'
                            ,content: errorFaceId
                        });
                    } else {
                        layer.msg(ret.message);    
                    }

                }, "admin/face");
            })
        } else {
            layer.msg("请选择需要复制的图片");
        }
    }

    //批量删除
    $(".batchDel").click(function () {

        var $checkbox = $('#Images li input[type="checkbox"]');
        var $checked = $('#Images li input[type="checkbox"]:checked');

        if ($checkbox.is(":checked")) {
            layer.confirm('确定删除选中的图片？', {icon: 3, title: '提示信息'}, function (index) {

                var index = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
                var faceIdList = [];

                $checked.each(function () {
                    faceIdList.push($(this).attr('face-id'));
                });

                console.log(faceIdList);

                $.api("batchRemoveDeviceFace", {deviceCode: $('.device_code_name').text().split(' ')[2], faceIdList:faceIdList}, function (ret) {

                    $('#Images li input[type="checkbox"],#selectAll').prop("checked", false);
                    form.render();
                    layer.close(index);
                    layer.msg(ret.message);

                    if (ret.status === 'SUCCESS') {

                        var retFaceIdList = ret.data; //最小长度为1
                        var errorFaceIdList = []; //操作失败faceIdList

                        //部分操作失败
                        if (retFaceIdList.length > 1) {

                            $.forEach(retFaceIdList, function (index, item) {

                                if (index > 0) {
                                    errorFaceIdList.push(item);
                                }
                            })

                        }

                        console.log(errorFaceIdList);

                        $checked.each(function (index, item) {

                            if (errorFaceIdList.length >= 1 && errorFaceIdList[index] === $(this).attr("face-id")) return true; //跳出

                            $(this).parents("li").hide(1000);

                            setTimeout(function () {

                                $(this).parents("li").remove();
                            }, 950);

                        });
                    }

                }, "admin/face");
            })
        } else {
            layer.msg("请选择需要删除的图片");
        }
    })

});