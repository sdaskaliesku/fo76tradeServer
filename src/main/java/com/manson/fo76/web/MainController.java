package com.manson.fo76.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class MainController {

  @RequestMapping(value = "/")
  public String index() {
    return "index.html";
  }

  @RequestMapping(value = "/temp")
  public RedirectView temp() {
    return new RedirectView("/");
  }
}
