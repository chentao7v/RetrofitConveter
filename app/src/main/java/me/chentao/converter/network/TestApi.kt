package me.chentao.converter.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * create by chentao on 2023-07-05.
 */
interface TestApi {

  @POST("test/post")
  fun doPost(@Body body: TestRequest): Call<ResponseBody>

}