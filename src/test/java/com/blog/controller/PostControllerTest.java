package com.blog.controller;

import com.blog.domain.Post;
import com.blog.repository.PostRepository;
import com.blog.request.PostCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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

    @Test
    @DisplayName("/post 정상 요청")
    void test() throws Exception {
        // given
        PostCreate request = PostCreate.builder().title("제목").content("내용").build();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);  // json 가공

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )   // application/json
                .andExpect(status().isOk())
                .andDo(print());  // Http 요청에 대한 정보 출력
    }

    @Test
    @DisplayName("/post 요청시 title과 content값 필수")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder().title("").build();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);  // json 가공

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
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
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);  // json 가공

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
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
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/" + post.getId())
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
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&sort=id,desc")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$.[0].id").value(30L))
                .andExpect(jsonPath("$.[0].title").value("테스트 제목 30"))
                .andExpect(jsonPath("$.[0].content").value("테스트 내용 30"))
                .andExpect(jsonPath("$.[4].id").value(26L))
                .andExpect(jsonPath("$.[4].title").value("테스트 제목 26"))
                .andExpect(jsonPath("$.[4].content").value("테스트 내용 26"))
                .andDo(print());
    }
}