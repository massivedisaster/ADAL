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

package com.massivedisaster.adal.tests.instrumented.suite.bus

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import com.massivedisaster.adal.samplekotlin.R
import com.massivedisaster.adal.samplekotlin.base.activity.ActivityToolbar
import com.massivedisaster.adal.samplekotlin.feature.bus.FragmentB
import com.massivedisaster.adal.tests.unit.suite.base.AbstractBaseTestSuite
import com.massivedisaster.adal.tests.utils.Constants
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

class BangBusTests : AbstractBaseTestSuite() {

    @Rule
    var activityTestRule = ActivityTestRule<ActivityToolbar>(ActivityToolbar::class.java, true, false)

    override fun setup() {
        //val intent  Ac=tivityCall.init(context!!, ActivityToolbar::class, FragmentA::class!!).intent
        //unlock(activityTestRule.activity)
    }

    override fun dispose() {

    }

    /**
     *
     * Subscribes on bang bus, opens another fragment and send a bang bus event with action to
     * the initial fragment, the result must match the expected message on txtResult TextView.
     */
    @Test
    fun testSendEventSubscribedWithAction() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        onView(withId(R.id.btnSubscribeOpenB)).perform(click())

        sleep(Constants.BASE_DELAY_SMALL.toLong())

        onView(withId(R.id.btnSendBangWithAction)).perform(click())

        sleep(Constants.BASE_DELAY_SMALL.toLong())

        onView(withText(FragmentB.BANG_MESSAGE_WITH_ACTION)).check(matches(isDisplayed()))
    }

    /**
     *
     * Subscribes on bang bus, opens another fragment and send a bang bus event without action to
     * the initial fragment, the result must match the expected message on txtResult TextView.
     */
    @Test
    fun testSendEventSubscribedWithoutAction() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        onView(withId(R.id.btnSubscribeOpenB)).perform(click())

        sleep(Constants.BASE_DELAY_SMALL.toLong())

        onView(withId(R.id.btnSendBangWithoutAction)).perform(click())

        sleep(Constants.BASE_DELAY_SMALL.toLong())

        onView(withText(FragmentB.BANG_NUMBER_WITHOUT_NUMBER.toString())).check(matches(isDisplayed()))
    }

    /**
     *
     * Unsubscribes on bang bus, opens another fragment and send a bang bus event with action to
     * the initial fragment, the method must throw an exception when try to find the expected TextView.
     */
    @Test(expected = NoMatchingViewException::class)
    fun testSendEventUnsubscribedWithAction() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        onView(withId(R.id.btnUnsubscribeOpenB)).perform(click())

        sleep(Constants.BASE_DELAY_SMALL.toLong())

        onView(withId(R.id.btnSendBangWithAction)).perform(click())

        sleep(Constants.BASE_DELAY_SMALL.toLong())

        onView(withText(FragmentB.BANG_MESSAGE_WITH_ACTION)).check(matches(isDisplayed()))
    }

    /**
     *
     * Unsubscribes on bang bus, opens another fragment and send a bang bus event without action to
     * the initial fragment, the method must throw an exception when try to find the expected TextView.
     */
    @Test(expected = NoMatchingViewException::class)
    fun testSendEventUnsubscribedWithoutAction() {
        sleep(Constants.BASE_DELAY_SMALL.toLong())

        onView(withId(R.id.btnUnsubscribeOpenB)).perform(click())

        sleep(Constants.BASE_DELAY_SMALL.toLong())

        onView(withId(R.id.btnSendBangWithoutAction)).perform(click())

        sleep(Constants.BASE_DELAY_SMALL.toLong())

        onView(withText(FragmentB.BANG_NUMBER_WITHOUT_NUMBER.toString())).check(matches(isDisplayed()))
    }
}