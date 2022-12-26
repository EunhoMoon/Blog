package com.blog.service;

import com.blog.domain.Post;
import com.blog.exception.PostNotFound;
import com.blog.repository.PostRepository;
import com.blog.request.PostCreate;
import com.blog.request.PostEdit;
import com.blog.request.PostSearch;
import com.blog.response.PostResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void test2() {
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

    @Test
    @DisplayName("글 단건 조회 - 실패")
    void test2_1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();
        Post post = postService.write(postCreate);

        // then
        assertThrows(PostNotFound.class, () -> postService.get(post.getId() + 1L));
    }

    @Test
    @DisplayName("글 목록 1페이지 조회")
    void test3() {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("테스트 제목 " + i)
                        .content("테스트 내용 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // when
        List<PostResponse> posts = postService.getList(PostSearch.builder().build());

        // then
        assertThat(postRepository.count()).isEqualTo(30L);
        assertEquals(posts.size(), 10L);
        assertEquals("테스트 제목 30", posts.get(0).getTitle());
        assertEquals("테스트 제목 26", posts.get(4).getTitle());
    }

    @Test
    @DisplayName("글 수정 - 제목")
    void test4() {
        // given
        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("테스트 제목 수정")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        assertEquals("테스트 제목 수정", changedPost.getTitle());
        assertEquals("테스트 내용", changedPost.getContent());
    }

    @Test
    @DisplayName("글 수정 - 내용")
    void test4_1() {
        // given
        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("테스트 제목 수정")
                .content("테스트 내용")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(PostNotFound::new);

        assertEquals("테스트 제목 수정", changedPost.getTitle());
        assertEquals("테스트 내용", changedPost.getContent());
    }

    @Test
    @DisplayName("글 수정 - 실패")
    void test4_2() {
        // given
        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("테스트 제목 수정")
                .content("테스트 내용")
                .build();

        // expect
        assertThrows(PostNotFound.class, () -> postService.edit(post.getId() + 1L, postEdit));
    }

    @Test
    @DisplayName("게시글 삭제")
    void test5() {
        // given
        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(postRepository.count(), 0L);
    }

    @Test
    @DisplayName("게시글 삭제 - 실패")
    void test5_1() {
        // given
        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();
        postRepository.save(post);

        // expect
        assertThrows(PostNotFound.class, () -> postService.delete(post.getId() + 1L));
    }

}