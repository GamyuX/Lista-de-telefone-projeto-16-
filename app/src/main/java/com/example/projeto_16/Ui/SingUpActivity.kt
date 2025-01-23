package com.example.projeto_16.Ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_16.R
import com.example.projeto_16.databinding.ActivitySingUpBinding
import dataBase.DBHelper

class SingUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this)

        binding.buttonLogin.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            val confirmPassword = binding.editConfirmPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {

                    val res = db.insertUser(username, password)
                    if (res > 0) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.usuario_registrado_com_successo),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }else{
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.erro_ao_cadastrar_usuario),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.editUsername.setText("")
                        binding.editPassword.setText("")
                        binding.editConfirmPassword.setText("")
                    }

                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.senha_incorreta),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                Toast.makeText(
                    applicationContext,
                    getString(R.string.preencha_todos_os_campos),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
