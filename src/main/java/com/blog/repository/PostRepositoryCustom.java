package com.blog.repository;

import com.blog.domain.Post;
import com.blog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);

}
