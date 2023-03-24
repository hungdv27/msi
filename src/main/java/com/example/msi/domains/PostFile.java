package com.example.msi.domains;

import com.example.msi.models.postfile.CreatePostFileDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "post_file")
public class PostFile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "post_id", nullable = false)
  private int postId;

  @Column(name = "file_id", nullable = false)
  private int fileId;

  private PostFile(@NonNull CreatePostFileDTO target){
    postId = target.getPostId();
    fileId = target.getFileId();
  }

  public static PostFile getInstance(@NonNull CreatePostFileDTO dto){
    return new PostFile(dto);
  }
}
