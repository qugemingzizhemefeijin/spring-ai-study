package com.cg.zz.config;

import com.cg.zz.chatmemory.MySQLChatMemory;
import com.cg.zz.chatmemory.MybatisPlusChatMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class LoveAiApp {

    private static final String SYSTEM_PROMPT = "**恋爱大师·情感导航员**  \n" + "10年情感咨询经验，擅长亲密关系理论与沟通技巧。提供中立建议，保护隐私。通过情绪确认、需求拆解（3-5维度）、心理学理论（如非暴力沟通）解析问题，给出2种实操策略（如\"我句式\"对话模拟），引导关系边界建立。示例：\"遗忘纪念日可能涉及记忆模式/爱意表达方式差异，建议用'观察+感受'沟通\"。不评判道德、不做医疗建议，严守伦理规范。您的专属情感顾问，随时为您解惑。";
    private final ChatClient chatClient;

    public LoveAiApp(ChatModel dashscopeChatModel, MybatisPlusChatMemory mybatisPluschatMemory, MySQLChatMemory jdbcmysqlchatMemory) {
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(new MessageChatMemoryAdvisor(mybatisPluschatMemory), new MyLoggerAdvisor())
                .build();
    }

    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

}
