package com.example.currentlocation

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.currentlocation.databinding.ActivityMapsBinding
import com.example.currentlocation.user.StudentFactory
import com.example.currentlocation.user.StudentViewModel
import com.example.currentlocation.user.UserDataBase
import com.example.currentlocation.user.UserRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var studentViewModel: StudentViewModel
    private var lat =""
    private var lan =""
    private var id =""
    private var address =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val studentRepository = UserRepository(UserDataBase.getDatabase(this))
        val factory = StudentFactory(studentRepository)
        studentViewModel = ViewModelProvider(this, factory)[StudentViewModel::class.java]
         lat = intent.getStringExtra("lat")!!
         lan = intent.getStringExtra("lan")!!
         id = intent.getStringExtra("id")!!
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        onLocationChanged()

        binding.history.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                getHistory(mMap)

            }
        }
    }

    suspend fun getHistory(googleMap: GoogleMap) {

        lifecycleScope.launchWhenStarted {
            studentViewModel.getUserLocation(id)!!.collect{

                for (i in it!!){
                    mMap = googleMap
                    Log.i("TAG", "getLocaion: $it")

                    // Add a marker in Sydney and move the camera

                    val sydney = LatLng(i.lat.toDouble(), i.lan.toDouble())
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

                    val numMarkersInRainbow = 12
                    for (j in 0 until numMarkersInRainbow) {
                        mMap.addMarker(
                            MarkerOptions()
                                .position(
                                    LatLng(
                                        i.lat.toDouble() ,
                                        i.lan.toDouble())
                                    )

                                .title("Marker $i")
                                .icon(BitmapDescriptorFactory.defaultMarker((j * 360 / numMarkersInRainbow).toFloat()))
                        )
                    }
                }

            }
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(lat.toDouble(), lan.toDouble())
        mMap.addMarker(MarkerOptions().position(sydney).title(address))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun onLocationChanged() {
        Toast.makeText(this, "$lat,$lan", Toast.LENGTH_SHORT).show()
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(lat.toDouble(), lan.toDouble(), 1)
            address = addresses[0].getAddressLine(0)
            Log.i("TAG", "onLocationChanged: $address")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}