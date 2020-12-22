package com.augusto.qrcode_estoque

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
private const val requestCodeCameraPermission = 1001
class Helper: AppCompatActivity(){
    fun askForCameraPermission(activity: Activity){
        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.INTERNET),requestCodeCameraPermission)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext,"permission granted, restart app", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext,"permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
