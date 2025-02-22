package dev.godarik.outbox.config

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.serialization.IntegerSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.ProducerListener
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaConfiguration {

    companion object : KLogging()

    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String? = null

    @Bean
    fun objectMapper() = ObjectMapper()

    @Bean
    fun <T> producerFactory(objectMapper: ObjectMapper): DefaultKafkaProducerFactory<Int, T> {
        val senderProps = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.LINGER_MS_CONFIG to 10,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to IntegerSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
        )

        return DefaultKafkaProducerFactory<Int, T>(senderProps, IntegerSerializer(), JsonSerializer<T>(objectMapper))
    }

    @Bean
    fun <T> kafkaTemplateT(objectMapper: ObjectMapper): KafkaTemplate<Int, T> {
        val template = KafkaTemplate<Int, T>(producerFactory(objectMapper))
        template.setProducerListener(object : ProducerListener<Int, T> {

            override fun onSuccess(
                producerRecord: ProducerRecord<Int, T?>,
                recordMetadata: RecordMetadata
            ) {
            }

            override fun onError(
                producerRecord: ProducerRecord<Int, T>?,
                recordMetadata: RecordMetadata?,
                exception: Exception?
            ) {
            }
        })
        return template
    }
}
