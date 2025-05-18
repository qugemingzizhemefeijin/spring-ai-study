package com.example.controller;

import com.example.mcp.service.ChatMemoryService;
import com.example.mcp.service.ChatService;
import com.example.mcp.service.SseEmitterUTF8;
import com.example.model.ChatRequest;
import com.example.model.ChatResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * 聊天控制器，处理AI聊天请求
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ChatController {

    @Resource
    private ChatService chatService;

    @Resource
    private ChatMemoryService chatMemoryService;

    /**
     * 处理聊天请求，使用AI和MCP工具进行响应（简单对话，情景模式，Function CALL）
     *
     * @param request 聊天请求
     * @return 包含AI回复的响应
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        try {
            String content = chatService.chat(request);

            return ResponseEntity.ok(new ChatResponse(content));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.ok(new ChatResponse("处理请求时出错: " + e.getMessage()));
        }
    }

    // 简单单轮对话
    @PostMapping("/with-simple")
    public ResponseEntity<ChatResponse> chatWithSimple(@RequestBody ChatRequest request) {
        try {
            String content = chatService.chatWithSimple(request);

            return ResponseEntity.ok(new ChatResponse(content));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.ok(new ChatResponse("处理请求时出错: " + e.getMessage()));
        }
    }

    /**
     * 上下文聊天
     *
     * @param request 聊天请求
     * @return 包含AI回复的响应
     */
    @PostMapping("/with-context")
    public ResponseEntity<ChatResponse> chatWithContext(@RequestBody ChatRequest request) {
        try {
            String content = chatService.chatWithContext(request);

            return ResponseEntity.ok(new ChatResponse(content));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.ok(new ChatResponse("处理请求时出错: " + e.getMessage()));
        }
    }

    /**
     * 多轮对话
     *
     * @param request 聊天请求
     * @return 包含AI回复的响应
     */
    @PostMapping("/multi-turn")
    public ResponseEntity<ChatResponse> multiTurnChat(@RequestBody ChatRequest request) {
        try {
            String content = chatService.multiTurnChat(request);

            return ResponseEntity.ok(new ChatResponse(content));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.ok(new ChatResponse("处理请求时出错: " + e.getMessage()));
        }
    }

    /**
     * 流式对话
     *
     * @param request 聊天请求
     * @return 包含AI回复的响应
     */
    @PostMapping("/with-stream")
    public Flux<String> chatWithStream(@RequestBody ChatRequest request) {
        return chatService.chatWithStream(request.getMessage())
                .doOnNext(System.out::println)
                .delayElements(Duration.ofMillis(500)) // 设置流速
                .doOnComplete(() -> System.out.println("Flux 对话结束"));
    }

    /**
     * SSE实现
     *
     * @param request 聊天请求
     * @return 包含AI回复的响应
     */
    @PostMapping(value = "/with-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitterUTF8 chatWithSSE(@RequestBody ChatRequest request) {
        return chatService.chatWithSSE(request);
    }

    /**
     * 支持记忆的聊天
     *
     * @param request 聊天请求
     * @return 包含AI回复的响应
     */
    @PostMapping(value = "/with-memory")
    public Flux<String> chatWithMemoryStream(@RequestBody ChatRequest request) {
        return chatMemoryService.chatWithMemoryStream(request.getRequestId(), request.getMessage());
    }

}
