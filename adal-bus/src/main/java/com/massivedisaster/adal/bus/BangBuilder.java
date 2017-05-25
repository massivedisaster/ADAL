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

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BangBuilder {

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
            intent.putExtra(BangBus.ARGUMENT_DATA, mParameter);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            return;
        }

        for(String action : mActions) {
            Intent intent = new Intent(action);

            if(mParameter != null) {
                intent.putExtra(BangBus.ARGUMENT_DATA, mParameter);
            }

            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }
    }
}
