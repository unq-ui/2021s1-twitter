package org.unq.ui.bootstrap

import org.unq.ui.model.TwitterSystem
import kotlin.random.Random

val photos = getTweets()
val commentTweets = getCommentTweets()
val users = getUsers()
val random = Random(1001)

private fun addUsers(twitterSystem: TwitterSystem) {
    users.forEach {
        twitterSystem.register(it.name, it.email, it.password, it.image)
    }
}

private fun addPhotos(twitterSystem: TwitterSystem) {
    twitterSystem.users.forEach { user ->
        val photos = List(15) { photos[random.nextInt(1, photos.size - 1)] }.toSet().toList()
        photos.forEach { twitterSystem.addTweet(user.id, it) }
    }
}

private fun addFollowers(twitterSystem: TwitterSystem) {
    twitterSystem.users.forEach { user ->
        val selectedUsers = List(15) { twitterSystem.users[random.nextInt(0, twitterSystem.users.size - 1)].id }.toSet().toMutableList()
        selectedUsers.removeIf { user.id == it }
        selectedUsers.forEach { twitterSystem.updateFollower(user.id, it) }
    }
}

private fun addCommentTweets(twitterSystem: TwitterSystem){
    val tweets = twitterSystem.tweets.toMutableList()
    tweets.forEach{ tweet ->
        val draftTweets = List(5) { commentTweets[random.nextInt(0, commentTweets.size - 1)] }.toSet().toList()
        draftTweets.forEach{
            val user = twitterSystem.users[random.nextInt(0, twitterSystem.users.size - 1)]
            twitterSystem.addComment(tweet.id,user.id,it)
        }
    }

}

private fun addLikes(twitterSystem: TwitterSystem) {
    val posts = twitterSystem.tweets
    twitterSystem.users.forEach { user ->
        val selectedPosts = List(75) { posts[random.nextInt(0, posts.size - 1)] }
        selectedPosts.forEach {
            twitterSystem.updateLike(it.id, user.id)
        }
    }
}

fun getTwitterSystem(): TwitterSystem {
    val twitterSystem = TwitterSystem()
    addUsers(twitterSystem)
    addPhotos(twitterSystem)
    addFollowers(twitterSystem)
    addCommentTweets(twitterSystem)
    addLikes(twitterSystem)
    return twitterSystem
}
