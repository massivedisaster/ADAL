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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BangBus {

    private final static String sArgumentData = "argument_bang_data";
    private Context mContext;
    private HashMap<Method, BroadcastReceiver> mBroadcastReceivers;

    private Object mObject;

    public BangBus(Context context) {
        mBroadcastReceivers = new HashMap<>();
        mContext = context;
    }

    public static BangBuilder with(Context context) {
        return new BangBuilder(context);
    }

    private static Set<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final Set<Method> methods = new HashSet<>();
        Class<?> klass = type;
        while(klass != Object.class) {
            final Set<Method> allMethods = new HashSet<>(Arrays.asList(klass.getDeclaredMethods()));
            for(final Method method : allMethods) {
                if(method.isAnnotationPresent(annotation)) {
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

        for(final Method method : methods) {
            if(mBroadcastReceivers.containsKey(method)) {
                return this;
            }

            BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        if(intent.hasExtra(sArgumentData)) {
                            Object object = intent.getSerializableExtra(sArgumentData);
                            method.setAccessible(true);
                            method.invoke(mObject, object);
                            method.setAccessible(false);
                        } else {
                            method.setAccessible(true);
                            method.invoke(mObject);
                            method.setAccessible(false);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            };

            mBroadcastReceivers.put(method, mBroadcastReceiver);

            String filter;

            Annotation annotation = method.getAnnotation(SubscribeBang.class);
            SubscribeBang subscribeBang = (SubscribeBang) annotation;

            if(!subscribeBang.action().isEmpty()) {
                filter = subscribeBang.action();
            } else {
                filter = method.getParameterTypes()[0].getCanonicalName();
            }

            if(filter != null) {
                LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastReceiver, new IntentFilter(filter));
            }
        }

        return this;
    }

    /**
     * Removes all methods subscribed in this instance
     */
    public void unsubscribe() {
        for(Map.Entry<Method, BroadcastReceiver> entry : mBroadcastReceivers.entrySet()) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(entry.getValue());
        }

        mBroadcastReceivers.clear();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface SubscribeBang {

        String action() default "";
    }

    public static class BangBuilder {

        private Context mContext;
        private List<String> mActions;
        private Serializable mParameter;

        public BangBuilder(Context context) {
            mContext = context;
            mActions = new ArrayList<>();
        }

        public BangBuilder addAction(String action) {
            mActions.add(action);
            return this;
        }

        public BangBuilder setParameter(Serializable parameter) {
            mParameter = parameter;
            return this;
        }

        public void bang() {
            if(mActions.isEmpty() && mParameter == null) {
                throw new MissingBangArgumentException();
            }

            if(mActions.isEmpty()) {
                Intent intent = new Intent(mParameter.getClass().getCanonicalName());
                intent.putExtra(sArgumentData, mParameter);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                return;
            }

            for(String action : mActions) {
                Intent intent = new Intent(action);

                if(mParameter != null) {
                    intent.putExtra(sArgumentData, mParameter);
                }

                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        }
    }
}
