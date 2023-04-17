package com.example.msi.models.result;

import com.example.msi.domains.Result;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class ResultDTO {
  private int processId;
  private int mark;
  private String review;

  private ResultDTO(@NonNull Result target) {
    processId = target.getProcessId();
    mark = target.getMark();
    review = target.getReview();
  }

  public static ResultDTO getInstance(@NonNull Result entity) {
    return new ResultDTO(entity);
  }
}
