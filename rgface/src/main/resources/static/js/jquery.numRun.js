(function ($) {
    /*jQuery对象添加  runNum  方法*/

    var valString;
    var par;
    var runNumJson;
    var num;

    $.fn.extend({
        /*
            *	滚动数字
            *	@ val 值，	params 参数对象
            *	params{addMin(随机最小值),addMax(随机最大值),interval(动画间隔),speed(动画滚动速度),width(列宽),height(行高)}
        */
        runNum:function (val,params) {
            /*初始化动画参数*/
            valString = val || '00000000'
            num = valString;
            par = params || {};
            runNumJson = {
                el:$(this),
                value:valString,
                valueStr:valString.toString(10),
                width:par.width || 40,
                height:par.height || 50,
                addMin:par.addMin || 1,
                addMax:par.addMax || 1,
                interval:par.interval || 3000,
                speed:par.speed || 500,
                width:par.width || 40,
                length:valString.toString(10).length
            };
            $._runNum._list(runNumJson.el,runNumJson); //渲染
            //$._runNum._interval(runNumJson.el.children("li"),runNumJson); //随机自动
        },

        /*手动添加*/
        add:function(vals) {
            valNum = vals || 1;
            $._runNum._addNum(runNumJson.el.children("li"),runNumJson, valNum);
        },

        /*手动减少*/
        reduce:function(vals) {
            valNum = vals || 1;
            $._runNum._reduce(runNumJson.el.children("li"),runNumJson, valNum);
        },

        prefixInteger: function (vals) {
            return $._runNum._PrefixInteger(vals, 8);
        }
    });
    /*jQuery对象添加  _runNum  属性*/
    $._runNum={

        /*初始化数字列表*/
        _list:function (el,json) {
            var str='';
            for(var i=0; i<json.length;i++) {
                var w=json.width*i;
                var t=json.height*parseInt(json.valueStr[i]);
                var h=json.height*10;
                str +='<li style="width:'+json.width+'px;left:'+w+'px;top: '+-t+'px;height:'+h+'px;">';
                for(var j=0;j<10;j++){
                    str+='<div style="height:'+json.height+'px;line-height:'+json.height+'px;">'+j+'</div>';
                }
                str+='</li>';
            }
            el.html(str);
        },

        //手动添加每次添加vals
        _addNum: function(el,json,vals) {
            var val = parseInt(json.value) + vals;

            json.value = val; //赋值

            val = $._runNum._PrefixInteger(val, 8);   //补零

            $._runNum._animate(el,val.toString(10),json); //渲染
        },

        //手动减少每次添加vals
        _reduce: function(el,json,vals) {

            var val = parseInt(json.value) - vals;

            if (val < 0) {
                val = 0;
            }

            json.value = val; //赋值

            val = $._runNum._PrefixInteger(val, 8);   //补零

            $._runNum._animate(el,val.toString(10),json); //渲染
        },

        /*生成随即数*/
        _random:function (json) {
            var Range = json.addMax - json.addMin;
            var Rand = Math.random();
            var num=json.addMin + Math.round(Rand * Range);
            return num;
        },

        /*执行动画效果*/
        _animate:function (el,value,json) {
            for(var x=0;x<json.length;x++){
                var topPx=value[x]*json.height;
                el.eq(x).animate({top:-topPx+'px'},json.speed);
            }
        },

        /*定期刷新动画列表*/
        _interval:function (el,json) {
            var val=json.value;
            setInterval(function () {
                val+=$._runNum._random(json);
                $._runNum._animate(el,val.toString(10),json);
            },json.interval);
        },

        //补零
        _PrefixInteger: function(num, length) {
            return ( "0000000000000000" + num ).substr( -length );
        }
    }
})(jQuery);