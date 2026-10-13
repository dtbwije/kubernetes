package org.kubernetes.hellokube.kafka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * Settings for the user-event consumer.
 *
 * The same code talks to a local Kafka broker (profile "local") or to Azure Event Hubs'
 * Kafka endpoint (profile "cloud"). Only the values differ; anything in [properties] is passed
 * straight through to the Kafka client (security.protocol, sasl.mechanism, sasl.jaas.config, ...).
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "app.kafka")
data class KafkaConsumerProperties(
    val enabled: Boolean = true,
    val bootstrapServers: String = "localhost:9092",
    val topic: String = "user-events",
    val groupId: String = "user-event-processor",
    val properties: Map<String, String> = emptyMap()
)
