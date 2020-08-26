package com.efm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gcc
 * @date 2020/8/24 11:50
 */
@SpringBootApplication
public class EasyFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyFileApplication.class, args);
        System.out.println("============== 项目启动成功 ==============");
    }
}
