package com.example.projeto_16.Ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_16.R
import com.example.projeto_16.databinding.ActivityContactImageBinding

class ContactImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactImageBinding
    private lateinit var i: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContactImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        i = intent

        binding.avatarProfile1.setOnClickListener { sendID(R.drawable.avatar1) }
        binding.avatarProfile2.setOnClickListener { sendID(R.drawable.avatar2) }
        binding.avatarProfile3.setOnClickListener { sendID(R.drawable.avatar3) }
        binding.avatarProfile4.setOnClickListener { sendID(R.drawable.avatar4) }
        binding.avatarProfile5.setOnClickListener { sendID(R.drawable.avatar5) }
        binding.avatarProfile6.setOnClickListener { sendID(R.drawable.avatar6) }
        binding.removeImage.setOnClickListener { sendID(R.drawable.user) }
    }
    private fun sendID(id: Int){
        i.putExtra("id",id)
        setResult(RESULT_OK,i)
        finish()

    }
}