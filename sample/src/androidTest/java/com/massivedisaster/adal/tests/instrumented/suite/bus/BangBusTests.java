/*
 * ADAL - A set of Android libraries to help speed up Android development.
 *
 * Copyright (c) 2017 ADAL
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

package com.massivedisaster.adal.tests.instrumented.suite.bus;

import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;

import com.massivedisaster.activitymanager.ActivityFragmentManager;
import com.massivedisaster.adal.sample.R;
import com.massivedisaster.adal.sample.base.activity.ActivityToolbar;
import com.massivedisaster.adal.sample.feature.bus.FragmentA;
import com.massivedisaster.adal.sample.feature.bus.FragmentB;
import com.massivedisaster.adal.tests.unit.suite.base.AbstractBaseTestSuite;
import com.massivedisaster.adal.tests.utils.Constants;

import org.junit.Rule;
import org.junit.Test;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * <b>BangBusTests class</b>
 * <p>
 * <p>Test suite to evaluate BangBus class methods and behaviours</p>
 * <p>
 * <b>Implemented tests:</b>
 * <p>
 * # <p>({@link #testSendEventSubscribedWithAction() testSendEventSubscribed} method)</p>
 * # <p>({@link #testSendEventSubscribedWithoutAction() testSendEventSubscribed} method)</p>
 * # <p>({@link #testSendEventUnsubscribedWithAction() testSendEventUnsubscribed} method) throws {@link NoMatchingViewException}</p>
 * # <p>({@link #testSendEventUnsubscribedWithoutAction() testSendEventUnsubscribed} method) throws {@link NoMatchingViewException}</p>
 */
public class BangBusTests extends AbstractBaseTestSuite {

    @Rule
    public ActivityTestRule<ActivityToolbar> activityTestRule = new ActivityTestRule<>(ActivityToolbar.class, true, false);

    @Override
    protected void setup() {
        Intent intent = ActivityFragmentManager.open(activityTestRule.getActivity(), ActivityToolbar.class, FragmentA.class).getIntent();

        activityTestRule.launchActivity(intent);
    }

    @Override
    protected void dispose() {

    }

    /**
     * <p>Subscribes on bang bus, opens another fragment and send a bang bus event with action to
     * the initial fragment, the result must match the expected message on txtResult TextView.</p>
     */
    @Test
    public void testSendEventSubscribedWithAction() {
        sleep(Constants.BASE_DELAY_SMALL);

        onView(withId(R.id.btnSubscribeOpenB)).perform(click());

        sleep(Constants.BASE_DELAY_SMALL);

        onView(withId(R.id.btnSendBangWithAction)).perform(click());

        sleep(Constants.BASE_DELAY_SMALL);

        onView(withText(FragmentB.BANG_MESSAGE_WITH_ACTION)).check(matches(isDisplayed()));
    }

    /**
     * <p>Subscribes on bang bus, opens another fragment and send a bang bus event without action to
     * the initial fragment, the result must match the expected message on txtResult TextView.</p>
     */
    @Test
    public void testSendEventSubscribedWithoutAction() {
        sleep(Constants.BASE_DELAY_SMALL);

        onView(withId(R.id.btnSubscribeOpenB)).perform(click());

        sleep(Constants.BASE_DELAY_SMALL);

        onView(withId(R.id.btnSendBangWithoutAction)).perform(click());

        sleep(Constants.BASE_DELAY_SMALL);

        onView(withText(String.valueOf(FragmentB.BANG_NUMBER_WITHOUT_NUMBER))).check(matches(isDisplayed()));
    }

    /**
     * <p>Unsubscribes on bang bus, opens another fragment and send a bang bus event with action to
     * the initial fragment, the method must throw an exception when try to find the expected TextView.</p>
     */
    @Test(expected = NoMatchingViewException.class)
    public void testSendEventUnsubscribedWithAction() {
        sleep(Constants.BASE_DELAY_SMALL);

        onView(withId(R.id.btnUnsubscribeOpenB)).perform(click());

        sleep(Constants.BASE_DELAY_SMALL);

        onView(withId(R.id.btnSendBangWithAction)).perform(click());

        sleep(Constants.BASE_DELAY_SMALL);

        onView(withText(FragmentB.BANG_MESSAGE_WITH_ACTION)).check(matches(isDisplayed()));
    }

    /**
     * <p>Unsubscribes on bang bus, opens another fragment and send a bang bus event without action to
     * the initial fragment, the method must throw an exception when try to find the expected TextView.</p>
     */
    @Test(expected = NoMatchingViewException.class)
    public void testSendEventUnsubscribedWithoutAction() {
        sleep(Constants.BASE_DELAY_SMALL);

        onView(withId(R.id.btnUnsubscribeOpenB)).perform(click());

        sleep(Constants.BASE_DELAY_SMALL);

        onView(withId(R.id.btnSendBangWithoutAction)).perform(click());

        sleep(Constants.BASE_DELAY_SMALL);

        onView(withText(String.valueOf(FragmentB.BANG_NUMBER_WITHOUT_NUMBER))).check(matches(isDisplayed()));
    }
}
