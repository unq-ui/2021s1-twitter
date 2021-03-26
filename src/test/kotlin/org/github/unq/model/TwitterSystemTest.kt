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

    private fun getTwitterSystemWithTwoUsersAndOneTweetPerUser(): TwitterSystem {
        val system = TwitterSystem()
        system.register("juan", "juan@gmail.com", "juan", "http://image.com/1234")
        system.register("lean", "lean@gmail.com", "lean", "http://image.com/1234")
        system.addTweet("u_1", DraftTweet("How it started / how it going" , mutableListOf("landscape.png")))
        Thread.sleep(1)
        system.addTweet("u_2", DraftTweet("How it started / how it going" , mutableListOf("landscape.png")))
        return system
    }

    private fun getTweeterSystemWithTwoUsersAndOneTweetPerUser(): TwitterSystem {
        val system = TwitterSystem()
        system.register("juan", "juan@gmail.com", "juan", "http://image.com/1234")
        system.register("lean", "lean@gmail.com", "lean", "http://image.com/1234")
        system.addTweet("u_1", DraftTweet("How it started / how it going" , mutableListOf("landscape.png")))
        Thread.sleep(1)
        system.addTweet("u_2", DraftTweet("Hi Tweeter!" , mutableListOf("portrait.png")))
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

    @Test
    fun addTweetTest() {
        val system = getTwitterSystemWithTwoUsers()
        assertEquals(system.tweets.size, 0)
        system.addTweet("u_1", DraftTweet("How it started / how it going" , mutableListOf("landscape.png")))
        assertEquals(system.tweets.size, 1)
        val tweet = system.tweets[0]
        assertEquals(tweet.id, "t_1")
        assertEquals(tweet.author.id, "u_1")
        assertEquals(tweet.images, mutableListOf("landscape.png"))
        assertEquals(tweet.text, "How it started / how it going")
        assertEquals(tweet.likes.size, 0)
    }

    @Test
    fun addTweetWithWrongUserIdTest() {
        val instagramSystem = getTwitterSystemWithTwoUsers()
        assertFailsWith<NotFound> {
            instagramSystem.addTweet(
                "u_10000",
                DraftTweet("How it started / how it going" , mutableListOf("landscape.png"))
            )
        }
    }

    @Test
    fun editTweetTest() {
        val system = getTwitterSystemWithTwoUsers()
        assertEquals(system.tweets.size, 0)
        system.addTweet("u_1", DraftTweet("How it started / how it going" , mutableListOf("landscape.png")))
        assertEquals(system.tweets.size, 1)
        system.editTweet("t_1", DraftTweet("Another text" , mutableListOf("landscape2.png")))
        val post = system.tweets[0]
        assertEquals(post.id, "t_1")
        assertEquals(post.author.id, "u_1")
        assertEquals(post.text, "Another text")
        assertEquals(post.images, mutableListOf("landscape2.png"))
        assertEquals(post.likes.size, 0)
    }

    @Test
    fun editPostWithWrongPostIdTest() {
        val system = getTwitterSystemWithTwoUsers()
        assertEquals(system.tweets.size, 0)
        system.addTweet("u_1", DraftTweet("How it started / how it going" , mutableListOf("landscape.png")))
        assertEquals(system.tweets.size, 1)
        assertFailsWith<NotFound> {
            system.editTweet(
                "t_10000",
                DraftTweet("New text" , mutableListOf("portrait.png"))
            )
        }
    }

    @Test
    fun deleteTweetTest() {
        val system = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        assertEquals(system.tweets.size, 2)
        system.deleteTweet("t_1")
        assertEquals(system.tweets.size, 1)
    }

    @Test
    fun deleteTweetWithWrongIdTest() {
        val instagramSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        assertEquals(instagramSystem.tweets.size, 2)
        instagramSystem.deleteTweet("t_1000")
        assertEquals(instagramSystem.tweets.size, 2)
    }

    @Test
    fun addReplyTweetTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        val retweet = twitterSystem.addReply("t_1", "u_2", DraftReply("im a reply"))
        val originalTweet = twitterSystem.getTweet("t_1")
        assertEquals(retweet.author.id, "u_2")
        assertEquals(retweet.text, "im a reply")
        assertEquals(retweet.reply, originalTweet)
    }

    @Test
    fun addCommentWithWrongUserIdTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        assertFailsWith<NotFound> { twitterSystem.addComment("t_1", "u_20000", DraftTweet("comment")) }
    }

    @Test
    fun addCommentToWrongTweetIdTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        assertFailsWith<NotFound> { twitterSystem.addComment("t_189876564678", "u_1", DraftTweet("comment")) }
    }

    @Test
    fun addCommentTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        val author = twitterSystem.getUser("u_2")
        assertEquals(1, author.tweets.size)
        val commentTweet =  twitterSystem.addComment("t_1", author.id, DraftTweet("this is a comment"))
        val originalTweet = twitterSystem.getTweet("t_1")

        assertEquals(author.id, commentTweet.author.id)
        assertEquals(2, author.tweets.size)
        assertEquals(commentTweet, originalTweet.comments[0])
    }
/*
    @Test
    fun updateLikeTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        val originalPost = twitterSystem.getTweet("t_1")
        assertEquals(0, originalPost.likes.size)
        val post = twitterSystem.updateLike("t_1", "u_2")
        assertEquals(post.likes.size, 1)
    }

@Test
fun updateLikeTwoTimesTest() {
    val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
    val originalPost = twitterSystem.getPost("p_1")
    assertEquals(originalPost.likes.size, 0)
    val firstTimePost = twitterSystem.updateLike("p_1", "u_2")
    assertEquals(firstTimePost.likes.size, 1)
    val secondTimePost = twitterSystem.updateLike("p_1", "u_2")
    assertEquals(secondTimePost.likes.size, 0)
}

@Test
fun updateLikeWithWrongPostIdTest() {
    val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
    assertFailsWith<NotFound> { twitterSystem.updateLike("p_10000", "u_2") }
}

@Test
fun updateLikeWithWrongUserIdTest() {
    val twitterSystem = getTwiterSystemWithTwoUsersAndOneTweetPerUser()
    assertFailsWith<NotFound> { twitterSystem.updateLike("p_1", "u_20000") }
}
 */
}