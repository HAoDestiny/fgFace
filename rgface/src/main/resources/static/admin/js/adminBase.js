!(function () {

    var sh = window.sh || {};

    //设定基本框架
    sh = {
        _INSTALL: function () {
            window.sh = sh;
        },
        TOOL: {}, //工具
        UI: {}, 	//前端显示层,用来重构和回流DOM,前端的特效显示处理
        Page: {}		//用户页面层,用来做一些页面上特有的功能
    };

    sh.TOOL = {
        timeTool: {
            timestampToTime: function (timestamp) {
                var date = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
                var Y = date.getFullYear() + '-';
                var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
                var D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ';
                var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
                var m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
                var s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
                return Y + M + D + h + m + s;
            },
            stringDateFormatTimestamps(string) {
                return Date.parse(new Date(string)) / 1000;
            }
        }
    };

    sh.UI = {
        TIPS: {
            tip: function (msg) {
                layer.msg(msg)
            }
        }
    }

    sh._INSTALL();

})();