package net.sh.rgface.exception;

import net.sh.rgface.po.base.ResultPo;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by DESTINY on 2017/10/18.
 */

@Controller
public class NotFoundException implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public ResultPo handleError(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if(httpServletRequest.getMethod().equals("GET")) {

            httpServletResponse.sendRedirect("/sh/");
            return null;
        }

        ResultPo resultPo = new ResultPo();
        resultPo.setStatus("NOT_FOUND_SERVICE");
        return resultPo;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
