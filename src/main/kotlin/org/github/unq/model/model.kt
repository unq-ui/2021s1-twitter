package org.github.unq.model

import java.time.LocalDateTime

data class User(
    val id: String,
    var name: String,
    val email: String,
    var password: String,
    var image: String,
    val followers: MutableList<User>,
    val tweets: MutableList<Tweet> = mutableListOf()
    )

data class Tweet(
    val id: String,
    val author: User,
    var text: String,
    val reply: Tweet? = null,
    var images: MutableList<String> = mutableListOf(),
    val date: LocalDateTime,
    val comments: MutableList<Tweet> = mutableListOf(),
    val likes: MutableList<User> = mutableListOf()
    )

data class DraftTweet(val text: String, val images: MutableList<String> = mutableListOf())
data class DraftReply(val text: String, val images: MutableList<String> = mutableListOf())
