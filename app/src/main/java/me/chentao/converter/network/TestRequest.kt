package me.chentao.converter.network

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.github.chentao7v.converter.AsFormBody
import io.github.chentao7v.converter.FlatMembers

/**
 * create by chentao on 2023-07-05.
 */
@AsFormBody
class TestRequest {

  @SerializedName("serialized_key1")
  var key1: String? = null

  var key2: String? = null

  @FlatMembers
  var internalRequest: InternalRequest? = null

  @SerializedName("to_json_key")
  var toJsonRequest: ToJSONRequest? = null

}

class ToJSONRequest {

  @SerializedName("serialized_json_key1")
  var jsonKey1: String? = null

  var jsonKey2: Float = 1f

  override fun toString(): String {
    return Gson().toJson(this)
  }
}

class InternalRequest {

  var internalKey1: String? = null

  @SerializedName("internal_key2")
  var internalKey2: Int = 0

}