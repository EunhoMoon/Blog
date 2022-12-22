package com.blog.service;

import com.blog.domain.Post;
import com.blog.repository.PostRepository;
import com.blog.request.PostCreate;
import com.blog.response.PostResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();

        // when
        Post post = postService.write(postCreate);
        log.info("post = {}", post);

        // then
        assertThat(postRepository.count()).isEqualTo(1L);
        assertThat(post.getTitle()).isEqualTo(postCreate.getTitle());
        assertThat(post.getContent()).isEqualTo(postCreate.getContent());
    }

    @Test
    @DisplayName("글 단건 조회")
    void test() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();
        Post savePost = postService.write(postCreate);
        Long postId = savePost.getId();

        // when
        PostResponse postResponse = postService.get(postId);
        log.info("findPost = {}", postResponse);

        // then
        assertThat(postRepository.count()).isEqualTo(1L);
        assertThat(postResponse.getTitle()).isEqualTo(postCreate.getTitle());
        assertThat(postResponse.getContent()).isEqualTo(postCreate.getContent());
    }

}