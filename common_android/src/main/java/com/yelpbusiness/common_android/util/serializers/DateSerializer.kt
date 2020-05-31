package com.yelpbusiness.common_android.util.serializers

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateSerializer : JsonSerializer<Date> {
  override fun serialize(
    src: Date?,
    typeOfSrc: Type?,
    context: JsonSerializationContext?
  ): JsonElement? {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    return src?.let {
      val date = formatter.format(it)
      JsonPrimitive(date)
    }
  }
}