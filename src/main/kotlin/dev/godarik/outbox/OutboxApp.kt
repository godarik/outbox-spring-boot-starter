package dev.godarik.outbox

import dev.godarik.outbox.config.annotation.EnableTransactionOutbox
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableTransactionOutbox
@SpringBootApplication
class OutboxApp

fun main(args: Array<String>) {
    runApplication<OutboxApp>(*args)
}