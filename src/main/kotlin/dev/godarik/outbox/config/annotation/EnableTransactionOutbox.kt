package dev.godarik.outbox.config.annotation

import dev.godarik.outbox.config.OutboxAutoConfiguration
import org.springframework.context.annotation.Import

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(OutboxAutoConfiguration::class)
annotation class EnableTransactionOutbox
