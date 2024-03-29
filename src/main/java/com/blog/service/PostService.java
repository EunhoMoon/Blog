package com.blog.service;

import com.blog.domain.Post;
import com.blog.domain.PostEditor;
import com.blog.exception.PostNotFound;
import com.blog.repository.PostRepository;
import com.blog.request.PostCreate;
import com.blog.request.PostEdit;
import com.blog.request.PostSearch;
import com.blog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

  private final PostRepository postRepository;

  public Post write(PostCreate postCreate) {
    Post post = Post.builder()
        .title(postCreate.getTitle())
        .content(postCreate.getContent())
        .build();

    return postRepository.save(post);
  }

  public PostResponse get(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    return PostResponse.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .build();
  }

  public List<PostResponse> getList(PostSearch postSearch) {
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
    return postRepository.getList(postSearch).stream()
        .map(PostResponse::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public void edit(Long postId, PostEdit postEdit) {
    Post post = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

    PostEditor editor = editorBuilder
        .title(postEdit.getTitle())
        .content(postEdit.getContent())
        .build();

    post.edit(editor);
  }

  @Transactional
  public void delete(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    postRepository.deleteById(post.getId());
  }

}
