package com.blog.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @Lob
  private String content;

  @Builder
  public Post(String title, String content) {
    this.title = title;
    this.content = content;
  }

  public PostEditor.PostEditorBuilder toEditor() {
    return PostEditor.builder()
        .title(title)
        .content(content);
  }

  public void edit(PostEditor editor) {
    title = editor.getTitle();
    content = editor.getContent();
  }
}
