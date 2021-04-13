package bootstrap

import org.github.unq.model.DraftTweet
import org.github.unq.model.TwitterSystem
import kotlin.random.Random

val photos = getTweets()
val comments = getComments()
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

private fun addComments(twitterSystem: TwitterSystem) {
    twitterSystem.tweets.forEach { post ->
        val selectedComments = List(7) { comments[random.nextInt(0, comments.size - 1)] }.toSet().toList()
        selectedComments.forEach {
            val user = twitterSystem.users[random.nextInt(0, twitterSystem.users.size - 1)]
            twitterSystem.addComment(post.id, user.id, DraftTweet(it))
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
    addComments(twitterSystem)
    addLikes(twitterSystem)
    return twitterSystem
}