package com.example.currentlocation

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.currentlocation.PermissionUtil.ACTION_START_FUSED_SERVICE
import com.example.currentlocation.PermissionUtil.ACTION_STOP_FUSED_SERVICE
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY

class FusedLocationService : LifecycleService(){
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    companion object {
        val latitudeFlow = MutableLiveData<Location>()
    }


    @SuppressLint("VisibleForTests")
    override fun onCreate() {
        super.onCreate()

        fusedLocationProviderClient= FusedLocationProviderClient(applicationContext)

        locationRequest = LocationRequest.create().apply {
            interval = 15000L
            fastestInterval = 12000L
            priority = PRIORITY_HIGH_ACCURACY
        }


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                latitudeFlow.value = locationResult.lastLocation

            }
        }


    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (intent.action) {
                ACTION_START_FUSED_SERVICE -> {

                    requestLastLocation()
                }
                ACTION_STOP_FUSED_SERVICE -> {
                    stopRequestLocation()
                }
                else -> {
                    /*NO_OP*/
                }
            }
        }


        return super.onStartCommand(intent, flags, startId)
    }

    private fun requestLastLocation() {

        if (PermissionUtil.checkPermission(applicationContext)) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    latitudeFlow.value=it
                }
                requestCurrentLocation()
            }

        }

    }

    private fun stopRequestLocation() {
        stopSelf()
    }

    private fun requestCurrentLocation() {
        if (PermissionUtil.checkPermission(applicationContext)) {
            fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(applicationContext)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

}