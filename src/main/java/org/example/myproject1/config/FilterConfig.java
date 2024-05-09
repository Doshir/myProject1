package org.example.myproject1.config;

import org.example.myproject1.filter.RequestThrottleFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RequestThrottleFilter> requestThrottleFilter() {
        FilterRegistrationBean<RequestThrottleFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestThrottleFilter());
        registrationBean.addUrlPatterns("/info/*");
        return registrationBean;
    }
}