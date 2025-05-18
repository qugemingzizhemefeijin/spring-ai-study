package com.cg.zz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
public class StartApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(StartApplication.class, args);
  }

  @Override
  public void run(String... args) {
    log.info("服务已启动");
  }

}
