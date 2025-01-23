package com.example.projeto_16

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_16.Model.ContactModel
import com.example.projeto_16.databinding.ActivityContactDetailBinding
import dataBase.DBHelper

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactDetailBinding
    private lateinit var db: DBHelper
    private var contactModel = ContactModel()
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var imageId: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = intent
        val id = i.extras?.getInt("id", 0)
        db = DBHelper()

        if (id != null) {
            contactModel = db.getContact(id)
            populate()
        } else {
            finish()
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
                imageId = contactModel.imageId
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
            launcher.launch(Intent(applicationContext, ContactImageActivity::class.java))
        }
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.resultCode == RESULT_OK) {
                imageId = it.data?.extras?.getInt("id")
                binding.imageViewContact.setImageDrawable(resources.getDrawable(imageId!!))

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

            binding.imageViewContact.setImageDrawable(resources.getDrawable(contactModel.imageId))

        } else {

            binding.imageViewContact.setImageResource(R.drawable.user)

        }
    }
}