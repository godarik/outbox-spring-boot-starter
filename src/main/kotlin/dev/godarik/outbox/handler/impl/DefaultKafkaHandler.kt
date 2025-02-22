package dev.godarik.outbox.handler.impl

import dev.godarik.outbox.converter.KafkaKeyValueConverter
import dev.godarik.outbox.handler.OutboxHandler
import dev.godarik.outbox.model.KafkaPayload
import dev.godarik.outbox.model.MessageResult
import dev.godarik.outbox.model.OutboxMessage
import dev.godarik.outbox.producer.impl.KafkaProducer
import dev.godarik.outbox.utils.DEFAULT
import mu.KLogging
import java.io.Serializable
import java.util.concurrent.CompletableFuture

open class DefaultKafkaHandler<K : Serializable, V>(
    private val producer: KafkaProducer<K, V>,
    private val converter: KafkaKeyValueConverter<K, V>
) : OutboxHandler {
    private companion object : KLogging()

    override fun handle(topicName: String, message: OutboxMessage): CompletableFuture<MessageResult> {
        logger.debug {
            "Handling outbox message with objectId: ${message.objectId} and objectType: ${message.objectType} to topic: $topicName"
        }
        val messageKey = converter.convertKey(message.objectId)
        val messagePayload = converter.convertValue(message.payload)
        return producer.send(topicName, messageKey, messagePayload).thenApply { result ->
            MessageResult(KafkaPayload(result.producerRecord, result.recordMetadata))
        }
    }

    override val type: String = DEFAULT
}

