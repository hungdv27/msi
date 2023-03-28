package com.example.msi.controller;

import com.example.msi.models.post.CreatePostDTO;
import com.example.msi.models.post.PostDTO;
import com.example.msi.models.post.UpdatePostDTO;
import com.example.msi.service.PostService;
import com.example.msi.shared.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.msi.service.impl.CompanyServiceImpl.getPageable;

@RequiredArgsConstructor
@RequestMapping("api/post")
@RestController
public class PostController {
  private final PostService service;

  @GetMapping
  public ResponseEntity<Page<PostDTO>> getAll(
      Principal principal,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "5") Integer size
  ) {
    Pageable pageable = getPageable(page, size);
    Page<PostDTO> responseData = service.findAll(pageable, principal.getName()).map(PostDTO::getInstance);
    return ResponseEntity.ok(responseData);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostDTO> postById(@PathVariable int id) throws NoSuchElementException {
    var response = PostDTO.getInstance(service.findById(id));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<PostDTO> createPost(
      @RequestParam("title") String title,
      @RequestParam("applyTo") Role applyTo,
      @RequestParam("content") String content,
      @ModelAttribute("files") List<MultipartFile> files
      ) throws IOException {
    var dto = new CreatePostDTO(title, applyTo, content);
    var response = PostDTO.getInstance(service.add(dto, files));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<PostDTO> updatePost(
      @RequestParam("id") int id,
      @RequestParam("title") String title,
      @RequestParam("applyTo") Role applyTo,
      @RequestParam("content") String content,
      @ModelAttribute("files") List<MultipartFile> files) {
    var dto = new UpdatePostDTO(id, title, applyTo,content, files);
    var response = service.update(dto).map(PostDTO::getInstance).orElseThrow(NoSuchElementException::new);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Object> deletePost(@PathVariable int id) {
    service.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
