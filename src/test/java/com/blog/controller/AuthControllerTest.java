package com.blog.controller;

import com.blog.domain.User;
import com.blog.repository.SessionRepository;
import com.blog.repository.UserRepository;
import com.blog.request.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test() throws Exception {
        // given
        User user = User.builder()
            .name("tester")
            .email("test@gmail.com")
            .password("1234")
            .build();

        userRepository.save(user);

        var request = Login.builder()
            .email("test@gmail.com")
            .password("1234")
            .build();

        String json = objectMapper.writeValueAsString(request);  // json 가공

        mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(json))   // application/json
            .andExpect(status().isOk())
            .andDo(print());  // Http 요청에 대한 정보 출력
    }

    @Test
    @DisplayName("로그인 성공 후 세션 1개 생성")
    void test2() throws Exception {
        // given
        var user = User.builder()
            .name("tester")
            .email("test@gmail.com")
            .password("1234")
            .build();

        userRepository.save(user);

        var request = Login.builder()
            .email("test@gmail.com")
            .password("1234")
            .build();

        String json = objectMapper.writeValueAsString(request);  // json 가공

        mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andDo(print());

        var totalSessionCount = sessionRepository.count();
        var loggedInUser = userRepository.findById(user.getId())
            .orElseThrow(RuntimeException::new);

        assertEquals(1, totalSessionCount);
        assertEquals(1, loggedInUser.getSessions().size());
    }

    @Test
    @DisplayName("로그인 성공 후 세션 응답")
    void test3() throws Exception {
        // given
        var user = User.builder()
            .name("tester")
            .email("test@gmail.com")
            .password("1234")
            .build();

        userRepository.save(user);

        var request = Login.builder()
            .email("test@gmail.com")
            .password("1234")
            .build();

        String json = objectMapper.writeValueAsString(request);  // json 가공

        mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").isNotEmpty())
            .andDo(print());
    }

}