package me.chentao.converter.network

import io.github.chentao7v.converter.FormBodyConverterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit

/**
 * create by chentao on 2023-07-05.
 */
class TestRepository {

  private val retrofit by lazy {
    val okHttpClient = OkHttpClient.Builder()
      .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
      })
      .build()

    Retrofit.Builder()
      .client(okHttpClient)
      .addConverterFactory(FormBodyConverterFactory.create())
      .baseUrl("https://m.baidu.com/")
      .build()
  }


  fun testPost(): Call<ResponseBody> {

    // build request
    val request = TestRequest().apply {
      internalRequest = InternalRequest().apply {
        internalKey1 = "intervalValue1"
        internalKey2 = 1111
      }

      key1 = "value1"
      key2 = "value2"

      toJsonRequest = ToJSONRequest().apply {
        jsonKey1 = "value1"
        jsonKey2 = 123.321f
      }
    }

    return retrofit.create(TestApi::class.java)
      .doPost(request)
  }

}