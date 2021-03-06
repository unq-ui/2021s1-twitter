package org.github.unq.model

class IdGenerator {
    private var userId = 0

    fun nextUserId(): String = "u_${++userId}"
}
