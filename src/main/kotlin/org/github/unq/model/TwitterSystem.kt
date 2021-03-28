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
        tweets.removeIf { it.id == tweetId }
    }

    fun getTweet(postId: String): Tweet = tweets.find { it.id == postId } ?: throw NotFound("Tweet")

    fun addReply(tweetId: String, userId: String, draftReply: DraftReply): Tweet{
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

    fun updateLike(tweetId: String, userId: String) : Tweet{
        val tweet = getTweet(tweetId)
        val user = getUser(userId)
        if (tweet.likes.contains(user)) {
            tweet.likes.remove(user)
            return tweet
        }
        tweet.likes.add(user)
        return tweet
    }

    private fun existUserWithEmail(email: String) {
        if (users.any { it.email == email }) throw UsedEmail()
    }
}