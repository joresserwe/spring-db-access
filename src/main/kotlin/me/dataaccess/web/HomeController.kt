package me.dataaccess.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class HomeController {
    
    @RequestMapping("/")
    fun home() = "redirect:/items"
}
