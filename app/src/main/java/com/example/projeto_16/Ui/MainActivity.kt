package com.example.projeto_16.Ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_16.ContactDetailActivity
import com.example.projeto_16.Model.ContactModel
import com.example.projeto_16.NewContactActivity
import com.example.projeto_16.databinding.ActivityMainBinding
import dataBase.DBHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contactList: ArrayList <ContactModel>
    private lateinit var adapter: ArrayAdapter <ContactModel>
    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var db: DBHelper.DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        db = DBHelper.getInstance(this)
        val sharedPreferences = application.getSharedPreferences("LoginPrefs", MODE_PRIVATE)

        loadList()

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogout.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("username", "")
            editor.apply()
            finish()
        }

        loadList()

        binding.listViewContacts.setOnItemClickListener { _, _, position, _ ->
            /*Toast.makeText(applicationContext, contactList[position].name, Toast.LENGTH_SHORT).show()*/
            val intent = Intent(applicationContext, ContactDetailActivity::class.java)
            intent.putExtra("id", contactList[position].id)
            //startActivity(intent)
            result.launch(intent)
        }
        binding.buttonAdd.setOnClickListener {
            result.launch(Intent(applicationContext, NewContactActivity::class.java))
        }
        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data!=null && it.resultCode == RESULT_OK) {
                contactList = db.getAllContact()
                adapter.notifyDataSetChanged()

            }else if (it.resultCode == RESULT_CANCELED) {
                Toast.makeText(applicationContext, " Operação Cancelada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadList() {

        contactList = db.getAllContact()

        adapter =
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_list_item_1,
                contactList
            )
        binding.listViewContacts.adapter = adapter
    }
}