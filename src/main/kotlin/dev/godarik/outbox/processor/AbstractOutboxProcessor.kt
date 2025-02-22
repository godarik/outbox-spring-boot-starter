package dev.godarik.outbox.processor

import dev.godarik.outbox.config.properties.OutboxProperties
import dev.godarik.outbox.handler.OutboxHandler
import dev.godarik.outbox.interceptor.OutboxInterceptorFacade
import dev.godarik.outbox.model.MessageResult
import dev.godarik.outbox.model.OutboxMessage
import dev.godarik.outbox.utils.DEFAULT
import mu.KLogging
import java.util.concurrent.CompletableFuture

abstract class AbstractOutboxProcessor(
    handlers: List<OutboxHandler>,
    protected val properties: OutboxProperties,
    protected val outboxInterceptorFacade: OutboxInterceptorFacade
) : OutboxProcessor {

    protected companion object : KLogging()

    protected val handlersMap = handlers.associateBy { it.type }

    protected open val defaultHandler = handlersMap[DEFAULT]
        ?: throw RuntimeException("Not found default outbox handler")

    protected open val defaultTopic = properties.topic[DEFAULT]
        ?: throw RuntimeException("Not found default outbox topic")

    protected open fun processMessage(
        topicName: String,
        message: OutboxMessage,
        handler: OutboxHandler
    ): CompletableFuture<MessageResult> {
        try {
            logger.debug { "Processing message id: ${message.id} to topic: $topicName with handler: ${handler.javaClass.name}" }
            outboxInterceptorFacade.beforeCall(topicName, message)

            val callback = handler.handle(topicName, message)

            outboxInterceptorFacade.afterCall(topicName, message, callback)
            logger.debug { "End of processing message id: ${message.id} to topic: $topicName with handler: ${handler.javaClass.name}" }
            return callback
        } catch (ex: Exception) {
            logger.error { "Error occurred while processing message id: ${message.id} to topic: $topicName with handler: ${handler.javaClass.name}" }
            outboxInterceptorFacade.onError(topicName, message, ex)
            return CompletableFuture.failedFuture(ex)
        }
    }
}