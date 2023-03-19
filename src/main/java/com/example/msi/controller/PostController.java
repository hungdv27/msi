package com.example.msi.controller;

import com.example.msi.models.post.CreatePostDTO;
import com.example.msi.models.post.PostDTO;
import com.example.msi.models.post.UpdatePostDTO;
import com.example.msi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static com.example.msi.service.impl.CompanyServiceImpl.getPageable;

@RequiredArgsConstructor
@RequestMapping("api/post")
@RestController
public class PostController {
  private final PostService service;

  @GetMapping
  public ResponseEntity<Page<PostDTO>> getAll(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "5") Integer size
  ) {
    Pageable pageable = getPageable(page, size);
    Page<PostDTO> page1 = service.findAll(pageable).map(PostDTO::getInstance);
    return ResponseEntity.ok(page1);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostDTO> postById(@PathVariable int id) throws NoSuchElementException{
    var response = PostDTO.getInstance(service.findById(id));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<PostDTO> createPost(@RequestBody @NonNull CreatePostDTO dto) {
    var response = PostDTO.getInstance(service.add(dto));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<PostDTO> updatePost(@RequestBody @NonNull UpdatePostDTO dto) {
    var response = service.update(dto).map(PostDTO::getInstance).orElseThrow(NoSuchElementException::new);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Object> deletePost(@PathVariable int id) {
    service.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
