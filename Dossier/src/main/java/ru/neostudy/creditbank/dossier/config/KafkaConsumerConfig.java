package ru.neostudy.creditbank.dossier.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.neostudy.creditbank.dossier.dto.EmailMessage;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Bean
  public ConsumerFactory<String, EmailMessage> emailMessageConsumerFactory() {
    Map<String, Object> props = new HashMap<>();

    props.put(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        bootstrapAddress);
    props.put(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class);

    JsonDeserializer<EmailMessage> jsonDeserializer = new JsonDeserializer<>(EmailMessage.class);
    jsonDeserializer.setRemoveTypeHeaders(false);
    jsonDeserializer.addTrustedPackages("*");
    jsonDeserializer.setUseTypeMapperForKey(true);
    props.put(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        jsonDeserializer);

    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, EmailMessage>
  emailMessageListenerContainerFactory() {

    ConcurrentKafkaListenerContainerFactory<String, EmailMessage> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(emailMessageConsumerFactory());
    return factory;
  }
}
