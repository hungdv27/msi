package com.example.msi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MSIException extends Exception{
  private String messageKey;
  private String message;
  private Throwable throwable;
  private List<String> messages;

  public MSIException(String msgKey) {
    this.messageKey = msgKey;
  }

  public MSIException(String msgKey, String msg) {
    this.messageKey = msgKey;
    this.message = msg;
  }

  public String getMessage() {
    if (this.message != null) {
      return message;
    }
    if (this.messageKey != null) {
      this.message = String.format(ExceptionUtils.messages.get(this.messageKey));
    }
    return null;
  }
}
