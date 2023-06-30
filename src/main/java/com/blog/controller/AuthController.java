package com.blog.controller;

import com.blog.request.Login;
import com.blog.response.SessionResponse;
import com.blog.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService userService;

    @PostMapping("/login")

    public SessionResponse login(@RequestBody Login loginRequest) {
        var accessToken = userService.signIn(loginRequest);
        return new SessionResponse(accessToken);
    }

}
