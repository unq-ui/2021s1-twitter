package org.github.unq.model

import java.time.LocalDateTime

data class User(val id: String, var name: String, val email: String, var password: String, var image: String, val followers: MutableList<User>)
data class Comment(val id: String, val body: String, val author: User)
data class Tweet(val id: String, val author: User, var text: String, var images: MutableList<String> = mutableListOf(), val date: LocalDateTime, val comments: MutableList<Comment>, val likes: MutableList<User>)


data class DraftTweet(val text: String, val images: MutableList<String> = mutableListOf())
data class DraftReply(val text: String, val images: MutableList<String> = mutableListOf())

