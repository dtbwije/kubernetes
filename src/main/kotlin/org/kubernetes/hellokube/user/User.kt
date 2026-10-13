package org.kubernetes.hellokube.user

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table("users")
data class User(
    @Id
    val id: Long? = null,
    val name: String,
    val email: String,
    @Column("created_at")
    val createdAt: OffsetDateTime? = null
)
