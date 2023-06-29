package com.blog.controller;

import com.blog.domain.Post;
import com.blog.request.PostCreate;
import com.blog.request.PostEdit;
import com.blog.request.PostSearch;
import com.blog.response.PostResponse;
import com.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/foo")
    public String foo() {
        return "Foo";
    }

    @PostMapping("/posts")
    public Post post(
        @RequestBody @Valid PostCreate request,
        @RequestParam String authorization
    ) {
        if (!authorization.equals("Janek")) {
            throw new RuntimeException();
        }
        request.validate();
        return postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(
        @PathVariable Long postId,
        @RequestBody @Valid PostEdit request,
        @RequestHeader String authorization
    ) {
        if (!authorization.equals("Janek")) {
            throw new RuntimeException();
        }
        postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(
        @PathVariable Long postId,
        @RequestHeader String authorization
    ) {
        if (!authorization.equals("Janek")) {
            throw new RuntimeException();
        }
        postService.delete(postId);
    }

}
