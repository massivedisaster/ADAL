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

package com.massivedisaster.adal.bus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BangBus {

    private final static String sArgumentData = "argument_bang_data";
    private Context mContext;
    private List<BroadcastReceiver> mLstBroadcastReceivers;
    private Object mObject;
    public BangBus(Context context) {
        mLstBroadcastReceivers = new LinkedList<>();
        mContext = context;
    }

    /**
     * Sends an object to another part of the application
     * All the methods subscribed with this data type will receive the object
     *
     * @param context      the application context
     * @param serializable the object to send
     */
    public static void bang(Context context, Serializable serializable) {
        Intent intent = new Intent(serializable.getClass().getCanonicalName());
        intent.putExtra(sArgumentData, serializable);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * Sends an object to another part of the application
     * All the methods subscribed with this name
     *
     * @param context the application context
     * @param name    the @annotation name
     */
    public static void bang(Context context, String name) {
        Intent intent = new Intent(name);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * Sends an object to another part of the application
     * All the methods subscribed with this name
     *
     * @param context      the application context
     * @param name         the annotation name
     * @param serializable the object to send
     */
    public static void bang(Context context, String name, Serializable serializable) {
        Intent intent = new Intent(name);

        intent.putExtra(sArgumentData, serializable);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<>();
        Class<?> klass = type;
        while (klass != Object.class) {
            final List<Method> allMethods = new ArrayList<>(Arrays.asList(klass.getDeclaredMethods()));
            for (final Method method : allMethods) {
                if (method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            klass = klass.getSuperclass();
        }
        return methods;
    }

    /**
     * Sends an object to another part of the application
     * All the methods subscribed with this data type will receive the object
     *
     * @param serializable the object to send
     */
    public void bang(Serializable serializable) {
        bang(mContext, serializable);
    }

    /**
     * Subscribes all methods in the object passed.
     *
     * @param object the object to find methods to subscribe
     * @return BangBus instance
     */
    public BangBus subscribe(Object object) {
        mObject = object;

        mLstBroadcastReceivers = new ArrayList<>();

        List<Method> lstMethods = getMethodsAnnotatedWith(mObject.getClass(), SubscribeBang.class);

        for (final Method m : lstMethods) {
            BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        if (intent.hasExtra(sArgumentData)) {
                            Object object = intent.getSerializableExtra(sArgumentData);
                            m.setAccessible(true);
                            m.invoke(mObject, object);
                            m.setAccessible(false);
                        } else {
                            m.setAccessible(true);
                            m.invoke(mObject);
                            m.setAccessible(false);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            };

            mLstBroadcastReceivers.add(mBroadcastReceiver);

            String filter;

            Annotation annotation = m.getAnnotation(SubscribeBang.class);
            SubscribeBang subscribeBang = (SubscribeBang) annotation;

            if (!subscribeBang.name().isEmpty()) {
                filter = subscribeBang.name();
            } else {
                filter = m.getParameterTypes()[0].getCanonicalName();
            }

            if (filter != null)
                LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastReceiver, new IntentFilter(filter));
        }

        return this;
    }

    /**
     * Removes all methods subscribed in this instance
     */
    public void unsubscribe() {
        for (BroadcastReceiver broadcastReceiver : mLstBroadcastReceivers) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(broadcastReceiver);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface SubscribeBang {

        String name() default "";
    }
}
