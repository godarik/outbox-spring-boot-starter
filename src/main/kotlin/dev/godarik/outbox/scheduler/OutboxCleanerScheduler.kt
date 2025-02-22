package dev.godarik.outbox.scheduler

import dev.godarik.outbox.config.properties.OutboxProperties
import dev.godarik.outbox.meter.OutboxMeterService
import dev.godarik.outbox.service.OutboxMessageService
import dev.godarik.outbox.utils.OUTBOX_MESSAGE_METRIC
import mu.KLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime

@ConditionalOnProperty(
    prefix = "outbox.scheduler.cleaner",
    name = ["enabled"],
    havingValue = "true"
)
class OutboxCleanerScheduler(
    private val properties: OutboxProperties,
    private val service: OutboxMessageService,
    private val meterService: OutboxMeterService
) {
    private companion object : KLogging()

    @Scheduled(cron = "\${outbox.scheduler.cleaner.cron:1 * * * * *}")
    fun deleteOlderOutboxMessages() {
        logger.info("Start of cleaning outbox messages")

        val date = LocalDateTime.now().minusDays(properties.message.cleanDays)
        val count = service.deleteAllOlderThan(date)
        meterService.summary(
            name = "${OUTBOX_MESSAGE_METRIC}_deleted",
            amount = count.toDouble()
        )

        logger.info("End of cleaning outbox messages. Deleted message count: $count")
    }
}