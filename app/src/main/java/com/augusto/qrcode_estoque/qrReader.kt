package com.augusto.qrcode_estoque


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.qr_reader.*
import kotlinx.android.synthetic.main.settings.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val requestCodeReader = 1002

class QrReader : AppCompatActivity() {
    private lateinit var cameraSource: CameraSource
    private lateinit var detector: BarcodeDetector
    private var idback = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_reader)
        if(ContextCompat.checkSelfPermission(this@QrReader, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            Helper().askForCameraPermission(this@QrReader)
        }else{
            setupControls()
        }
        searchButton.setOnClickListener {getDataByCode()}
        buttonSend.setOnClickListener{finished()}
        Log.d("app", intent.extras.toString())
        idback = intent.extras?.getInt("field") ?: 0
        Log.d("app", "Received $idback")
    }

    private fun getDataByCode(){
        if(codeInput.text.isNotEmpty()){
            progressBar.visibility = View.VISIBLE
            val retrofitClient = NetworkUtils.getRetrofitInstance()
            val endPoint = retrofitClient.create(RestApi::class.java)
            val callback = endPoint.getItemInfo(Integer.parseInt(codeInput.text.toString()))
            callback.enqueue(object: Callback<ListItem> {
                override fun onResponse(call: Call<ListItem>, response: Response<ListItem>) {
                    progressBar.visibility = View.INVISIBLE
                    resultText.visibility = View.VISIBLE

                  if(response.code() == 404){
                      resultText.text = "Item não encontrado"
                  }else {
                      try{
                          val item = response.body()
                          val text = "${item!!.name} <br> Em estoque: ${item!!.instock}"
                          resultText.text = HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_LEGACY)
                          containerInfoItem.requestLayout()
                          Log.d("app", item.toString())
                          buttonSend.visibility = View.VISIBLE
                      }catch (exception: Exception){
                          Toast.makeText(applicationContext, "something went wrong with surface created", Toast.LENGTH_SHORT).show()
                      }
                  }
                }

                override fun onFailure(call: Call<ListItem>, t: Throwable) {
                    Toast.makeText(applicationContext, "Falha ao obter os dados ${t.message}", Toast.LENGTH_LONG).show()
                }

            })


        }
        val hidekey = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hidekey.hideSoftInputFromWindow(codeInput.windowToken, 0)
    }
    private fun finished(){
        val intent = Intent()
        intent.putExtra("returnfield", idback)
        intent.putExtra("codeid", codeInput.text.toString())
        setResult(requestCodeReader, intent)
        finish()
    }
    private fun setupControls(){
        detector = BarcodeDetector.Builder(baseContext).setBarcodeFormats(Barcode.QR_CODE).build()
        cameraSource = CameraSource.Builder(baseContext, detector).setAutoFocusEnabled(true).build()
        cameraSurfaceView.holder.addCallback(surgaceCallback)
        detector.setProcessor(processor)

    }

    private val surgaceCallback = object: SurfaceHolder.Callback{
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            cameraSource.stop()
        }

        @SuppressLint("MissingPermission")
        override fun surfaceCreated(holder: SurfaceHolder?) {
            try{
                cameraSource.start(holder)
                Log.i("app", "cameraSource start")
            }catch (exception: Exception){
                Toast.makeText(applicationContext, "something went wrong with surface created", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private val processor = object: Detector.Processor<Barcode>{
        override fun release() {
            print("release start")
        }

        override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
            if(detections != null && detections.detectedItems.size()>0){
                val qrCodes:SparseArray<Barcode> = detections.detectedItems
                val code = qrCodes.valueAt(0)
                codeInput.setText(code.displayValue)
                Log.i("app", code.displayValue)
            }else{
                //textResult.text = "Nothing detected"
                Log.i("app", "Nothing detected")
            }
        }

    }
}
