package org.kubernetes.hellokube.database

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.springframework.jdbc.core.JdbcTemplate

class MobileDataServiceTests {
    @Test
    fun savesValidJsonPayload() {
        val jdbcTemplate = mock(JdbcTemplate::class.java)
        val service = MobileDataService(jdbcTemplate, ObjectMapper())
        val payload = """{"deviceId":"device-1","value":42}"""

        service.saveMobileData(payload)

        verify(jdbcTemplate).update(
            "INSERT INTO mobile_events (payload) VALUES (?::jsonb)",
            payload
        )
    }

    @Test
    fun rejectsInvalidJsonWithoutWritingToDatabase() {
        val jdbcTemplate = mock(JdbcTemplate::class.java)
        val service = MobileDataService(jdbcTemplate, ObjectMapper())

        assertThrows(JsonProcessingException::class.java) {
            service.saveMobileData("{invalid-json}")
        }

        verifyNoInteractions(jdbcTemplate)
    }
}