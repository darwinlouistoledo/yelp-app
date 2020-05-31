package com.yelpbusiness.common_android.util.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.NoSuchElementException

class DateDeserializer : JsonDeserializer<Date> {
  override fun deserialize(
    json: JsonElement?,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): Date {
    val date = json?.asString ?: throw NoSuchElementException("date not found")
    val utcDate = date.replace("Z", "+00:00")

    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    return formatter.parse(utcDate)
  }

}
