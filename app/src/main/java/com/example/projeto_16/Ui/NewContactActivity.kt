package com.example.projeto_16.Ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_16.R
import com.example.projeto_16.databinding.ActivityNewContactBinding
import dataBase.DBHelper

class NewContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewContactBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var id: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper.getInstance()
        val i = intent

        binding.buttonAdd.setOnClickListener {
            val name = binding.editName.text.toString()
            val address = binding.editAddress.text.toString()
            val email = binding.editEmail.text.toString()
            val phone = binding.editPhone.text.toString().toInt()
            var imageId = -1
            if (id!= null) {
                imageId = id as Int
            }

            if (name.isNotEmpty() && address.isNotEmpty() && email.isNotEmpty()) {
                val res = db.insertContact(name, address, email, phone, imageId)
                if (res > 0) {
                    Toast.makeText(applicationContext, "Contato adicionado com sucesso", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK,i)

                    finish()
                }
            } else {
                Toast.makeText(applicationContext, "Erro ao adicionar contato", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonCancel.setOnClickListener {
            setResult(RESULT_CANCELED,i)
            finish()
        }

        binding.imageViewContact.setOnClickListener {
            launcher.launch(Intent(applicationContext, ContactImageActivity::class.java))
        }
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.data!= null && it.resultCode == RESULT_OK){
                id= it.data?.extras?.getInt("id")
                binding.imageViewContact.setImageResource(id!!)

            } else {

                id = -1
                binding.imageViewContact.setImageResource(R.drawable.user)

            }
        }
    }
}