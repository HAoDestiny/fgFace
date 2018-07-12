package net.sh.rgface.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by DESTINY on 2018/4/27.
 */

@Controller
@RequestMapping(value = "/admin", method = RequestMethod.GET)
public class AdminMobileController {

    @RequestMapping("/")
    public String faceVideo() {
        return "admin/login";
    }

    @RequestMapping("/{forward}")
    public String reveal(@PathVariable String forward) {
        return "admin/" + forward;
    }
}
