package net.sh.rgface.controller.user;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by DESTINY on 2018/3/30.
 */
@Controller
@RequestMapping(value = "/sh", method = RequestMethod.GET)
public class MobileController {

    @RequestMapping("/")
    public String faceVideo() {
        return "login";
    }

    @RequestMapping("/{forward}")
    public String reveal(@PathVariable String forward) {
        return forward;
    }

}
