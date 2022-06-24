package com.example.currentlocation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.currentlocation.databinding.ActivityLoginBinding
import com.example.currentlocation.user.*
import kotlinx.coroutines.flow.first

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var studentViewModel: StudentViewModel
    var PERMISSION_ID = 44
    private lateinit var loginDataStore: LoginDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val studentRepository = UserRepository(UserDataBase.getDatabase(this))
        val factory1 = StudentFactory(studentRepository)
        studentViewModel = ViewModelProvider(this, factory1)[StudentViewModel::class.java]
        loginDataStore= LoginDataStore(this)

        binding.register.setOnClickListener {

            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }
        userLogin()
        loginValidation()

    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID
        )
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }
    private fun userLogin() {
        lifecycleScope.launchWhenStarted {
            val isLog = loginDataStore.isLoggedIn().first()
            if (isLog) {
                val intent = Intent(this@Login, MainActivity::class.java)
                startActivity(intent)
                finish()

            }
        }
    }

    fun Context.showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun loginValidation() {
        binding.apply {

            login.setOnClickListener {             when {
                email.text.toString().isEmpty() -> {
                    showToast(" Enter Email")
                }
                password.text.toString().isEmpty() -> {
                    showToast(" Enter Password")
                }
                else -> {
                    lifecycleScope.launchWhenStarted {

                        val email: String = email.text.toString().trim();
                        val password = password.text.toString().trim();

                        val user = studentViewModel.verifyUserLogin(email, password,)!!.collect {
                            Log.i("TAG", "loginValidation:$it ")
                            if (checkPermissions()) {

                                // check if location is enabled
                                if (isLocationEnabled()) {
                                    if (it != null) {
                                        showToast(" Login Successfully ")

                                        loginDataStore.saveLogin(true)
                                        loginDataStore.saveUserName(it.email)
                                        loginDataStore.savePassword(it.password)
                                        loginDataStore.saveId(it.id.toString())
                                        val intent = Intent(this@Login, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                        lifecycleScope.launchWhenStarted {



                                        }

                                    }
                                    else{
                                        showToast(" Enter Valid user name password ")
                                    }

                                }
                                else {
                                    Toast.makeText(this@Login, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                                    val  intent =  Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent)
                                }
                            }else {

                                requestPermissions()
                            }

                        }



                    }
                }
            }
            }
        }
    }

}