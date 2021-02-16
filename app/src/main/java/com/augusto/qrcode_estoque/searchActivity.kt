package com.augusto.qrcode_estoque

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.request_list.*
import kotlinx.android.synthetic.main.settings.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

private const val DATAONE = 1
private const val DATATWO = 2


class SearchActivity: AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.request_list)
        dataOne.tag = DATAONE
        dataOne.setOnClickListener(this)
        dataTwo.tag = DATATWO
        dataTwo.setOnClickListener(this)
        buttonFilter.setOnClickListener(this)
        getRequest()
    }

    override fun onResume() {
        super.onResume()
        getRequest()
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



    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val dataStr = String.format("%02d/%02d/%d", dayOfMonth, month+1, year)
        when(view?.tag){
            DATAONE -> {
                dataOne.setText(dataStr)
            }
            DATATWO -> {
                dataTwo.setText(dataStr)
            }
        }
        buttonFilter.requestFocusFromTouch()
    }

    override fun onClick(v: View?) {
        if(v?.id == buttonFilter.id){
            val formFrom = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateFrom = formFrom.parse(dataOne.text.toString()).time

            val formTo = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateTo = formTo.parse(dataTwo.text.toString()).time

            getRequest(dateFrom, dateTo)


        }else if(v?.tag.toString().isNotBlank()){
            val dpd = DatePickerDialog(this, this, 2020,1,1)
            dpd.datePicker.tag = v?.tag
            dpd.show()
        }
    }
    private fun getRequest(from: Long = 0, to: Long = System.currentTimeMillis()){
        val retrofitClient = NetworkUtils.getRetrofitInstance()
        val endpoint = retrofitClient.create(RestApi::class.java)
        val callback = endpoint.getAllRequests(from,to)
        callback.enqueue(object : Callback<MutableList<ListModel?>>{
            override fun onFailure(call: Call<MutableList<ListModel?>>, t: Throwable) {
                Toast.makeText(applicationContext, "Falha ao obter os dados ${t.message}", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<MutableList<ListModel?>>, response: Response<MutableList<ListModel?>>) {
                findViewById<ExpandableListView>(R.id.listView).setAdapter(RequestListAdapter(applicationContext,
                    response.body() as MutableList<ListModel>
                ))
                val teste = findViewById<DrawerLayout>(R.id.drawer)
                teste.closeDrawer(GravityCompat.START);
            }
        })
    }
}