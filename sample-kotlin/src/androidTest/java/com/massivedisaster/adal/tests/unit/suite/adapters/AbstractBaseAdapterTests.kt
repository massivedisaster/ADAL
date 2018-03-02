/*
 * ADAL - A set of Android libraries to help speed up Android development.
 *
 * Copyright (c) 2018 ADAL
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.massivedisaster.adal.tests.unit.suite.adapters

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.massivedisaster.adal.adapter.AbstractLoadMoreBaseAdapter
import com.massivedisaster.adal.samplekotlin.feature.network.AdapterPost
import com.massivedisaster.adal.samplekotlin.model.Post
import com.massivedisaster.adal.tests.unit.suite.base.AbstractBaseTestSuite
import com.massivedisaster.adal.tests.utils.Constants
import junit.framework.Assert.*
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.lang.Thread.sleep
import java.util.*

class AbstractBaseAdapterTests : AbstractBaseTestSuite() {

    private val sTotalPostCollection = 5

    private val sFirstPostId = 1
    private val sFirstPostUserId = 111
    private val sSecondPostId = 2
    private val sSecondPostUserId = 222

    private val sPostTitle = "Title"
    private val sPostBody = "Body"

    private val sNumberZero = 0
    private val sNumberOne = 1
    private val sNumberTwo = 2
    private val sNumberThree = 3

    private var mRecyclerViewTest: RecyclerView? = null
    private var mAdapterPost: AdapterPost? = null

    override fun setup() {
        mRecyclerViewTest = RecyclerView(getContext())
        mAdapterPost = AdapterPost()
        mRecyclerViewTest!!.adapter = mAdapterPost
        mRecyclerViewTest!!.layoutManager = LinearLayoutManager(getContext())
    }

    override fun dispose() {
        mAdapterPost!!.clear()
        mAdapterPost = null
        mRecyclerViewTest = null
    }

    /**
     *
     * Adds one item into the collection and test if the items count is correct
     */
    @Test
    fun testAddItem() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())
        testClear()

        val firstPost = mock<Post>(Post::class.java)
        val secondPost = mock<Post>(Post::class.java)
        val thirdPost = mock<Post>(Post::class.java)
        val previousSize = mAdapterPost!!.itemCount

        mAdapterPost!!.add(firstPost)
        assertEquals(mAdapterPost!!.itemCount, previousSize + sNumberOne)

        mAdapterPost!!.add(secondPost)
        assertEquals(mAdapterPost!!.itemCount, previousSize + sNumberTwo)

        mAdapterPost!!.add(thirdPost)
        assertEquals(mAdapterPost!!.itemCount, previousSize + sNumberThree)
    }

    /**
     *
     * Adds one item into the collection in a specific position and test if the item at that
     * position matches with the expected item
     */
    @Test
    fun testAddItemAtPosition() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        val firstPost = mock<Post>(Post::class.java)

        `when`<Int>(firstPost.mId).thenReturn(sFirstPostId)
        `when`<Int>(firstPost.mUserId).thenReturn(sFirstPostUserId)
        `when`<String>(firstPost.mTitle).thenReturn(sPostTitle + sFirstPostId.toString())
        `when`<String>(firstPost.mBody).thenReturn(sPostBody + sFirstPostId.toString())

        mAdapterPost!!.add(firstPost)

        var itemOne = mAdapterPost!!.getItem(sNumberZero)

        assertEquals(itemOne.mId, firstPost.mId)
        assertEquals(itemOne.mUserId, firstPost.mUserId)
        assertEquals(itemOne.mTitle, firstPost.mTitle)
        assertEquals(itemOne.mBody, firstPost.mBody)

        val secondPost = mock<Post>(Post::class.java)

        `when`<Int>(secondPost.mId).thenReturn(sSecondPostId)
        `when`<Int>(secondPost.mUserId).thenReturn(sSecondPostUserId)
        `when`<String>(firstPost.mTitle).thenReturn(sPostTitle + sSecondPostId.toString())
        `when`<String>(firstPost.mBody).thenReturn(sPostBody + sSecondPostId.toString())

        mAdapterPost!!.add(sNumberZero, secondPost)

        itemOne = mAdapterPost!!.getItem(sNumberZero)

        assertEquals(itemOne.mId, secondPost.mId)
        assertEquals(itemOne.mUserId, secondPost.mUserId)
        assertEquals(itemOne.mTitle, secondPost.mTitle)
        assertEquals(itemOne.mBody, secondPost.mBody)
    }

    /**
     *
     * Adds a collection of items and check if contains each added item
     */
    @Test
    fun testAddCollection() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())
        testClear()

        val posts = ArrayList<Post>()

        for (i in 0 until sTotalPostCollection) {
            val post = mock<Post>(Post::class.java)

            `when`<Int>(post.mId).thenReturn(i)
            `when`<Int>(post.mUserId).thenReturn(i)
            `when`<String>(post.mTitle).thenReturn(sPostTitle + i.toString())
            `when`<String>(post.mBody).thenReturn(sPostBody + i.toString())

            posts.add(post)
        }

        mAdapterPost!!.addAll(posts)
        assertEquals(mAdapterPost!!.itemCount, sTotalPostCollection)

        for (post in posts) {
            assertTrue(mAdapterPost!!.containsItem(post))
        }
    }

    /**
     *
     * Adds and removes a specific item from a collection and check if the size is identical
     * previous size
     */
    @Test
    fun testRemoveItem() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())
        testClear()

        val post = mock<Post>(Post::class.java)
        val previousSize = mAdapterPost!!.itemCount

        mAdapterPost!!.add(post)
        assertEquals(mAdapterPost!!.itemCount, sNumberOne)

        mAdapterPost!!.remove(post)
        assertEquals(mAdapterPost!!.itemCount, previousSize)
    }

    /**
     *
     * Adds and removes a specific item by it's position from a collection and check if the size
     * is identical previous size
     */
    @Test
    fun testRemoveItemAtPosition() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())
        testClear()

        val post = mock<Post>(Post::class.java)
        val previousSize = mAdapterPost!!.itemCount

        mAdapterPost!!.add(post)
        assertEquals(mAdapterPost!!.itemCount, sNumberOne)

        mAdapterPost!!.remove(sNumberZero)
        assertEquals(mAdapterPost!!.itemCount, previousSize)
    }

    /**
     *
     * Adds and removes a collection from a collection and check if the size is identical
     * previous size
     */
    @Test
    fun testRemoveCollection() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())
        testClear()

        val posts = ArrayList<Post>()
        val previousSize = mAdapterPost!!.itemCount

        for (i in 0 until sTotalPostCollection) {
            val post = mock<Post>(Post::class.java)

            `when`<Int>(post.mId).thenReturn(i)
            `when`<Int>(post.mUserId).thenReturn(i)
            `when`<String>(post.mTitle).thenReturn(sPostTitle + i.toString())
            `when`<String>(post.mBody).thenReturn(sPostBody + i.toString())

            posts.add(post)
        }

        mAdapterPost!!.addAll(posts)
        assertEquals(mAdapterPost!!.itemCount, sTotalPostCollection)

        mAdapterPost!!.removeAll(posts)
        assertEquals(mAdapterPost!!.itemCount, previousSize)
    }

    /**
     *
     * Get one item from the collection that doesn't exist
     */
    @Test(expected = IndexOutOfBoundsException::class)
    fun testGetItemOutOfBounds() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        val firstPost = mock<Post>(Post::class.java)
        val secondPost = mock<Post>(Post::class.java)

        mAdapterPost!!.add(firstPost)
        mAdapterPost!!.add(secondPost)

        assertNotNull(mAdapterPost!!.getItem(sNumberZero))
        assertNotNull(mAdapterPost!!.getItem(sNumberOne))

        mAdapterPost!!.getItem(sNumberTwo)
    }

    /**
     *
     * Get one item from an empty collection
     */
    @Test(expected = IndexOutOfBoundsException::class)
    fun testGetItemEmptyAdapter() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())
        testClear()

        mAdapterPost!!.getItem(sNumberZero)
    }

    /**
     *
     * Adds an item, test if the [getItemCount][AbstractLoadMoreBaseAdapter.getItemCount] returns
     * the number of items added plus one, adds an empty collection and check if returns the number
     * if items added
     */
    @Test
    fun testHasLoadingAddingEmptyCollection() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        val firstPost = mock<Post>(Post::class.java)

        mAdapterPost!!.add(firstPost)
        assertEquals(mAdapterPost!!.getItemCount(), sNumberTwo)
        assertEquals(mAdapterPost!!.getItemViewType(sNumberZero), AbstractLoadMoreBaseAdapter.VIEW_TYPE_ITEM)
        assertEquals(mAdapterPost!!.getItemViewType(sNumberOne), AbstractLoadMoreBaseAdapter.VIEW_TYPE_LOAD)

        mAdapterPost!!.addAll(HashSet<Post>())
        assertEquals(mAdapterPost!!.getItemCount(), sNumberOne)
        assertEquals(mAdapterPost!!.getItemViewType(sNumberZero), AbstractLoadMoreBaseAdapter.VIEW_TYPE_ITEM)
    }

    /**
     *
     * Adds an item, test if the [getItemCount][AbstractLoadMoreBaseAdapter.getItemCount] returns
     * the number of items added plus one, use the method
     * [setIsMoreDataAvailable][AbstractLoadMoreBaseAdapter.setIsMoreDataAvailable] to set
     * variable isMoreDataAvailable false and check if returns the number if items added
     */
    @Test
    fun testHasLoadingSettingIsMoreDataAvailableFalse() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        val firstPost = mock<Post>(Post::class.java)

        mAdapterPost!!.add(firstPost)
        assertEquals(mAdapterPost!!.itemCount, sNumberTwo)
        assertEquals(mAdapterPost!!.getItemViewType(sNumberZero), AbstractLoadMoreBaseAdapter.VIEW_TYPE_ITEM)
        assertEquals(mAdapterPost!!.getItemViewType(sNumberOne), AbstractLoadMoreBaseAdapter.VIEW_TYPE_LOAD)

        mAdapterPost!!.setIsMoreDataAvailable(false)
        assertEquals(mAdapterPost!!.itemCount, sNumberOne)
        assertEquals(mAdapterPost!!.getItemViewType(sNumberZero), AbstractLoadMoreBaseAdapter.VIEW_TYPE_ITEM)
    }

    /**
     * Adds a collection and check if the data set retrieved from adapter matches
     */
    @Test
    fun testGetDataSet() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())
        testClear()

        val posts = ArrayList<Post>()

        for (i in 0 until sTotalPostCollection) {
            val post = mock<Post>(Post::class.java)

            `when`<Int>(post.mId).thenReturn(i)
            `when`<Int>(post.mUserId).thenReturn(i)
            `when`<String>(post.mTitle).thenReturn(sPostTitle + i.toString())
            `when`<String>(post.mBody).thenReturn(sPostBody + i.toString())

            posts.add(post)
        }

        mAdapterPost!!.addAll(posts)

        assertEquals(mAdapterPost!!.dataSet, posts)
    }

    /**
     *
     * Check if adapter is empty from an initialized collection without any item
     */
    @Test
    fun testEmpty() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        assertTrue(mAdapterPost!!.isEmpty)
    }

    /**
     *
     * Clears the adapter collection and check if it's empty
     */
    @Test
    fun testClear() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        mAdapterPost!!.clear()
        mAdapterPost!!.setIsMoreDataAvailable(false)
        assertEquals(mAdapterPost!!.itemCount, sNumberZero)
    }

}