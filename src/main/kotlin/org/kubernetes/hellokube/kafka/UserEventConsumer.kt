@file:Suppress("UNRESOLVED_REFERENCE")

package org.kubernetes.hellokube.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.util.retry.Retry
import java.time.Duration
import javax.annotation.PreDestroy

/**
 * Reactive Kafka consumer. Reads user events from the configured topic and saves each one through
 * [UserEventHandler]. Offsets are acknowledged only after the database write succeeds.
 */
@Component
@ConditionalOnProperty(prefix = "app.kafka", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class UserEventConsumer(
    private val properties: KafkaConsumerProperties,
    private val handler: UserEventHandler
) {
    private val log = LoggerFactory.getLogger(UserEventConsumer::class.java)
    private var cancelSubscription: (() -> Unit)? = null

    @EventListener(ApplicationReadyEvent::class)
    fun start() {
        log.info("Starting Kafka consumer: topic={} group={} servers={}",
            properties.topic, properties.groupId, properties.bootstrapServers)

        val activeSubscription = Flux.defer { consume() }
            .retryWhen(
                Retry.backoff(Long.MAX_VALUE, Duration.ofSeconds(1))
                    .maxBackoff(Duration.ofSeconds(30))
                    .doBeforeRetry { log.warn("Kafka consumer failed, restarting: {}", it.failure().message) }
            )
            .subscribe(
                { },
                { log.error("Kafka consumer stopped", it) }
            )
            cancelSubscription = { activeSubscription.dispose() }
    }

    private fun consume(): Flux<Void> =
        KafkaReceiver.create(receiverOptions())
            .receive()
            .concatMap { record ->
                handler.handle(record.value())
                    .doOnSuccess { record.receiverOffset().acknowledge() }
            }

    private fun receiverOptions(): ReceiverOptions<String, String> {
        val config = mutableMapOf<String, Any>(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG to properties.groupId,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false
        )
        config.putAll(properties.properties)

        return ReceiverOptions.create<String, String>(config)
            .subscription(listOf(properties.topic))
    }

    @PreDestroy
    fun stop() {
        cancelSubscription?.invoke()
    }
}
