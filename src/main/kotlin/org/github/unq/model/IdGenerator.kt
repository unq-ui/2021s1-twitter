package org.github.unq.model

class IdGenerator {
    private var userId = 0
    private var tweetId = 0

    fun nextUserId(): String = "u_${++userId}"
    fun nextTweetId(): String = "t_${++tweetId}"

}
