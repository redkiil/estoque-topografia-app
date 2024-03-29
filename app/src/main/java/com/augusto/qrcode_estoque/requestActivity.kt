package com.augusto.qrcode_estoque

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.request_order.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val INITIAL_VALUE_LAYOUT_ID = 105
private const val requestCodeReader = 1002



class RequestActivity : AppCompatActivity(){


    private var size = 0
    private var protocol = -1
    private var username = ""
    private lateinit var container:LinearLayout
    private lateinit var scroll:ScrollView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.request_order)
        findViewById<ImageButton>(R.id.btnMoreFields).setOnClickListener {addMoreFields()}
        findViewById<Button>(R.id.btnSend).setOnClickListener {sendData()}
        container = findViewById(R.id.container)
        scroll = findViewById(R.id.zeroaesq)

        val pref = applicationContext.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        val user = pref.getString("user", null)
        username = user!!


        if(intent.hasExtra("data")){
            val data = intent?.extras?.get("data") as ListModel
            for (item in data.items) {
                if(item.code != -1) addMoreFields(item.code.toString() , item.quant.toString())
            }
            findViewById<EditText>(R.id.osField).setText(data.os.toString())
            findViewById<EditText>(R.id.usedByField).setText(data.usedby)
            protocol = data.protocol
            username = data.retiredby
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addMoreFields(code: String = "", quant: String = ""){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mylist = inflater.inflate(R.layout.layout_item,null)
        mylist.id = size + INITIAL_VALUE_LAYOUT_ID
        size++
        mylist.findViewById<ImageButton>(R.id.searchButton).setOnClickListener{handleClick(mylist.id)}
        if(!code.isBlank()){
            mylist.findViewById<EditText>(R.id.itemCode).setText(code)
            mylist.findViewById<EditText>(R.id.itemQuant).setText(quant)
        }
        container.addView(mylist)
        scroll.post{
            scroll.fullScroll(View.FOCUS_DOWN)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleClick(nada: Int) {
        val intent = Intent(this, QrReader::class.java)//how i call activity(B) so good
        intent.putExtra("field", nada)
        //parentFragment!!.startActivityForResult(intent, REQ_CODE)//
        startActivityForResult(intent, requestCodeReader)

    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun sendData(){
        Log.d("app", "childs"+ container.childCount)
        val params = mutableMapOf<String,Any>()
        params["protocol"] = protocol
        params["os"] = osField.text
        params["usedby"] = usedByField.text
        params["retiredby"] = username

        for(i in INITIAL_VALUE_LAYOUT_ID..((INITIAL_VALUE_LAYOUT_ID-1)+container.childCount)){
            val mainLayout = findViewById<ConstraintLayout>(i)
            val firstInput = mainLayout.getChildAt(2) as AppCompatEditText
            val firstValue = firstInput.text
            val secondInput = mainLayout.getChildAt(3) as AppCompatEditText
            val secondValue = secondInput.text
            if (firstValue != null && secondValue != null) {
                if(firstValue.isNotEmpty() && secondValue.isNotEmpty()){
                    params["items[][${(i-INITIAL_VALUE_LAYOUT_ID)}][code]"] = firstValue
                    params["items[][${(i-INITIAL_VALUE_LAYOUT_ID)}][quant]"] = secondValue
                }
            }
            Log.d("app", "I: $i - FirstIin: $firstValue - SecondIn $secondValue")
        }
        val retrofitClient = NetworkUtils.getRetrofitInstance()
        val endpoint = retrofitClient.create(RestApi::class.java)


        val callback = endpoint.addUser(params)

        callback.enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "Falha ao enviar os dados ${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(applicationContext, "Requisição enviada!", Toast.LENGTH_LONG).show()
            }

        })
}
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
 super.onActivityResult(requestCode, resultCode, data)
 if(resultCode == requestCodeReader){
     val dt = data?.getIntExtra("returnfield",0)
     val dt2 = data?.getStringExtra("codeid")
     val dt4 = findViewById<ConstraintLayout>(dt!!)
     val test = dt4.getChildAt(2) as AppCompatEditText
     test.setText(dt2!!)
 }
}
}