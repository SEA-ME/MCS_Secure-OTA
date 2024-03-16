package com.site.ota;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {


    @GetMapping("/apply")
    public String apply() {

        return "apply.html";
//        return "redirect:/question/list";
    }

}