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

package com.massivedisaster.adal.bus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.massivedisaster.adal.utils.LogUtils;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Cominucation between activities, fragments and services.
 */
public class BangBus {

    public static final String ARGUMENT_DATA = "argument_bang_data";
    private final Context mContext;
    private final Map<Method, BroadcastReceiver> mBroadcastReceivers;
    protected Object mObject;

    /**
     * Creates a instance of {@link BangBus}.
     *
     * @param context the context.
     */
    public BangBus(Context context) {
        mBroadcastReceivers = new HashMap<>();
        mContext = context;
    }

    /**
     * Creates a builder for {@link BangBus}.
     *
     * @param context the context.
     * @return instance of {@link BangBuilder}.
     */
    public static BangBuilder with(Context context) {
        return new BangBuilder(context);
    }

    /**
     * Retrieves the methods annotated with <var>annotation</var> from class <var>type</var>.
     *
     * @param type       the class type.
     * @param annotation the annotation.
     * @return a ser of methods.
     */
    private static Set<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final Set<Method> methods = new HashSet<>();
        Class<?> klass = type;
        while (klass != Object.class) {
            final Set<Method> allMethods = new HashSet<>(Arrays.asList(klass.getDeclaredMethods()));
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
     * Subscribes all methods in the object passed.
     *
     * @param object the object to find methods to subscribe
     * @return BangBus instance
     */
    public BangBus subscribe(Object object) {
        mObject = object;

        Set<Method> methods = getMethodsAnnotatedWith(mObject.getClass(), SubscribeBang.class);

        for (final Method method : methods) {
            if (mBroadcastReceivers.containsKey(method)) {
                return this;
            }

            BroadcastReceiver mBroadcastReceiver = getBroadcastReceiver(method);

            mBroadcastReceivers.put(method, mBroadcastReceiver);

            String filter;

            Annotation annotation = method.getAnnotation(SubscribeBang.class);
            SubscribeBang subscribeBang = (SubscribeBang) annotation;

            if (!subscribeBang.action().isEmpty()) {
                filter = subscribeBang.action();
            } else {
                Class clazz = method.getParameterTypes()[0];
                filter = clazz.getCanonicalName();
                if (clazz.isPrimitive()) {
                    throw new BangIllegalArgumentException();
                }
            }

            if (filter != null) {
                LocalBroadcastManager.getInstance(mContext)
                        .registerReceiver(mBroadcastReceiver, new IntentFilter(filter));
            }
        }

        return this;
    }

    /**
     * Get a broadcast receiver for the <var>method</var>
     *
     * @param method the method to be analized.
     * @return s
     */
    @NonNull
    private BroadcastReceiver getBroadcastReceiver(final Method method) {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (intent.hasExtra(ARGUMENT_DATA)) {
                        Object object = intent.getSerializableExtra(ARGUMENT_DATA);
                        method.setAccessible(true);
                        method.invoke(mObject, object);
                        method.setAccessible(false);
                    } else {
                        method.setAccessible(true);
                        method.invoke(mObject);
                        method.setAccessible(false);
                    }
                } catch (IllegalAccessException e) {
                    LogUtils.logErrorException(BangBus.class, e);
                } catch (InvocationTargetException e) {
                    LogUtils.logErrorException(BangBus.class, e);
                }
            }
        };
    }

    /**
     * Removes all methods subscribed in this instance
     */
    public void unsubscribe() {
        for (Map.Entry<Method, BroadcastReceiver> entry : mBroadcastReceivers.entrySet()) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(entry.getValue());
        }

        mBroadcastReceivers.clear();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface SubscribeBang {
        String action() default "";
    }

    /**
     * Builder for {@link BangBus}.
     */
    public static class BangBuilder {

        private final Context mContext;
        private final List<String> mActions;
        private Serializable mParameter;

        /**
         * Create instance of {@link BangBuilder}
         *
         * @param context the context.
         */
        public BangBuilder(Context context) {
            mContext = context;
            mActions = new ArrayList<>();
        }

        /**
         * Adds a action to the builder.
         *
         * @param action name of the action.
         * @return the instance of {@link BangBuilder}.
         */
        public BangBuilder addAction(String action) {
            mActions.add(action);
            return this;
        }

        /**
         * Adds a parameter to the builder.
         *
         * @param parameter the parameter for the action.
         * @return the instance of {@link BangBuilder}.
         */
        public BangBuilder setParameter(Serializable parameter) {
            mParameter = parameter;
            return this;
        }

        /**
         * Sends the message.
         */
        public void bang() {
            if (mActions.isEmpty() && mParameter == null) {
                throw new BangMissingArgumentException();
            }

            if (mActions.isEmpty()) {
                Intent intent = new Intent(mParameter.getClass()
                        .getCanonicalName());
                intent.putExtra(BangBus.ARGUMENT_DATA, mParameter);
                LocalBroadcastManager.getInstance(mContext)
                        .sendBroadcast(intent);
                return;
            }

            for (String action : mActions) {
                Intent intent = new Intent(action);

                if (mParameter != null) {
                    intent.putExtra(BangBus.ARGUMENT_DATA, mParameter);
                }

                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        }
    }
}
