package com.yelpbusiness.data.remote.model

import com.yelpbusiness.domain.model.Category

data class CategoryRepo(
  val alias: String,
  val title: String
) {

  constructor(item: Category) : this(
    alias = item.alias,
    title = item.title
  )

  val toCategory: Category
    get() = Category(
      alias = alias,
      title = title
    )

}