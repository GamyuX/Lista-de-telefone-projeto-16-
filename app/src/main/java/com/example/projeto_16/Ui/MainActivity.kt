package com.example.projeto_16.Ui

import adapter.ContactListAdapter
import adapter.listener.ContactOnClickListener
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_16.Model.ContactModel
import com.example.projeto_16.databinding.ActivityMainBinding
import dataBase.DBHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contactList: List <ContactModel>
    private var ascDesc: Boolean = true
    // private lateinit var adapter: ArrayAdapter <ContactModel>
    private lateinit var adapter: ContactListAdapter
    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var db: DBHelper.DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        db = DBHelper.getInstance()
        val sharedPreferences = application.getSharedPreferences("LoginPrefs", MODE_PRIVATE)

        binding.recyclerViewContacts.layoutManager = LinearLayoutManager(applicationContext)

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
        /*binding.listViewContacts.setOnItemClickListener { _, _, position, _ ->
            /*Toast.makeText(applicationContext, contactList[position].name, Toast.LENGTH_SHORT).show()*/
            val intent = Intent(applicationContext, ContactDetailActivity::class.java)
            intent.putExtra("id", contactList[position].id)
            //startActivity(intent)
            result.launch(intent)
        }*/

        binding.buttonAdd.setOnClickListener {
            result.launch(Intent(applicationContext, NewContactActivity::class.java))
        }

        binding.buttonOrder.setOnClickListener {
            if (ascDesc) {
                binding.buttonOrder.setImageResource(android.R.drawable.arrow_up_float)
            }else{
                binding.buttonOrder.setImageResource(android.R.drawable.arrow_down_float)
            }
            ascDesc = !ascDesc
            if (ascDesc) {
                contactList = contactList.reversed()
                placeAdapter()
            }
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

    private fun placeAdapter() {
        adapter = ContactListAdapter(contactList, ContactOnClickListener { contact ->
            val i = Intent(applicationContext, ContactDetailActivity::class.java)
            intent.putExtra("id", contact.id)
            result.launch(intent)
        })
        binding.recyclerViewContacts.adapter = adapter
    }
    private fun loadList() {
        contactList = db.getAllContact().sortedWith(compareBy { it.name })
        placeAdapter()

        /*contactList = db.getAllContact()
        adapter =
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_list_item_1,
                contactList
            )
        binding.listViewContacts.adapter = adapter */
    }
}