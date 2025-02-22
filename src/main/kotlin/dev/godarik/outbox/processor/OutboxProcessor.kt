package dev.godarik.outbox.processor

interface OutboxProcessor {

    fun processMessages()
}