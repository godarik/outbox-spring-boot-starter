package dev.godarik.outbox.handler

import dev.godarik.outbox.model.MessageResult
import dev.godarik.outbox.model.OutboxMessage
import java.util.concurrent.CompletableFuture

interface OutboxHandler {

    fun handle(topicName: String, message: OutboxMessage): CompletableFuture<MessageResult>

    val type: String
}