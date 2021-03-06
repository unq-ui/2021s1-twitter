package org.github.unq.model

data class User(val id: String, var name: String, val email: String, var password: String, var image: String, val followers: MutableList<User>)