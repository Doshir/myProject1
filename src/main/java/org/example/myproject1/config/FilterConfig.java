package org.example.myproject1.config;

import org.example.myproject1.filter.RequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Value("${request.throttle.maxRequestsPerMinute}")
    private int maxRequestsPerMinute;

    @Value("${request.throttle.timeFrameInMillis}")
    private long timeFrameInMillis;

    @Bean
    public FilterRegistrationBean<RequestFilter> requestThrottleFilter() {
        FilterRegistrationBean<RequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestFilter(maxRequestsPerMinute,timeFrameInMillis));
        registrationBean.addUrlPatterns("/info/*");
        return registrationBean;
    }
}