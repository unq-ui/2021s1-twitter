package org.unq.ui.model

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
        val tweet = Tweet(id= idGenerator.nextTweetId(), author= user, text= draftTweet.text, images= draftTweet.images, date= LocalDateTime.now())
        user.tweets.add(tweet)
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
        getTweet(tweetId).author.tweets.removeIf { it.id == tweetId }
        tweets.removeIf { it.id == tweetId }
    }

    fun getTweet(tweetId: String): Tweet = tweets.find { it.id == tweetId } ?: throw NotFound("Tweet")

    fun addReply(tweetId: String, userId: String, draftReply: DraftReply): Tweet {
        val tweet = getTweet(tweetId)
        val user = getUser(userId)
        val retweet = Tweet(id= idGenerator.nextTweetId(), author= user, text= draftReply.text, images= draftReply.images, reply= tweet, date= LocalDateTime.now())
        user.tweets.add(retweet)
        tweets.add(retweet)
        return retweet
    }

    fun addComment(tweetId: String, userId: String, commetTweet: DraftTweet) : Tweet {
        val user = getUser(userId)
        val tweet = getTweet(tweetId)
        val comment = Tweet(id= idGenerator.nextTweetId(), author= user, text= commetTweet.text, images= commetTweet.images, date= LocalDateTime.now())
        tweet.comments.add(comment)
        user.tweets.add(comment)
        tweets.add(comment)
        return comment
    }

    fun updateLike(tweetId: String, userId: String) : Tweet {
        val tweet = getTweet(tweetId)
        val user = getUser(userId)
        if (tweet.likes.contains(user)) {
            tweet.likes.remove(user)
            return tweet
        }
        tweet.likes.add(user)
        return tweet
    }

    fun searchByTag(tag: String): List<Tweet> {
        if (!tag.startsWith("#")) throw NotATag()
        return tweets.filter { it.text.contains(tag) }.sortedByDescending { it.date }
    }

    fun searchByUserName(name: String): List<Tweet> {
        val userIds = users.filter { it.name.contains(name, true) }
        return tweets.filter { userIds.contains(it.author) }.sortedByDescending { it.date }
    }

    fun searchByUserId(userId: String): List<Tweet> {
        val user = getUser(userId)
        return tweets.filter { it.author == user }.sortedByDescending { it.date }
    }

    fun searchByName(name: String): List<User> {
        if (name.isBlank()) return listOf()
        return users.filter { it.name.contains(name, true) }.sortedBy { it.name }
    }

    fun timeline(userId: String): List<Tweet> {
        val user = getUser(userId)
        return tweets.filter { user.followers.contains(it.author) }.sortedByDescending { it.date }
    }

    fun updateFollower(fromUserId: String, toUserId: String) {
        val fromUser = getUser(fromUserId)
        val toUser = getUser(toUserId)
        if (fromUser.followers.contains(toUser)) {
            fromUser.followers.remove(toUser)
        } else {
            fromUser.followers.add(toUser)
        }
    }

    private fun existUserWithEmail(email: String) {
        if (users.any { it.email == email }) throw UsedEmail()
    }
}