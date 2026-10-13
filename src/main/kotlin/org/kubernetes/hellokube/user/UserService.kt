package org.kubernetes.hellokube.user

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun save(request: SaveUserRequest): Mono<User> {
        val name = request.name?.trim().orEmpty()
        val email = request.email?.trim().orEmpty()
        if (name.isEmpty() || email.isEmpty()) {
            return Mono.error(IllegalArgumentException("name and email must not be blank"))
        }
        return userRepository.save(User(name = name, email = email, createdAt = OffsetDateTime.now()))
    }

    fun findAll(): Flux<User> = userRepository.findAll()
}
