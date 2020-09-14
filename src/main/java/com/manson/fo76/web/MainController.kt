package com.manson.fo76.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.RedirectView
import springfox.documentation.annotations.ApiIgnore

@ApiIgnore
@Controller
class MainController {
    @RequestMapping(value = ["/"])
    fun index(): String {
        return "index.html"
    }

    @RequestMapping(value = ["/temp"])
    fun temp(): RedirectView {
        return RedirectView("/")
    }
}