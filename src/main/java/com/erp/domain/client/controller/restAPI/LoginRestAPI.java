package com.erp.domain.client.controller.restAPI;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/client")
public class LoginRestAPI {

    // 로그인 페이지
    @PostMapping("/login")
    public String login(){
        
    }
}
