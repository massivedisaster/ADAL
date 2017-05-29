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
 *
 * <p>Test suite to evaluate BangBus class methods and behaviours</p>
 *
 * <b>Implemented tests:</b>
 *
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
        Intent intent = new Intent();

        intent.putExtra(ActivityFragmentManager.ACTIVITY_MANAGER_FRAGMENT, FragmentA.class.getCanonicalName());
        intent.putExtra(ActivityFragmentManager.ACTIVITY_MANAGER_FRAGMENT_TAG, FragmentA.class.getCanonicalName());

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
