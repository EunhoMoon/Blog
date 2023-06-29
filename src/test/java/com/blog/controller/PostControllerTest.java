package com.blog.controller;

import com.blog.domain.Post;
import com.blog.repository.PostRepository;
import com.blog.request.PostCreate;
import com.blog.request.PostEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("/post 정상 요청")
    void test() throws Exception {
        // given
        var request = PostCreate.builder()
            .title("제목")
            .content("내용")
            .build();

        String json = mapper.writeValueAsString(request);  // json 가공

        mockMvc.perform(post("/posts")
                .header("authorization", "Janek")
                .contentType(APPLICATION_JSON)
                .content(json))   // application/json
            .andExpect(status().isOk())
            .andDo(print());  // Http 요청에 대한 정보 출력
    }

    @Test
    @DisplayName("/post 요청시 title과 content값 필수")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder().title("").build();
        String json = mapper.writeValueAsString(request);  // json 가공

        mockMvc.perform(post("/posts")
                .contentType(APPLICATION_JSON)
                .content(json)
            )   // application/json
            .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.title").value("제목을 입력해주세요."))
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validation.title").value("제목을 입력해주세요."))
            .andExpect(jsonPath("$.validation.content").value("내용을 입력해주세요."))
            .andDo(print());  // Http 요청에 대한 정보 출력
    }

    @Test
    @DisplayName("/post 요청시 DB에 값 저장")
    void test3() throws Exception {
        // given
        PostCreate request = PostCreate.builder().title("제목 입니다.").content("내용 입니다.").build();
        String json = mapper.writeValueAsString(request);  // json 가공

        // when
        mockMvc.perform(post("/posts")
                .contentType(APPLICATION_JSON)
                .content(json)
            )
            .andExpect(status().isOk())
            .andDo(print());

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목 입니다.", post.getTitle());
        assertEquals("내용 입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 단건 조회")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
            .title("foo")
            .content("bar")
            .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/" + post.getId())
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(post.getId()))
            .andExpect(jsonPath("$.title").value(post.getTitle()))
            .andExpect(jsonPath("$.content").value(post.getContent()))
            .andDo(print());
    }

    @Test
    @DisplayName("글 목록 조회")
    void test5() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
            .mapToObj(i -> Post.builder()
                .title("테스트 제목 " + i)
                .content("테스트 내용 " + i)
                .build())
            .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=1&size=10")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(10)))
            .andExpect(jsonPath("$.[0].id").value(requestPosts.get(29).getId()))
            .andExpect(jsonPath("$.[0].title").value("테스트 제목 30"))
            .andExpect(jsonPath("$.[0].content").value("테스트 내용 30"))
            .andExpect(jsonPath("$.[4].id").value(requestPosts.get(25).getId()))
            .andExpect(jsonPath("$.[4].title").value("테스트 제목 26"))
            .andExpect(jsonPath("$.[4].content").value("테스트 내용 26"))
            .andDo(print());
    }


    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test6() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
            .mapToObj(i -> Post.builder()
                .title("테스트 제목 " + i)
                .content("테스트 내용 " + i)
                .build())
            .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=0&size=10")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(10)))
            .andExpect(jsonPath("$.[0].id").value(requestPosts.get(29).getId()))
            .andExpect(jsonPath("$.[0].title").value("테스트 제목 30"))
            .andExpect(jsonPath("$.[0].content").value("테스트 내용 30"))
            .andExpect(jsonPath("$.[4].id").value(requestPosts.get(25).getId()))
            .andExpect(jsonPath("$.[4].title").value("테스트 제목 26"))
            .andExpect(jsonPath("$.[4].content").value("테스트 내용 26"))
            .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
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

        // expected
        mockMvc.perform(patch("/posts/" + post.getId())
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(postEdit)))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("글 삭제")
    void test8() throws Exception {
        // given
        Post post = Post.builder()
            .title("테스트 제목")
            .content("테스트 내용")
            .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/posts/" + post.getId())
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception {
        mockMvc.perform(get("/posts/" + 123L)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception {
        PostEdit postEdit = PostEdit.builder()
            .title("테스트 제목 수정")
            .content("테스트 내용")
            .build();

        mockMvc.perform(patch("/posts/" + 123L)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(postEdit)))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성시 '바보'는 포함될 수 없다.")
    void test11() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
            .title("나는 바보입니다.")
            .content("내용")
            .build();

        // expected
        mockMvc.perform(post("/posts")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

}