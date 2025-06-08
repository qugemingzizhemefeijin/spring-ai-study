package com.cg.zz.test.image.search;

import com.cg.zz.image.search.ImageSearchTool;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImageSearchToolTest {

    @Resource
    private ImageSearchTool imageSearchTool;

    @Test
    void searchImage() {
        String result = imageSearchTool.searchImage("computer");
        System.out.println(result);
    }

}
