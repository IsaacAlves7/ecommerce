package com.pos.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic saleConfirmedTopic() {
        return TopicBuilder.name("pos.sale.confirmed")
                .partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic saleCancelledTopic() {
        return TopicBuilder.name("pos.sale.cancelled")
                .partitions(3).replicas(1).build();
    }
}
