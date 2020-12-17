package com.augusto.qrcode_estoque

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.Serializable
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

private const val URL_PATH = "http://10.0.2.2:3000"

class NetworkUtils {
    companion object{
        fun getRetrofitInstance(): Retrofit{
            val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
            return Retrofit.Builder().baseUrl(URL_PATH).addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}
data class LoginModel(
    @SerializedName("teste") var teste: String,
    @SerializedName("teste2") var teste2: String
)
data class ListModel(
    var os: Int = -1,
    var protocol: Int = -1,
    var items: MutableList<Item> = mutableListOf(),
    var usedby: String = "N/A",
    var retiredby: String = "N/A",
    var date: Date = Date.from(Instant.now()),
    var itemsinfo: MutableList<Itemsinfo> = mutableListOf()
) : Serializable
data class ListItem(
    var code: Int,
    var instock: Int,
    var name: String?
) : Serializable
data class Item(
    var code: Int,
    var quant: Int
) : Serializable

data class Itemsinfo(
    var name: String?
) : Serializable

interface RestApi {
    @FormUrlEncoded
    @POST("/api/requests/new")
    fun addUser(@FieldMap asd: MutableMap<String, Any>): Call<ResponseBody>
    @GET("/api/requests")
    fun getAllRequests(): Call<MutableList<ListModel?>>
    @GET("/api/item/{codeid}")
    fun getItemInfo(@Path("codeid") code: Int): Call<ListItem>
}

/*

var requestSchema = new Schema({
    protocol: { type: Number},
    os: { type: Number, default: 0},
    usedby: { type: String },
    retiredby: { type: String},
    date: { type: Date, default: Date.now},
    items:[{
        code:{
            type: Number,
            ref: 'itemSchema'
        },quant:{ type: Number}
    }]
})

protocol: { type: Number},
os: { type: Number, default: 0},
usedby: { type: String },
retiredby: { type: String},
date: { type: Date, default: Date.now},
items: [requestItemSchema]*/