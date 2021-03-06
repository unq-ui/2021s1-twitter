package org.github.unq.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TwitterSystemTest {

    private fun getTwitterSystemWithTwoUsers(): TwitterSystem {
        val system = TwitterSystem()
        system.register("juan", "juan@gmail.com", "juan", "http://image.com/1234")
        system.register("lean", "lean@gmail.com", "lean", "http://image.com/1234")
        return system
    }

    @Test
    fun registerTest() {
        val twitterSystem = TwitterSystem()
        assertEquals(twitterSystem.users.size, 0)
        twitterSystem.register("julian", "julian@gmail.com", "julian", "http://image.com/1234")
        assertEquals(twitterSystem.users.size, 1)
        val user = twitterSystem.users[0]
        assertEquals(user.id, "u_1")
        assertEquals(user.name, "julian")
        assertEquals(user.email, "julian@gmail.com")
        assertEquals(user.password, "julian")
        assertEquals(user.image, "http://image.com/1234")
    }

    @Test
    fun registerTwoTimesTest() {
        val twitterSystem = TwitterSystem()
        assertEquals(twitterSystem.users.size, 0)
        twitterSystem.register("juan", "juan@gmail.com", "juan", "http://image.com/1234")
        assertEquals(twitterSystem.users.size, 1)
        assertFailsWith<UsedEmail> {
            twitterSystem.register(
                "Juan",
                "juan@gmail.com",
                "juan",
                "http://image.com/1234"
            )
        }
    }

    @Test
    fun loginWithoutUserTest() {
        val twitterSystem = TwitterSystem()
        assertEquals(twitterSystem.users.size, 0)
        assertFailsWith<NotFound> { twitterSystem.login("juan@gmail.com", "juan") }
    }

    @Test
    fun loginTest() {
        val twitterSystem = TwitterSystem()
        assertEquals(twitterSystem.users.size, 0)
        twitterSystem.register("juan", "juan@gmail.com", "juan", "http://image.com/1234")
        assertEquals(twitterSystem.users.size, 1)
        val user = twitterSystem.login("juan@gmail.com", "juan")
        assertEquals(user.id, "u_1")
        assertEquals(user.name, "juan")
        assertEquals(user.email, "juan@gmail.com")
        assertEquals(user.password, "juan")
        assertEquals(user.image, "http://image.com/1234")
        assertEquals(user.followers.size, 0)
    }

    @Test
    fun editProfileTest() {
        val system = getTwitterSystemWithTwoUsers()
        val user = system.editProfile("u_1", "juan2", "juan2", "http://image.com/4321")
        assertEquals(user.id, "u_1")
        assertEquals(user.name, "juan2")
        assertEquals(user.email, "juan@gmail.com")
        assertEquals(user.password, "juan2")
        assertEquals(user.image, "http://image.com/4321")
        assertEquals(user.followers.size, 0)
    }

    @Test
    fun editProfileWithWrongUserId() {
        val system = getTwitterSystemWithTwoUsers()
        assertFailsWith<NotFound> { system.editProfile("u_1000", "juan2", "juan2", "http://image.com/4321") }
    }


}