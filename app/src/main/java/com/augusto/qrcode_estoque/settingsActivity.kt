package com.augusto.qrcode_estoque

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        findViewById<Button>(R.id.button).setOnClickListener {
            val input = findViewById<EditText>(R.id.username)
            if(input.text.isEmpty()){
                val toas = Toast.makeText(applicationContext, "Campo de usuario vazio!", Toast.LENGTH_SHORT)
                toas.setGravity(Gravity.CENTER,0,0)
                toas.show()
            }else{
                val prefre = applicationContext.getSharedPreferences("SETTINGS", MODE_PRIVATE)
                val edit = prefre.edit()
                edit.putString("user", input.text.toString())
                edit.apply()
            }

        }
    }
}