package com.cg.zz.test.love;

import com.cg.zz.config.LoveAiApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class LoveAPPTest {

    @Resource
    private LoveAiApp loveAiApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员小风";
        String answer = loveAiApp.doChat(message, chatId);
        // 第二轮
        message = "我想让我的女朋友小玲更爱我";
        answer = loveAiApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的女朋友叫什么来着？刚跟你说过，帮我回忆一下";
        answer = loveAiApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void testChatAgain() {
        String chatId = "78287501-70d0-4692-a366-c061d8d44772";

        String message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        String answer = loveAiApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

}
