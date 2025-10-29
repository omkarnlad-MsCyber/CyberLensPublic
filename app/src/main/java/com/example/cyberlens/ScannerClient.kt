
package com.example.cyberlens

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class ScanBatchRequest(val targets: List<String>)
data class ScanResult(val target: String, val rating: Int?, val issues: List<String>?)
data class ScanBatchResponse(val findings: List<ScanResult>)

interface ScannerApi {
    @POST("/api/scan-batch")
    fun scanBatch(@Body req: ScanBatchRequest): Call<ScanBatchResponse>
}

object ScannerClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5000/") // change to your backend (emulator -> host)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(ScannerApi::class.java)
}
