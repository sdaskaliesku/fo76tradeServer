package com.manson.fo76.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

	@RequestMapping("/error")
	public String error(HttpServletRequest request) {
		System.out.println(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
		return "error.html";
	}

	@Override
	public String getErrorPath() {
		return null;
	}
}
