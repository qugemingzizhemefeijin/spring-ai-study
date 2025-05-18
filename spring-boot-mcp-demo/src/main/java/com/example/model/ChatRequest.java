package com.example.model;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * 聊天请求模型，包含用户发送的消息
 */
@Data
public class ChatRequest implements Serializable {

  // 回话ID，不同的ID会话记忆是隔离的
  private String requestId;

  @Getter
  private String message;

  private String context;

  public ChatRequest() {
  }

  public ChatRequest(String message) {
    this.message = message;
  }

}
