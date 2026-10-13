package org.kubernetes.hellokube.kafka

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.kubernetes.hellokube.user.SaveUserRequest
import org.kubernetes.hellokube.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

/**
 * Turns one Kafka message value (JSON) into a saved user. Kept separate from the Kafka plumbing so
 * it can be tested without a broker.
 *
 * Messages that can never succeed (not JSON, blank fields) are logged and skipped so they do not
 * block the partition. Anything else (e.g. the database being down) is propagated so the consumer
 * restarts and the message is re-read, because its offset was never committed.
 */
@Component
class UserEventHandler(
    private val userService: UserService,
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(UserEventHandler::class.java)

    fun handle(payload: String?): Mono<Void> {
        val request = try {
            objectMapper.readValue<SaveUserRequest>(payload ?: "")
        } catch (e: JsonProcessingException) {
            log.warn("Skipping user event that is not valid JSON: {}", e.originalMessage)
            return Mono.empty()
        }

        return userService.save(request)
            .doOnNext { log.info("Saved user {} from Kafka event", it.id) }
            .then()
            .onErrorResume(IllegalArgumentException::class.java) { e ->
                log.warn("Skipping invalid user event: {}", e.message)
                Mono.empty()
            }
    }
}
