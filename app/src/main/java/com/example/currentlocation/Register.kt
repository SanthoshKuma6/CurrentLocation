package com.example.currentlocation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.currentlocation.databinding.ActivityRegisterBinding
import com.example.currentlocation.user.*

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var studentViewModel: StudentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val studentRepository = UserRepository(UserDataBase.getDatabase(this))
        val factory = StudentFactory(studentRepository)
        studentViewModel = ViewModelProvider(this, factory)[StudentViewModel::class.java]

        registerValidation()
    }
    fun Context.showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    private fun registerValidation() {
        binding.apply {
            register.setOnClickListener {
                when{
                    email.text.toString().isEmpty()->{
                        showToast("Enter Email Address")
                    }
                    password.text.toString().isEmpty()->{
                        showToast("Enter  Password")
                    }
                    else->{
                        lifecycleScope.launchWhenStarted {
                            val email = email.text.toString().trim();
                            val password = password.text.toString().trim();
                            val user = studentViewModel.findByEmail(email)!!.collect {
                                Log.i("TAG", "registerValidation:$it ")
                                if (it == null) {
                                    studentViewModel.register(UserTable(email,password))
                                    showToast(" Register Successfully ")
                                    binding.email.setText("")
                                    binding.password.setText("")
                                    val intent = Intent(this@Register, Login::class.java)
                                    startActivity(intent)
                                }  else{
                                    showToast(" User Name Already Register ")
                                }
                            }


                        }


                    }
                }
            }
        }
    }
}