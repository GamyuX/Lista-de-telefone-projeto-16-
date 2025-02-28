package com.example.projeto_16.Ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.projeto_16.Manifest
import com.example.projeto_16.Model.ContactModel
import com.example.projeto_16.R
import com.example.projeto_16.databinding.ActivityContactDetailBinding
import dataBase.DBHelper

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactDetailBinding
    private lateinit var db: DBHelper.DatabaseHelper
    private var contactModel = ContactModel()
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var imageId: Int = -1
    private val REQUEST_PHONE_CALL = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = intent
        val id = i.extras?.getInt("id", 0)
        val db = DBHelper.getInstance()

        if (id != null) {
            contactModel = db.getContact(id)
            populate()
        } else {
            finish()
        }

        binding.imageEmail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(contactModel.email))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Assunto")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Texto")
            try {
                startActivity(Intent.createChooser(emailIntent, "Escolha um aplicativo"))
            }catch (e:Exception){
                Toast.makeText(applicationContext, "Erro ao enviar email", Toast.LENGTH_SHORT).show()
            }
        }
        binding.imagePhone.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            )
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_PHONE_CALL
                )
            }else{
                val dialIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${contactModel.phone}"))
            }
        }

        binding.buttonBack.setOnClickListener {
            setResult(RESULT_OK, i)
            finish()
        }

        binding.buttonEdit.setOnClickListener {
            binding.layoutDeleteEdit.visibility = View.GONE
            binding.layoutEdit.visibility = View.VISIBLE
            changeEditText(true)
        }

        binding.buttonSave.setOnClickListener {


            val res = db.updateContact(

                id = contactModel.id,
                name = binding.editName.text.toString(),
                address = binding.editAddress.text.toString(),
                email = binding.editEmail.text.toString(),
                phone = binding.editPhone.text.toString().toInt(),
                imageId = imageId
            )
            if (res > 0) {
                Toast.makeText(
                    applicationContext,
                    "Contato atualizado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_OK, i)
                finish()
            } else {
                Toast.makeText(applicationContext, "Erro ao atualizar contato", Toast.LENGTH_SHORT)
                    .show()
                setResult(RESULT_CANCELED, i)
                finish()
            }
        }
        binding.buttonCancel.setOnClickListener {
            populate()
            changeEditText(false)

        }

        binding.buttonDelete.setOnClickListener {
            val res = db.deleteContact(contactModel.id)
            if (res > 0) {

                Toast.makeText(
                    applicationContext,
                    "Contato removido com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_OK, i)
                finish()
            } else {
                Toast.makeText(applicationContext, "Erro ao deletar contato", Toast.LENGTH_SHORT)
                    .show()
                setResult(RESULT_CANCELED, i)
                finish()
            }
        }

        binding.imageViewContact.setOnClickListener {
            if (binding.editName.isEnabled) {
                launcher.launch(
                    Intent(
                        applicationContext,
                        ContactImageActivity::class.java
                    )
                )
            }
        }
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.resultCode == RESULT_OK) {
                if (it.data?.extras != null){
                    imageId = it.data?.getIntExtra("id",0)!!
                    binding.imageViewContact.setImageResource(imageId!!)
                }
            } else {
                imageId = -1
                binding.imageViewContact.setImageResource(R.drawable.user)
            }
        }
    }
    private fun changeEditText(status: Boolean) {
        binding.editName.isEnabled = status
        binding.editAddress.isEnabled = status
        binding.editEmail.isEnabled = status
        binding.editPhone.isEnabled = status
    }

    private fun populate() {
            binding.editName.setText(contactModel.name)
            binding.editAddress.setText(contactModel.address)
            binding.editEmail.setText(contactModel.email)
            binding.editPhone.setText(contactModel.phone.toString())
        if (contactModel.imageId > 0) {
            binding.imageViewContact.setImageResource(imageId)
        } else {
            binding.imageViewContact.setImageResource(R.drawable.user)
        }
    }
}