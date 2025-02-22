package dev.godarik.outbox.interceptor

import dev.godarik.outbox.model.MessageResult
import dev.godarik.outbox.model.OutboxMessage
import java.util.concurrent.CompletableFuture

interface OutboxInterceptor {

    fun beforeCall(
        topicName: String,
        message: OutboxMessage
    ) {
    }

    fun afterCall(
        topicName: String,
        message: OutboxMessage,
        callback: CompletableFuture<MessageResult>
    ) {
    }

    fun onError(
        topicName: String,
        message: OutboxMessage,
        ex: Exception
    ) {
    }
}