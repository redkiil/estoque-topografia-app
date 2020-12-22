package com.augusto.qrcode_estoque

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


private const val MOVE_TO_REQUEST_LAYOUT = 0
private const val MOVE_TO_SEARCH_LAYOUT = 1
private const val MOVE_TO_SETTINGS_LAYOUT = 2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        if(ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            Helper().askForCameraPermission(this@MainActivity)
        }
        findViewById<RelativeLayout>(R.id.newRequestLayout).setOnClickListener{handleClick(MOVE_TO_REQUEST_LAYOUT)}
        findViewById<RelativeLayout>(R.id.textViewLayout).setOnClickListener{handleClick(MOVE_TO_SEARCH_LAYOUT)}
        findViewById<RelativeLayout>(R.id.settingsRequestLayout).setOnClickListener{handleClick(MOVE_TO_SETTINGS_LAYOUT)}
    }


    private fun handleClick(which: Int){
        var intent = Intent()
        when(which){
            MOVE_TO_REQUEST_LAYOUT -> intent = Intent(this, RequestActivity::class.java)
            MOVE_TO_SEARCH_LAYOUT -> intent = Intent(this, SearchActivity::class.java)
            MOVE_TO_SETTINGS_LAYOUT -> intent = Intent(this, SettingsActivity::class.java)
        }
        startActivity(intent)
    }

}
