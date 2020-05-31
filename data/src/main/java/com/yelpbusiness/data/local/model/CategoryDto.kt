package com.yelpbusiness.data.local.model

import com.yelpbusiness.domain.model.Category
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class CategoryDto() : RealmObject(){
  @PrimaryKey var reference: String = ""
  var businessId: String = ""
  var alias: String = ""
  var title: String = ""

  constructor(item: Category, bId: String, ref: String) : this(){
    reference = ref
    businessId = bId
    alias = item.alias
    title = item.title
  }

  val toCategory: Category
    get() = Category(
      alias = alias,
      title = title
    )

}