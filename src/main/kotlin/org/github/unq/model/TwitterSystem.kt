package org.github.unq.model

class TwitterSystem(val users: MutableList<User> = mutableListOf(), private val idGenerator: IdGenerator = IdGenerator()) {

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

    private fun existUserWithEmail(email: String) {
        if (users.any { it.email == email }) throw UsedEmail()
    }
}