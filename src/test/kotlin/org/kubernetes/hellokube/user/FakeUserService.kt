package org.kubernetes.hellokube.user

import org.mockito.Mockito.mock
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.util.concurrent.atomic.AtomicLong

/**
 * Test double for [UserService] that keeps users in memory (no database needed).
 * Set [failWith] to simulate a database outage.
 */
class FakeUserService : UserService(mock(UserRepository::class.java)) {
    val saved = mutableListOf<User>()
    var failWith: Throwable? = null
    private val ids = AtomicLong(1)

    override fun save(request: SaveUserRequest): Mono<User> {
        failWith?.let { return Mono.error(it) }
        val name = request.name?.trim().orEmpty()
        val email = request.email?.trim().orEmpty()
        if (name.isEmpty() || email.isEmpty()) {
            return Mono.error(IllegalArgumentException("name and email must not be blank"))
        }
        val user = User(ids.getAndIncrement(), name, email, OffsetDateTime.now())
        saved.add(user)
        return Mono.just(user)
    }

    override fun findAll(): Flux<User> = Flux.fromIterable(saved)
}
