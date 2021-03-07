package org.github.unq.model

import java.time.LocalDateTime

class TwitterSystem(val users: MutableList<User> = mutableListOf(), val tweets: MutableList<Tweet> = mutableListOf(), private val idGenerator: IdGenerator = IdGenerator()) {

    fun register(name: String, email: String, password: String, image: String): User {
        existUserWithEmail(email)
        val user = User(idGenerator.nextUserId(), name, email, password, image, mutableListOf())
        users.add(user)
        return user
    }

    fun login(email: String, password: String): User {
        return users.find { it.email == email && it.password == password } ?: throw NotFound("User")
    }

    fun getUser(userId: String): User = users.find { it.id == userId } ?: throw NotFound("User")

    fun editProfile(userId: String, name: String, password: String, image: String): User {
        val user = getUser(userId)
        user.name = name
        user.password = password
        user.image = image
        return user
    }

    fun addTweet(userId: String, draftTweet: DraftTweet): Tweet {
        val user = getUser(userId)
        val tweet = Tweet(idGenerator.nextTweetId(), user,draftTweet.text, draftTweet.images, LocalDateTime.now(), mutableListOf(), mutableListOf())
        tweets.add(tweet)
        return tweet
    }

    fun editTweet(tweetId: String, draftTweet: DraftTweet): Tweet {
        val tweet = getTweet(tweetId)
        tweet.text = draftTweet.text
        tweet.images = draftTweet.images
        return tweet
    }

    fun deleteTweet(tweetId: String) {
        tweets.removeIf { it.id == tweetId }
    }

    fun getTweet(postId: String): Tweet = tweets.find { it.id == postId } ?: throw NotFound("Tweet")

    private fun existUserWithEmail(email: String) {
        if (users.any { it.email == email }) throw UsedEmail()
    }
}