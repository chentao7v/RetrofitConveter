package me.chentao.converter

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import me.chentao.converter.network.TestRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

  private lateinit var btnTest: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    btnTest = findViewById(R.id.btn_test)
    btnTest.setOnClickListener {
      test()
    }
  }

  private fun test() {
    TestRepository().testPost()
      .enqueue(object : Callback<ResponseBody?> {
        override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {

        }

        override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
        }
      })

  }

}