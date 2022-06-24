package com.example.currentlocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currentlocation.PermissionUtil.ACTION_START_FUSED_SERVICE
import com.example.currentlocation.PermissionUtil.ACTION_STOP_FUSED_SERVICE
import com.example.currentlocation.PermissionUtil.displayLocationSettingsRequest
import com.example.currentlocation.PermissionUtil.getLocationStatus
import com.example.currentlocation.databinding.ActivityMainBinding
import com.example.currentlocation.user.*
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first


class MainActivity : AppCompatActivity() {
private lateinit var binding: ActivityMainBinding
private lateinit var adapter: LocationAdapter
    private lateinit var studentViewModel: StudentViewModel
private lateinit var loginDataStore: LoginDataStore

    private var id ="0"
    private var user ="0"
    private var pass ="0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val studentRepository = UserRepository(UserDataBase.getDatabase(this))
        val factory = StudentFactory(studentRepository)
        studentViewModel = ViewModelProvider(this, factory)[StudentViewModel::class.java]
        loginDataStore= LoginDataStore(this)
        lifecycleScope.launchWhenStarted {
            id = loginDataStore.getId().first()
            user =loginDataStore.getUserName().first()
            pass = loginDataStore.getPassword().first()
            Log.i("TAG", "onCreateId:$id")
            Log.i("TAG", "onCreatePass:$pass")
            Log.i("TAG", "onCreateUser:$user")
        }




        binding.swipe.setOnRefreshListener{
            getLocaion()
            setRecycle()
        }

        updateCUrrentLocation()
        getLocaion()
        setRecycle()
        logout()
        adapter.locationListener={
            val intent= Intent(this,MapsActivity::class.java)
            intent.putExtra( "lat" ,it.lat)
            intent.putExtra( "lan" ,it.lan)
            intent.putExtra( "id" ,it.userId)
            startActivity(intent)
        }

    }

    private fun updateCUrrentLocation() {
        FusedLocationService.latitudeFlow.observe(this){
            val lat= it.latitude.toString()
            val longi = it.longitude.toString()
            lifecycleScope.launchWhenStarted {
                Log.i("TAG", "onCreateId:$id")
                studentViewModel.insertLocation(LocationTable(lat,longi,id))
            }

        }
    }

    private fun logout() {
        binding.logout.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                loginDataStore.clearValues()
                val intent = Intent(this@MainActivity, Login::class.java)
                startActivity(intent)
                finish()
            }

        }
    }


    private fun setRecycle() {
        adapter= LocationAdapter()
        binding.recycle.adapter=adapter
        binding.recycle.layoutManager= LinearLayoutManager(this)
    }

    private fun getLocaion() {
        lifecycleScope.launchWhenStarted {
            studentViewModel.getUserLocation(id)!!.collect{

                if(it.isNullOrEmpty()){
                    Log.i("TAG", "getLocaion: $it")
//                    adapter.differ.submitList(it)
                    binding.swipe.isRefreshing=false
                    binding.empty.visibility = View.VISIBLE

                } else {
                    try {
                        binding.empty.visibility = View.GONE
                        adapter.differ.submitList(it)
                        binding.swipe.isRefreshing=false
                    }catch (e:NullPointerException){
                        e.printStackTrace()
                    }

                }

            }
        }
    }

    private fun startLocationService() {

        Intent(this, FusedLocationService::class.java).also {
            it.action = ACTION_START_FUSED_SERVICE
            startService(it)
        }

    }

    private fun stopLocationServices() {

        Intent(this, FusedLocationService::class.java).also {
            it.action = ACTION_STOP_FUSED_SERVICE
            startService(it)
        }

    }


    override fun onResume() {
        super.onResume()
        startLocationService()
    }

    override fun onPause() {
        super.onPause()
        stopLocationServices()
    }


}