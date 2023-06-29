package com.blog.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.*;

@Getter
@Setter
@Builder
public class PostSearch {

  private final int MAX_SIZE = 2000;

  @Builder.Default
  private Integer page = 1;

  @Builder.Default
  private Integer size = 10;

  public long getOffset() {
    return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
  }

}
