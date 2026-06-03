package com.acrobtw.elei.core.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topics.level-up}")
    private String levelUpTopic;

    @Bean
    public NewTopic levelUpTopic() {
        return TopicBuilder.name(levelUpTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic levelUpDlt() {
        return TopicBuilder.name(levelUpTopic + ".DLT")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
