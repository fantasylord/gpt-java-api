package com.lord.local.gptjavaapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.lord.local.gptjavaapi")
@EnableSwagger2
@MapperScan({"com.lord.local.gptjavaapi.dao"})
public class GptJavaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GptJavaApiApplication.class, args);
    }

}
