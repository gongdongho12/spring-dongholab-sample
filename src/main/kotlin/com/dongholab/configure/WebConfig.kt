package com.dongholab.configure

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfig: WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedOrigins(
                "http://localhost:3000", "http://localhost:3001"
            )
    }

//    override fun addInterceptors(registry: InterceptorRegistry) {
//        registry.addInterceptor(())
//            .addPathPatterns("/**")
//            .excludePathPatterns("/tenants/**")
//    }
}
