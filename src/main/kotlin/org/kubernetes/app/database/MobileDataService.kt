package org.kubernetes.hellokube.database

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class MobileDataService(
    private val jdbcTemplate: JdbcTemplate,
    private val objectMapper: ObjectMapper
) {
    fun saveMobileData(payload: String) {
        require(payload.isNotBlank()) { "Mobile data payload must not be blank" }
        objectMapper.readTree(payload)
            ?: throw IllegalArgumentException("Mobile data payload must be valid JSON")

        jdbcTemplate.update(
            "INSERT INTO mobile_events (payload) VALUES (?::jsonb)",
            payload
        )
    }

    fun getMobileData(): List<JsonNode> = jdbcTemplate.query(
        "SELECT payload::text FROM mobile_events ORDER BY received_at DESC, id DESC"
    ) { resultSet, _ -> objectMapper.readTree(resultSet.getString(1)) }
}