package org.example.project.service

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import org.koin.java.KoinJavaComponent.inject

actual class LocationService {

    private val context: Context by inject(Context::class.java)

    actual fun getCurrentLocation(gotLocation: (Location?) -> Unit) {

        //si on n'a pas la permission
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            gotLocation(null)
        }

        LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener {
            gotLocation(Location(it.latitude, it.longitude))
        }.addOnFailureListener {
            it.printStackTrace()
            gotLocation(null)
        }

    }
}