package com.boot.security.controller;

import com.boot.security.model.Account;
import com.boot.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final UserService userService;

    @RequestMapping(value = {"/", "/login"})
    public String login(Model model) {
        return "/auth/login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("account", new Account());
        return "/auth/registration";
    }

    @PostMapping("/registration")
    public String createNewUser(Model model, @Validated Account account, BindingResult bindingResult) {
        try {
            Account userExists = userService.getUserByUsername(account.getUsername());

            if (userExists != null) {
                bindingResult.rejectValue("username", "error.user", "이미 가입된 유저입니다.");
            }

            if (!account.getPassword().equals(account.getConfirmPassword())) {
                bindingResult.rejectValue("confirmPassword", "error.user", "비밀번호를 확인해주세요.");
            }

            if (bindingResult.hasErrors()) {
                log.error("[ 에러 ] :  {}", bindingResult.getFieldError().toString());
            } else {
                userService.setUser(account);
                model.addAttribute("user", new Account());
                model.addAttribute("successMessage", "회원가입에 성공하였습니다.");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("successMessage", "FAIL : " + e.getMessage());
        }
        return "/auth/registration";
    }

    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = null;
        try {
            account = userService.getUserByUsername(auth.getName());
        } catch (Exception e) {
            log.error("[ 에러 ] : {}", e.getMessage());
        }
        model.addAttribute("username", account.getUsername() + "(" + account.getEmail() + ")");
        model.addAttribute("adminMessage", "관리자 권한이 있는 사용자만 사용할 수 있습니다.");

        return "index";
    }

    @GetMapping("/home/admin")
    public String adminHome(Model model) {
        return "/home/admin";
    }

    @GetMapping("/home/user")
    public String userHome(Model model) {
        log.info("==> 호츌");
        return "/home/user";
    }

    @GetMapping("/home/guest")
    public String guestHome(Model model) {
        return "/home/guest";
    }
}
