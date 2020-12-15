package com.augusto.qrcode_estoque

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


private val requestCodeCameraPermission = 1001;
private val MOVE_TO_REQUEST_LAYOUT = 0;
private val MOVE_TO_SEARCH_LAYOUT = 1;
private val MOVE_TO_SETTINGS_LAYOUT = 2;

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        if(ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            askForCameraPermission()
        }else{

           // setupControls()
            //Log.i("app", "setupControls")
        }
        findViewById<RelativeLayout>(R.id.newRequestLayout).setOnClickListener{handleClick(MOVE_TO_REQUEST_LAYOUT)}
        findViewById<RelativeLayout>(R.id.textViewLayout).setOnClickListener{handleClick(MOVE_TO_SEARCH_LAYOUT)}
        findViewById<RelativeLayout>(R.id.settingsRequestLayout).setOnClickListener{handleClick(MOVE_TO_SETTINGS_LAYOUT)}
    }

    private fun askForCameraPermission(){
        ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.INTERNET),requestCodeCameraPermission)
    }
    private fun handleClick(which: Int){
        var intent: Intent = Intent();
        when(which){
            MOVE_TO_REQUEST_LAYOUT -> intent = Intent(this, RequestActivity::class.java)
            MOVE_TO_SEARCH_LAYOUT -> intent = Intent(this, SearchActivity::class.java)
            MOVE_TO_SETTINGS_LAYOUT -> intent = Intent(this, SettingsActivity::class.java)
        }
        startActivity(intent)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

            }else{
                Toast.makeText(applicationContext,"permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
