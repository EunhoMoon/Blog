package com.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    // SSR -> jsp, thymeleaf, mustache, freemarker -> html rendering
    // SPA -> view(vue + SSR = nuxt.js), react(react + SSR = next.js)
        // -> javascript + <-> API (JSON)

    @GetMapping("/posts")
    public String get() {
        return "Hello World";
    }

}
