package org.unq.ui.model

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
        system.addTweet("u_1", DraftTweet("How it started / how it going #description" , mutableListOf("landscape.png")))
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
        val tweet = system.getTweet("t_1")
        assertEquals(tweet.author.tweets.size,1)
        assertEquals(system.tweets.size, 2)
        system.deleteTweet(tweet.id)
        assertEquals(system.tweets.size, 1)
        assertEquals(system.getUser(tweet.author.id).tweets.size,0)
    }

    @Test
    fun deleteTweetWithWrongIdTest() {
        val instagramSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        assertEquals(instagramSystem.tweets.size, 2)
        assertFailsWith<NotFound>(
            message = "No found Tweet",
            block = {
                instagramSystem.deleteTweet("t_1000")
            }
        )
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

    @Test
    fun updateLikeTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        val originalTweet = twitterSystem.getTweet("t_1")
        assertEquals(0, originalTweet.likes.size)
        val tweet = twitterSystem.updateLike("t_1", "u_2")
        assertEquals(1, tweet.likes.size)
    }

    @Test
    fun updateLikeTwoTimesTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        val originalTweet = twitterSystem.getTweet("t_1")
        assertEquals(originalTweet.likes.size, 0)
        val firstTimePost = twitterSystem.updateLike("t_1", "u_2")
        assertEquals(firstTimePost.likes.size, 1)
        val secondTimePost = twitterSystem.updateLike("t_1", "u_2")
        assertEquals(secondTimePost.likes.size, 0)
    }

    @Test
    fun updateLikeWithWrongTweetIdTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        assertFailsWith<NotFound> { twitterSystem.updateLike("t_10000", "u_2") }
    }

    @Test
    fun updateLikeWithWrongUserIdTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        assertFailsWith<NotFound> { twitterSystem.updateLike("t_1", "u_20000") }
    }

    @Test
    fun updateFollowerTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        val originalUser = twitterSystem.getUser("u_1")
        assertEquals(originalUser.followers.size, 0)
        twitterSystem.updateFollower("u_1", "u_2")
        assertEquals(originalUser.followers.size, 1)
    }

    @Test
    fun updateFollowerTwoTimesTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        val originalUser = twitterSystem.getUser("u_1")
        assertEquals(originalUser.followers.size, 0)
        twitterSystem.updateFollower("u_1", "u_2")
        assertEquals(originalUser.followers.size, 1)
        twitterSystem.updateFollower("u_1", "u_2")
        assertEquals(originalUser.followers.size, 0)
    }

    @Test
    fun updateFollowerWithWrongFromUserIdTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        assertFailsWith<NotFound> { twitterSystem.updateFollower("u_10000", "u_2") }
    }

    @Test
    fun updateFollowerWithWrongToUserIdTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        assertFailsWith<NotFound> { twitterSystem.updateFollower("u_1", "u_20000") }
    }

    @Test
    fun searchByTagTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        val posts = twitterSystem.searchByTag("#description")
        assertEquals(posts.size, 1)
        val post = posts[0]
        assertEquals(post.author.id, "u_1")
    }

    @Test
    fun searchByTagWithWrongTagFormatTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        assertFailsWith<NotATag> { twitterSystem.searchByTag("description") }
    }

    @Test
    fun searchByUserNameTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        val tweets = twitterSystem.searchByUserName("a")
        assertEquals(tweets.size, 2)
        assertEquals(tweets[0].author.id, "u_2")
        assertEquals(tweets[1].author.id, "u_1")
    }

    @Test
    fun searchByUserIdTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        val tweets = twitterSystem.searchByUserId("u_1")
        assertEquals(tweets.size, 1)
        assertEquals(tweets[0].author.id, "u_1")
    }

    @Test
    fun timelineTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        twitterSystem.updateFollower("u_1", "u_2")
        val posts = twitterSystem.timeline("u_1")
        assertEquals(posts.size, 1)
        assertEquals(posts[0].author.id, "u_2")
    }

    @Test
    fun timelineWithWrongUserIdTest() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        assertFailsWith<NotFound> { twitterSystem.timeline("u_1000") }
    }

    @Test
    fun searchByName() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        val result = twitterSystem.searchByName("ju")
        assertEquals(result.size, 1)
        assertEquals(result[0].name, "juan")
    }

    @Test
    fun searchByNameWithoutName() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        val result = twitterSystem.searchByName("")
        assertEquals(result.size, 0)
    }

    @Test
    fun searchByNameOnlySpacesName() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        val result = twitterSystem.searchByName("   ")
        assertEquals(result.size, 0)
    }

    @Test
    fun searchByNameWithSameLetter() {
        val twitterSystem = getTwitterSystemWithTwoUsersAndOneTweetPerUser()
        val result = twitterSystem.searchByName("a")
        assertEquals(result.size, 2)
    }
}