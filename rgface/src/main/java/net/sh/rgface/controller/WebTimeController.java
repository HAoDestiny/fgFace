package net.sh.rgface.controller;

import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.util.DateTimeUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by DESTINY on 2018/4/2.
 */

@RestController
@RequestMapping(value = "/api/webTime")
public class WebTimeController {

    @RequestMapping(value = "/getWebTime")
    public ResultPo getWebTime() throws ControllerException {

        ResultPo resultPo = new ResultPo();

        String webTime = DateTimeUtil.getWebsiteDatetime("http://www.baidu.com", null);

        if (webTime == null || "".equals(webTime)) {
            throw new ControllerException("获取网络时间失败");
        }

        resultPo.setMessage("获取成功");
        resultPo.setStatus("SUCCESS");
        resultPo.setData(webTime);

        return resultPo;
    }
}
