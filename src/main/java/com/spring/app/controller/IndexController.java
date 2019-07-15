package com.spring.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping(value = "/{[path:[^\\.]*}")
    public String index(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        if (url.startsWith("/static")) {
            return String.format("forward:/%s", url);
        }
        return "redirect:/404";
    }
}
