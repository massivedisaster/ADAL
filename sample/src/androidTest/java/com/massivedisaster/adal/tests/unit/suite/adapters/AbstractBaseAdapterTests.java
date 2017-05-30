/*
 * ADAL - A set of Android libraries to help speed up Android development.
 * Copyright (C) 2017 ADAL.
 *
 * ADAL is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or any later version.
 *
 * ADAL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with ADAL. If not, see <http://www.gnu.org/licenses/>.
 */

package com.massivedisaster.adal.tests.unit.suite.adapters;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.massivedisaster.adal.adapter.LoadMoreBaseAdapter;
import com.massivedisaster.adal.sample.feature.network.AdapterPost;
import com.massivedisaster.adal.sample.model.Post;
import com.massivedisaster.adal.tests.unit.suite.base.AbstractBaseTestSuite;
import com.massivedisaster.adal.tests.utils.Constants;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static android.os.SystemClock.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <b>AbstractBaseAdapterTests class</b>
 *
 * <p>Test suite to evaluate LoadMoreBaseAdapter class methods and behaviours</p>
 *
 * <p>Note: All {@link LoadMoreBaseAdapter#getItemCount() getItemCount} validations are considering
 * one more item into the collection because of loading view</p>
 *
 * <b>Implemented tests:</b>
 *
 * # <p>({@link #testAddItem() testAddItem} method)</p>
 * # <p>({@link #testAddItemAtPosition() testAddItemAtPosition} method)</p>
 * # <p>({@link #testAddCollection() testAddCollection} method)</p>
 * # <p>({@link #testRemoveItem() testRemoveItem} method)</p>
 * # <p>({@link #testRemoveItemAtPosition() testRemoveItemAtPosition} method)</p>
 * # <p>({@link #testRemoveCollection() testRemoveCollection} method)</p>
 * # <p>({@link #testGetItemOutOfBounds() testGetItemOutOfBounds} method) throws {@link IndexOutOfBoundsException}</p>
 * # <p>({@link #testGetItemEmptyAdapter() testGetItemEmptyAdapter} method) throws {@link IndexOutOfBoundsException}</p>
 * # <p>({@link #testHasLoadingAddingEmptyCollection() testHasLoadingAddingEmptyCollection} method)</p>
 * # <p>({@link #testHasLoadingSettingIsMoreDataAvailableFalse() testHasLoadingSettingIsMoreDataAvailableFalse} method)</p>
 * # <p>({@link #testGetDataSet() testGetDataSet} method)</p>
 * # <p>({@link #testEmpty() testEmpty} method)</p>
 * # <p>({@link #testClear() testClear} method)</p>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AbstractBaseAdapterTests extends AbstractBaseTestSuite {

    private static final int sTotalPostCollection = 5;

    private static final int sFirstPostId = 1;
    private static final int sFirstPostUserId = 111;
    private static final int sSecondPostId = 2;
    private static final int sSecondPostUserId = 222;

    private static final String sPostTitle = "Title";
    private static final String sPostBody = "Body";

    private static final int sNumberZero = 0;
    private static final int sNumberOne = 1;
    private static final int sNumberTwo = 2;
    private static final int sNumberThree = 3;

    private RecyclerView mRecyclerViewTest;
    private AdapterPost mAdapterPost;

    @Override
    protected void setup() {
        mRecyclerViewTest = new RecyclerView(getContext());
        mAdapterPost = new AdapterPost();
        mRecyclerViewTest.setAdapter(mAdapterPost);
        mRecyclerViewTest.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void dispose() {
        mAdapterPost.clear();
        mAdapterPost = null;
        mRecyclerViewTest = null;
    }

    /**
     * <p>Adds one item into the collection and test if the items count is correct</p>
     */
    @Test
    public void testAddItem() {
        sleep(Constants.BASE_DELAY_SMALL);
        testClear();

        Post firstPost = mock(Post.class);
        Post secondPost = mock(Post.class);
        Post thirdPost = mock(Post.class);
        int previousSize = mAdapterPost.getItemCount();

        mAdapterPost.add(firstPost);
        assertEquals(mAdapterPost.getItemCount(), previousSize + sNumberOne);

        mAdapterPost.add(secondPost);
        assertEquals(mAdapterPost.getItemCount(), previousSize + sNumberTwo);

        mAdapterPost.add(thirdPost);
        assertEquals(mAdapterPost.getItemCount(), previousSize + sNumberThree);
    }

    /**
     * <p>Adds one item into the collection in a specific position and test if the item at that
     * position matches with the expected item</p>
     */
    @Test
    public void testAddItemAtPosition() {
        sleep(Constants.BASE_DELAY_SMALL);

        Post firstPost = mock(Post.class);

        when(firstPost.getId()).thenReturn(sFirstPostId);
        when(firstPost.getUserId()).thenReturn(sFirstPostUserId);
        when(firstPost.getTitle()).thenReturn(sPostTitle.concat(String.valueOf(sFirstPostId)));
        when(firstPost.getBody()).thenReturn(sPostBody.concat(String.valueOf(sFirstPostId)));

        mAdapterPost.add(firstPost);

        Post itemOne = mAdapterPost.getItem(sNumberZero);

        assertEquals(itemOne.getId(), firstPost.getId());
        assertEquals(itemOne.getUserId(), firstPost.getUserId());
        assertEquals(itemOne.getTitle(), firstPost.getTitle());
        assertEquals(itemOne.getBody(), firstPost.getBody());

        Post secondPost = mock(Post.class);

        when(secondPost.getId()).thenReturn(sSecondPostId);
        when(secondPost.getUserId()).thenReturn(sSecondPostUserId);
        when(firstPost.getTitle()).thenReturn(sPostTitle.concat(String.valueOf(sSecondPostId)));
        when(firstPost.getBody()).thenReturn(sPostBody.concat(String.valueOf(sSecondPostId)));

        mAdapterPost.add(sNumberZero, secondPost);

        itemOne = mAdapterPost.getItem(sNumberZero);

        assertEquals(itemOne.getId(), secondPost.getId());
        assertEquals(itemOne.getUserId(), secondPost.getUserId());
        assertEquals(itemOne.getTitle(), secondPost.getTitle());
        assertEquals(itemOne.getBody(), secondPost.getBody());
    }

    /**
     * <p>Adds a collection of items and check if contains each added item</p>
     */
    @Test
    public void testAddCollection() {
        sleep(Constants.BASE_DELAY_SMALL);
        testClear();

        List<Post> posts = new ArrayList<>();

        for (int i = 0; i < sTotalPostCollection; i++) {
            Post post = mock(Post.class);

            when(post.getId()).thenReturn(i);
            when(post.getUserId()).thenReturn(i);
            when(post.getTitle()).thenReturn(sPostTitle.concat(String.valueOf(i)));
            when(post.getBody()).thenReturn(sPostBody.concat(String.valueOf(i)));

            posts.add(post);
        }

        mAdapterPost.addAll(posts);
        assertEquals(mAdapterPost.getItemCount(), sTotalPostCollection);

        for (Post post : posts) {
            assertTrue(mAdapterPost.containsItem(post));
        }
    }

    /**
     * <p>Adds and removes a specific item from a collection and check if the size is identical
     * previous size</p>
     */
    @Test
    public void testRemoveItem() {
        sleep(Constants.BASE_DELAY_SMALL);
        testClear();

        Post post = mock(Post.class);
        int previousSize = mAdapterPost.getItemCount();

        mAdapterPost.add(post);
        assertEquals(mAdapterPost.getItemCount(), sNumberOne);

        mAdapterPost.remove(post);
        assertEquals(mAdapterPost.getItemCount(), previousSize);
    }

    /**
     * <p>Adds and removes a specific item by it's position from a collection and check if the size
     * is identical previous size</p>
     */
    @Test
    public void testRemoveItemAtPosition() {
        sleep(Constants.BASE_DELAY_SMALL);
        testClear();

        Post post = mock(Post.class);
        int previousSize = mAdapterPost.getItemCount();

        mAdapterPost.add(post);
        assertEquals(mAdapterPost.getItemCount(), sNumberOne);

        mAdapterPost.remove(sNumberZero);
        assertEquals(mAdapterPost.getItemCount(), previousSize);
    }

    /**
     * <p>Adds and removes a collection from a collection and check if the size is identical
     * previous size</p>
     */
    @Test
    public void testRemoveCollection() {
        sleep(Constants.BASE_DELAY_SMALL);
        testClear();

        List<Post> posts = new ArrayList<>();
        int previousSize = mAdapterPost.getItemCount();

        for (int i = 0; i < sTotalPostCollection; i++) {
            Post post = mock(Post.class);

            when(post.getId()).thenReturn(i);
            when(post.getUserId()).thenReturn(i);
            when(post.getTitle()).thenReturn(sPostTitle.concat(String.valueOf(i)));
            when(post.getBody()).thenReturn(sPostBody.concat(String.valueOf(i)));

            posts.add(post);
        }

        mAdapterPost.addAll(posts);
        assertEquals(mAdapterPost.getItemCount(), sTotalPostCollection);

        mAdapterPost.removeAll(posts);
        assertEquals(mAdapterPost.getItemCount(), previousSize);
    }

    /**
     * <p>Get one item from the collection that doesn't exist</p>
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetItemOutOfBounds() {
        sleep(Constants.BASE_DELAY_SMALL);

        Post firstPost = mock(Post.class);
        Post secondPost = mock(Post.class);

        mAdapterPost.add(firstPost);
        mAdapterPost.add(secondPost);

        assertNotNull(mAdapterPost.getItem(sNumberZero));
        assertNotNull(mAdapterPost.getItem(sNumberOne));

        mAdapterPost.getItem(sNumberTwo);
    }

    /**
     * <p>Get one item from an empty collection</p>
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetItemEmptyAdapter() {
        sleep(Constants.BASE_DELAY_SMALL);
        testClear();

        mAdapterPost.getItem(sNumberZero);
    }

    /**
     * <p>Adds an item, test if the {@link LoadMoreBaseAdapter#getItemCount() getItemCount} returns
     * the number of items added plus one, adds an empty collection and check if returns the number
     * if items added</p>
     */
    @Test
    public void testHasLoadingAddingEmptyCollection() {
        sleep(Constants.BASE_DELAY_SMALL);

        Post firstPost = mock(Post.class);

        mAdapterPost.add(firstPost);
        assertEquals(mAdapterPost.getItemCount(), sNumberTwo);
        assertEquals(mAdapterPost.getItemViewType(sNumberZero), LoadMoreBaseAdapter.VIEW_TYPE_ITEM);
        assertEquals(mAdapterPost.getItemViewType(sNumberOne), LoadMoreBaseAdapter.VIEW_TYPE_LOAD);

        mAdapterPost.addAll(new HashSet<Post>());
        assertEquals(mAdapterPost.getItemCount(), sNumberOne);
        assertEquals(mAdapterPost.getItemViewType(sNumberZero), LoadMoreBaseAdapter.VIEW_TYPE_ITEM);
    }

    /**
     * <p>Adds an item, test if the {@link LoadMoreBaseAdapter#getItemCount() getItemCount} returns
     * the number of items added plus one, use the method
     * {@link LoadMoreBaseAdapter#setIsMoreDataAvailable(boolean) setIsMoreDataAvailable} to set
     * variable isMoreDataAvailable false and check if returns the number if items added</p>
     */
    @Test
    public void testHasLoadingSettingIsMoreDataAvailableFalse() {
        sleep(Constants.BASE_DELAY_SMALL);

        Post firstPost = mock(Post.class);

        mAdapterPost.add(firstPost);
        assertEquals(mAdapterPost.getItemCount(), sNumberTwo);
        assertEquals(mAdapterPost.getItemViewType(sNumberZero), LoadMoreBaseAdapter.VIEW_TYPE_ITEM);
        assertEquals(mAdapterPost.getItemViewType(sNumberOne), LoadMoreBaseAdapter.VIEW_TYPE_LOAD);

        mAdapterPost.setIsMoreDataAvailable(false);
        assertEquals(mAdapterPost.getItemCount(), sNumberOne);
        assertEquals(mAdapterPost.getItemViewType(sNumberZero), LoadMoreBaseAdapter.VIEW_TYPE_ITEM);
    }

    /**
     * Adds a collection and check if the data set retrieved from adapter matches
     */
    @Test
    public void testGetDataSet() {
        sleep(Constants.BASE_DELAY_SMALL);
        testClear();

        List<Post> posts = new ArrayList<>();

        for (int i = 0; i < sTotalPostCollection; i++) {
            Post post = mock(Post.class);

            when(post.getId()).thenReturn(i);
            when(post.getUserId()).thenReturn(i);
            when(post.getTitle()).thenReturn(sPostTitle.concat(String.valueOf(i)));
            when(post.getBody()).thenReturn(sPostBody.concat(String.valueOf(i)));

            posts.add(post);
        }

        mAdapterPost.addAll(posts);

        assertEquals(mAdapterPost.getDataSet(), posts);
    }

    /**
     * <p>Check if adapter is empty from an initialized collection without any item</p>
     */
    @Test
    public void testEmpty() {
        sleep(Constants.BASE_DELAY_SMALL);

        assertTrue(mAdapterPost.isEmpty());
    }

    /**
     * <p>Clears the adapter collection and check if it's empty</p>
     */
    @Test
    public void testClear() {
        sleep(Constants.BASE_DELAY_SMALL);

        mAdapterPost.clear();
        mAdapterPost.setIsMoreDataAvailable(false);
        assertEquals(mAdapterPost.getItemCount(), sNumberZero);
    }

}
