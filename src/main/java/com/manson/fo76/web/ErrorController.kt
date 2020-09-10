package com.manson.fo76.web

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import springfox.documentation.annotations.ApiIgnore
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

@ApiIgnore
@Controller
class ErrorController : ErrorController {
    @RequestMapping("/error")
    fun error(request: HttpServletRequest): String {
        println(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE))
        return "error.html"
    }

    override fun getErrorPath(): String? {
        return null
    }
}