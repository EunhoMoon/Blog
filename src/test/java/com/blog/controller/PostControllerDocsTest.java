package com.blog.controller;

import com.blog.domain.Post;
import com.blog.repository.PostRepository;
import com.blog.request.PostCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.blog", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("글 단건 조회")
    void test1() throws Exception {
        // given
        Post post = Post.builder()
                .title("제목 입니다.")
                .content("내용 입니다.")
                .build();
        postRepository.save(post);

        // expect
        mockMvc.perform(get("/posts/{postId}", 1L).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-inquiry",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용")
                        )
                ));
    }

    @Test
    @DisplayName("글 등록")
    void test2() throws Exception {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목 입니다.")
                .content("내용 입니다.")
                .build();
        String json = mapper.writeValueAsString(postCreate);

        // expect
        mockMvc.perform(post("/posts")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용")
                        )
                ));
    }

}
