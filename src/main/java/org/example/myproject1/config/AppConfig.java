package org.example.myproject1.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.example.myproject1", "org.example.*"})

public class AppConfig {

}
