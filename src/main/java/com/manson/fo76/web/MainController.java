package com.manson.fo76.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping(value = "/")
	public String index() {
		return "index.html";
	}

	@RequestMapping(value = "/temp")
	public String temp() {
		return "temp.html";
	}

}
