package io.github.mkohm.demo.lcom

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.airthings.utilities.PermissionHelper
import com.google.android.gms.location.LocationServices
import kotlin.coroutines.suspendCoroutine

class LocationServiceRepository(
    val context: Context,
    private val locationManager: LocationManager
) : LocationListener {

    var lastKnownLocation: Location? = null
        private set

    private var listener: LocationUpdateListener? = null

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context)

    interface LocationUpdateListener {
        /**
         * Called when the location has changed.
         *
         * @param location The new location
         */
        fun onLocationChanged(location: Location)
    }

    override fun onLocationChanged(location: Location?) {
        lastKnownLocation = location
        if (null != location) {
            listener?.onLocationChanged(location)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        // NO-OP
    }

    override fun onProviderEnabled(provider: String?) {
        // NO-OP
    }

    override fun onProviderDisabled(provider: String?) {
        // NO-OP
    }

    /**
     * This will listen for updates to the GPS location and will update the last known location property.
     */
    fun possiblyRequestLocationUpdates(
        interval: Long = 60_000L,
        minimumDistance: Float = 100F,
        listener: LocationUpdateListener? = null
    ) {
        if (locationPermissionGranted()) {
            stopLocationUpdates()

            this.listener = listener

            @Suppress("MissingPermission")
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                interval,
                minimumDistance,
                this
            )
        }
    }

    /**
     * Stop listening for location changes.
     */
    fun stopLocationUpdates() = locationManager.removeUpdates(this)

    @Suppress("unused")
    fun requestLocationPermissionsForFragment(fragment: Fragment, requestCode: Int) {
        fragment.requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            requestCode
        )
    }

    @Suppress("unused")
    fun requestLocationPermissionsForActivity(activity: Activity, requestCode: Int): Boolean {
        if (locationServicesEnabled() && !locationPermissionGranted()) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                requestCode
            )
            return true
        }
        return false
    }

    fun locationServicesEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            // Deprecated in API 28.
            val mode = Settings.Secure.getInt(
                context.contentResolver,
                @Suppress("DEPRECATION") Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )

            // Result.
            Settings.Secure.LOCATION_MODE_OFF != mode
        } else {
            // New in API 28.
            locationManager.isLocationEnabled
        }
    }

    fun locationPermissionGranted() = PermissionHelper.arePermissionsGranted(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    /**
     * Checks if we have permission to access users location (eg: to scan for bluetooth)
     * while the app is in background.
     *
     * @return true if necessary permissions are granted
     */
    fun backgroundLocationPermissionGranted(): Boolean = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        PermissionHelper.arePermissionsGranted(this.context, Manifest.permission.ACCESS_FINE_LOCATION)
    } else {
        PermissionHelper.arePermissionsGranted(this.context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    suspend fun getCurrentLocation() = suspendCoroutine<Location?> { cont ->
        fusedLocationClient.lastLocation
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    cont.resumeWith(Result.success(task.result))
                } else {
                    cont.resumeWith(
                        Result.failure(
                            task.exception ?: IllegalStateException(
                                "Failed to get last known location, " +
                                        "task failed without providing error info"
                            )
                        )
                    )
                }
            }
    }

    /*
    ? {
        try {
            val location = locationManager.getLastKnownLocation(Manifest.permission.ACCESS_FINE_LOCATION)
            if (null != location) {
                lastKnownLocation = location
            }
        } catch (e: SecurityException) {
            if (BuildConfig.DEBUG) {
                @Suppress("PrintStackTrace")
                e.printStackTrace()
            }
        }
        return lastKnownLocation
    }
     */
}
