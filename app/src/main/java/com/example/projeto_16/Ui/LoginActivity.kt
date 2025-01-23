package com.example.projeto_16.Ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_16.R
import com.example.projeto_16.databinding.ActivityLoginBinding
import dataBase.DBHelper
import kotlin.text.isNotEmpty
import kotlin.text.isNotEmpty as isNotEmpty1

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: DBHelper.DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

         db = DBHelper.getInstance(this)

        sharedPreferences = application.getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        if (username != null) {
            if (username.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))

            }
        }

        binding.buttonLogin.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            val logged = binding.checkBoxLogged.isChecked

            if (username.isNotEmpty1() && password.isNotEmpty1()) {

                if (db.login(username, password)) {
                    if (logged) {
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("username", username)
                        editor.putString("password", password)
                        editor.apply()
                    }

                    startActivity(Intent(this, MainActivity::class.java))
                    //finish()

                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.login_falhou), Toast.LENGTH_SHORT
                    ).show()
                    binding.editUsername.setText("")
                    binding.editPassword.setText("")
                }
            } else {
                Toast.makeText(applicationContext, "Preencha todos os campos", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.textRegister.setOnClickListener {
                startActivity(Intent(this, SingUpActivity::class.java))
            }

            binding.RecoverPassword.setOnClickListener { }
        }
    }
}