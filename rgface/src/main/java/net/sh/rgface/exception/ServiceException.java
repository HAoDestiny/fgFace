package net.sh.rgface.exception;

import net.sh.rgface.po.base.ResultPo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ServiceException {

//    /**
//     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
//     *
//     * @param binder
//     */
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//    }
//
//    /**
//     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
//     *
//     * @param model
//     */
//    @ModelAttribute
//    public void addAttributes(Model model) {
//        model.addAttribute("author", "Magical Sam");
//    }

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = ControllerException.class)
    public ResultPo errorHandler(ControllerException ex) {
        ResultPo resultPo = new ResultPo();
        resultPo.setMessage(ex.getMessage());
        resultPo.setStatus(ex.getStatus());
        return resultPo;
    }

}
