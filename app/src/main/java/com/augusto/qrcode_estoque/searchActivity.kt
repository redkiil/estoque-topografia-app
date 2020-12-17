package com.augusto.qrcode_estoque

import android.os.Bundle
import android.util.Log
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val URL_PATH = "http://10.0.2.2:3000"

class SearchActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.request_list)
    }

    override fun onResume() {
        super.onResume()
        val retrofitClient = NetworkUtils.getRetrofitInstance()
        val endpoint = retrofitClient.create(RestApi::class.java)
        val callback = endpoint.getAllRequests()
        callback.enqueue(object : Callback<MutableList<ListModel?>>{
            override fun onFailure(call: Call<MutableList<ListModel?>>, t: Throwable) {
                Toast.makeText(applicationContext, "Falha ao obter os dados ${t.message}", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<MutableList<ListModel?>>, response: Response<MutableList<ListModel?>>) {
                findViewById<ExpandableListView>(R.id.listView).setAdapter(RequestListAdapter(applicationContext,
                    response.body() as MutableList<ListModel>
                ))
            }
        })
    }

    override fun onStart(){
        super.onStart()
        Log.d("app", "onStart")
    }

    override fun onRestart(){
        super.onRestart()
        Log.d("app", "onRestart")
    }

    override fun onPause(){
        super.onPause()
        Log.d("app", "onPause")
    }

    override fun onStop(){
        super.onStop()
        Log.d("app", "onStop")
    }

    override fun onDestroy(){
        super.onDestroy()
        Log.d("app", "onDestroy")
    }
}