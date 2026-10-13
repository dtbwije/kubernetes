package org.kubernetes.hellokube.user

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface UserRepository : ReactiveCrudRepository<User, Long>
