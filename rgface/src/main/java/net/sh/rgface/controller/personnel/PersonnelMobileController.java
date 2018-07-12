package net.sh.rgface.controller.personnel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by DESTINY on 2018/4/27.
 */

@Controller
@RequestMapping(value = "/personnel", method = RequestMethod.GET)
public class PersonnelMobileController {

    @RequestMapping("/")
    public String faceVideo() {
        return "personnel/login";
    }

    @RequestMapping("/{forward}")
    public String reveal(@PathVariable String forward) {
        return "personnel/" + forward;
    }
}
