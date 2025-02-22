package dev.godarik.outbox.service

import dev.godarik.outbox.converter.OutboxMessageConverter
import dev.godarik.outbox.model.OutboxMessage
import dev.godarik.outbox.repository.OutboxMessageRepository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
class OutboxMessageService(
    private val repository: OutboxMessageRepository,
    private val converter: OutboxMessageConverter,
) {
    fun create(model: OutboxMessage) =
        converter.toModel(converter.toEntity(model))

    fun findAllWaiting(limit: Int): List<OutboxMessage> =
        repository.findAllNew(limit).map(converter::toModel)

    fun updateAll(messages: Collection<OutboxMessage>) {
        val records = messages.map(converter::toEntity)
        repository.updateAll(records)
    }

    fun deleteAllOlderThan(date: LocalDateTime) =
        repository.deleteAllOlderThan(date)
}