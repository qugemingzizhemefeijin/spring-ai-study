package com.example.mcp.service;

import com.example.model.ChatRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ChatService {

    @Resource
    private ChatClient chatClient;

    @Resource
    private OpenAiChatModel openAiChatModel;

    // 用于保存多轮历史对话记录
    private final List<Message> conversationHistory = new ArrayList<>();

    // 简单对话，情景模式
    public String chat(ChatRequest request) {
        // 创建用户消息
        String userMessage = request.getMessage();

        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content()
                .trim();
    }

    // 简单单轮对话
    public String chatWithSimple(ChatRequest request) {
        return openAiChatModel.call(new Prompt(request.getMessage())).getResult().getOutput().getText();
    }

    // 上下文对话
    public String chatWithContext(ChatRequest request) {
        String template = """
                 你是一个专业的AI助手，请根据以下上下文回答问题：
                 {context}
                """;

        log.info(template);

        // 带上下文的对话
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(template);

        Message systemMessage = systemPromptTemplate.createMessage(Map.of("context", request.getContext()));

        UserMessage userMessage = new UserMessage(request.getMessage());
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        return openAiChatModel.call(prompt).getResult().getOutput().getText();
    }

    // 多轮对话（感觉像智障一样，是我使用错了？）
    public String multiTurnChat(ChatRequest request) {
        log.info(conversationHistory.toString());
        // 添加用户消息到历史
        conversationHistory.add(new UserMessage(request.getMessage()));
        // 多轮对话
        Prompt prompt = new Prompt(conversationHistory);
        String response = openAiChatModel.call(prompt).getResult().getOutput().getText();

        // 添加回复到历史
        conversationHistory.add(new AssistantMessage(response));
        log.info(conversationHistory.toString());

        return response;
    }

    /**
     * 流式对话
     *
     * @param message
     * @return
     */
    public Flux<String> chatWithStream(String message) {
        return openAiChatModel.stream(message);
    }

    // 使用姿势有问题？
    public SseEmitterUTF8 chatWithSSE(ChatRequest request) {
        log.info(request.getMessage());

        SseEmitterUTF8 emitter = new SseEmitterUTF8(5000L);

        openAiChatModel.stream(new Prompt(request.getMessage()))
                .subscribe(
                        chunk -> {
                            try {
                                emitter.send(chunk.getResult().getOutput().getText());
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        emitter::completeWithError,
                        emitter::complete
                );

        return emitter;
    }

}
