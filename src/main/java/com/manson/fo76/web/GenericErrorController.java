package com.manson.fo76.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
@Slf4j
public class GenericErrorController implements ErrorController {

  @RequestMapping("/error")
  public String error(HttpServletRequest request) {
    log.error("Error code: {}", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
    return "error.html";
  }

  @Override
  public String getErrorPath() {
    return null;
  }
}
