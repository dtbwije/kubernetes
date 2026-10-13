package org.kubernetes.hellokube.kafka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kubernetes.hellokube.user.FakeUserService
import reactor.test.StepVerifier

class UserEventHandlerTests {

    private lateinit var service: FakeUserService
    private lateinit var handler: UserEventHandler

    @BeforeEach
    fun setUp() {
        service = FakeUserService()
        handler = UserEventHandler(service, jacksonObjectMapper())
    }

    @Test
    fun savesAUserFromAValidEvent() {
        StepVerifier.create(handler.handle("""{"name":"Ada","email":"ada@example.com"}"""))
            .verifyComplete()

        assertEquals(listOf("Ada"), service.saved.map { it.name })
    }

    @Test
    fun skipsEventsThatAreNotJson() {
        StepVerifier.create(handler.handle("{broken")).verifyComplete()
        StepVerifier.create(handler.handle(null)).verifyComplete()

        assertEquals(0, service.saved.size)
    }

    @Test
    fun skipsEventsWithBlankFields() {
        StepVerifier.create(handler.handle("""{"name":" ","email":"ada@example.com"}"""))
            .verifyComplete()

        assertEquals(0, service.saved.size)
    }

    @Test
    fun propagatesDatabaseFailuresSoTheMessageIsRetried() {
        service.failWith = IllegalStateException("db down")

        StepVerifier.create(handler.handle("""{"name":"Ada","email":"ada@example.com"}"""))
            .expectError(IllegalStateException::class.java)
            .verify()
    }
}
