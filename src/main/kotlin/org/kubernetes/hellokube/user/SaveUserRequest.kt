package org.kubernetes.hellokube.user

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

/**
 * Payload accepted by POST /saveUser and by the Kafka consumer.
 */
data class SaveUserRequest(
    @field:NotBlank
    val name: String?,
    @field:NotBlank
    @field:Email
    val email: String?
)
