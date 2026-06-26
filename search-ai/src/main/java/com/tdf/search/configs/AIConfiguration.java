package com.tdf.search.configs;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AIConfiguration {

    @Bean
    @Primary
    public ChatClient chatClient(ChatClient.Builder builder){
        return builder
                .defaultSystem("Be concise and answer to the point")
                .build();
    }
}

