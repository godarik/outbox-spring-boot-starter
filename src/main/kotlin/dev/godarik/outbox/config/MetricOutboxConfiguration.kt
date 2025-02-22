package dev.godarik.outbox.config

import dev.godarik.outbox.interceptor.impl.MetricOutboxInterceptor
import dev.godarik.outbox.meter.OutboxMeterService
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean

@ConditionalOnClass(MeterRegistry::class)
class MetricOutboxConfiguration {

    @Bean
    fun outboxMeterService(
        registry: MeterRegistry
    ) = OutboxMeterService(registry)

    @Bean
    fun metricOutboxListener(
        metricService: OutboxMeterService
    ) = MetricOutboxInterceptor(metricService)
}