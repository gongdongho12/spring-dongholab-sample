package com.dongholab.configure

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    @Primary
    fun webClient(): WebClient {
        val webClient = WebClient.builder()
            .exchangeStrategies { builder ->
                builder.codecs {
                    it.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)
                }
            }
            .build()
        return webClient
    }
}