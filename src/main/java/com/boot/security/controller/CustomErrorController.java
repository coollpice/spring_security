package com.boot.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class CustomErrorController implements ErrorController {

    private final String DEFAULT_ERROR_PATH = "/error";

    public String getErrorPath() {
        return DEFAULT_ERROR_PATH;
    }

    @RequestMapping("/error")
    public String errorHandle(HttpServletRequest request, Model model) {
        return errorHandleImpl(request, model);
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("errorCode", "403");
        model.addAttribute("errorMessage", "Forbidden");

        return getErrorPath()+"/error";
    }

    private String errorHandleImpl(HttpServletRequest request, Model model) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(status.toString()));

        model.addAttribute("errorCode", status.toString());
        model.addAttribute("errorMessage", httpStatus.getReasonPhrase());

        return getErrorPath() + "/error";
    }
}
