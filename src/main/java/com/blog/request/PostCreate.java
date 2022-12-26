package com.blog.request;

import com.blog.exception.InvalidRequest;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PostCreate {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if (title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 '바보'를 포함할 수 없습니다.");
        }
    }
}
