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

package com.massivedisaster.adal.samplekotlin.feature.location

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.massivedisaster.adal.fragment.BaseFragment
import com.massivedisaster.adal.samplekotlin.R
import com.massivedisaster.location.LocationManager
import com.massivedisaster.location.listener.OnLocationManager
import com.massivedisaster.location.utils.LocationError

class FragmentLocation : BaseFragment() {

    private var mLocationManager: LocationManager? = null

    private var mBtnGetLocation: Button? = null
    private var mBtnGetLocationUpdates: Button? = null
    private var mTxtInfo: TextView? = null
    private var mTxtInfoUpdates: TextView? = null
    private var mLocationUpdates = ""

    override fun getFromBundle(bundle: Bundle) {
        // Intended.
    }

    override fun restoreInstanceState(savedInstanceState: Bundle?) {
        // Intended.
    }

    override fun layoutToInflate(): Int {
        return R.layout.fragment_location
    }

    override fun doOnCreated() {
        activity!!.setTitle(R.string.sample_location)

        mBtnGetLocation = findViewById(R.id.btnGetLocation)
        mTxtInfo = findViewById(R.id.txtInfo)

        mBtnGetLocationUpdates = findViewById(R.id.btnGetLocationUpdates)
        mTxtInfoUpdates = findViewById(R.id.txtInfoUpdates)

        initialize()
    }

    fun initialize() {
        mBtnGetLocation!!.setOnClickListener { v ->
            mTxtInfo!!.text = "Getting location..."
            v.isEnabled = false
            getLocation()
        }
        mBtnGetLocationUpdates!!.setOnClickListener({ v ->
            if (v.isSelected) {
                mBtnGetLocationUpdates!!.setText(R.string.start_location_updates)
                mBtnGetLocationUpdates!!.isSelected = false
                mLocationManager!!.stopRequestLocation()
                mLocationUpdates = ""
            } else {
                mTxtInfoUpdates!!.text = "Getting location..."
                v.isEnabled = false
                getLocationUpdates()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLocationManager = LocationManager()
        mLocationManager!!.onCreate(this)
    }

    override fun onDestroy() {
        mLocationManager!!.onDestroy()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        mLocationManager!!.onRequestPermissionsResult(requestCode, permissions, *grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mLocationManager!!.onActivityResult(requestCode, resultCode)
    }

    private fun getLocation() {
        mLocationManager!!.requestSingleLocation(false, object : OnLocationManager {

            override fun onLocationFound(location: Location, isLastKnowLocation: Boolean) {
                mTxtInfo!!.text = getLocation(location)
                mBtnGetLocation!!.isEnabled = true
            }

            override fun onLocationError(locationError: LocationError) {
                when (locationError) {
                    LocationError.DISABLED -> mTxtInfo!!.text = "Error: Location disabled"
                    LocationError.TIMEOUT -> mTxtInfo!!.text = "Error: Timeout getting location"
                    LocationError.REQUEST_UPDATES_ENABLED -> mTxtInfo!!.text = "Error: Request Updates are enabled"
                    else -> {
                    }
                }

                mBtnGetLocation!!.isEnabled = true
            }

            override fun onPermissionsDenied() {
                mTxtInfo!!.text = "Permissions Denied"
                mBtnGetLocation!!.isEnabled = true
            }

            override fun onProviderEnabled() {
                // Intended.
            }

            override fun onProviderDisabled() {
                // Intended.
            }

            override fun onStopRequestUpdate() {

            }
        })
    }

    private fun getLocationUpdates() {
        mLocationManager!!.requestLocationUpdates(object : OnLocationManager {
            override fun onLocationFound(location: Location, isLastKnowLocation: Boolean) {

                if (!mBtnGetLocationUpdates!!.isSelected) {
                    mBtnGetLocationUpdates!!.setText(R.string.stop_location_updates)
                    mBtnGetLocationUpdates!!.isSelected = true
                }

                mLocationUpdates += getLocation(location) + "\n"

                mTxtInfoUpdates!!.text = mLocationUpdates
                mBtnGetLocationUpdates!!.isEnabled = true
            }

            override fun onLocationError(locationError: LocationError) {
                when (locationError) {
                    LocationError.DISABLED -> mTxtInfoUpdates!!.text = "Error: Location disabled"
                    LocationError.TIMEOUT -> mTxtInfoUpdates!!.text = "Error: Timeout getting location"
                    else -> {
                    }
                }

                mBtnGetLocationUpdates!!.setEnabled(true)
            }

            override fun onPermissionsDenied() {
                mTxtInfoUpdates!!.text = "Permissions Denied"
                mBtnGetLocationUpdates!!.isEnabled = true
            }

            override fun onProviderEnabled() {
                // Intended.
            }

            override fun onProviderDisabled() {
                // Intended.
            }

            override fun onStopRequestUpdate() {
                mLocationUpdates += "STOP - Request Updates"
                mTxtInfoUpdates!!.setText(mLocationUpdates)
            }
        })
    }

    private fun getLocation(location: Location): String {
        return "Location found " + location.latitude + ", " + location.longitude
    }
}