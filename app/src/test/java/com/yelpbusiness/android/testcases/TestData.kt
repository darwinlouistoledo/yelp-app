package com.yelpbusiness.android.testcases

import com.yelpbusiness.domain.enums.BusinessSort
import com.yelpbusiness.domain.model.Business
import com.yelpbusiness.domain.model.Category
import com.yelpbusiness.domain.model.Coordinates
import com.yelpbusiness.domain.model.Hour
import com.yelpbusiness.domain.model.Location
import com.yelpbusiness.domain.model.Schedule

object TestData {

  const val latitude = 14.5176f
  const val longitude = 121.0509f
  const val location = "taguig"
  const val categories = "cake"
  const val term = "banapple"
  val sort = BusinessSort.DISTANCE

  val businessOne = Business(
    id = "2yjtBWxyqIRnZ92nLSy1gw",
    alias = "banapple-taguig-2",
    name = "Banapple",
    primaryImgUrl = "https://s3-media2.fl.yelpcdn.com/bphoto/ZXEN2QwNMJ9cJjqTQtPHYQ/o.jpg",
    isClosed = false,
    isClaimed = false,
    url = "https://www.yelp.com/biz/banapple-taguig-2?adjust_creative=O1O2OfNbigx_ZU0G2SpbYQ&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_lookup&utm_source=O1O2OfNbigx_ZU0G2SpbYQ",
    phone = "+6325196437",
    displayPhone = "+63 2 519 6437",
    reviewCount = 5,
    categories = listOf(
      Category(
        alias = "desserts",
        title = "Desserts"
      ),
      Category(
        alias = "coffee",
        title = "Coffee & Tea"
      ),
      Category(
        alias = "cakeshop",
        title = "Patisserie/Cake Shop"
      )
    ),
    rating = 4.5f,
    location = Location(
      address1 = "McKinley Parkway,",
      address2 = "G/F, Market! Market!, Bonifacio Global City",
      address3 = "",
      city = "Taguig",
      zipCode = "1634",
      country = "PH",
      state = "NCR",
      displayAddress = listOf(
        "McKinley Parkway,",
        "G/F, Market! Market!, Bonifacio Global City",
        "Taguig, 1634 Metro Manila",
        "Philippines"
      )
    ),
    coordinates = Coordinates(
      latitude = 14.5498219643066f,
      longitude = 121.055429741f
    ),
    photos = listOf(
      "https://s3-media2.fl.yelpcdn.com/bphoto/ZXEN2QwNMJ9cJjqTQtPHYQ/o.jpg",
      "https://s3-media2.fl.yelpcdn.com/bphoto/FJm5W3940anxovUy1scHxw/o.jpg",
      "https://s3-media3.fl.yelpcdn.com/bphoto/gkYhxpLFxqRD2qveIO4PPQ/o.jpg"
    ),
    price = "₱₱",
    hours = listOf(
      Hour(
        schedule = listOf(
          Schedule(
            isOvernight = false,
            startTime = "0800",
            endTime = "2200",
            day = 0
          ),
          Schedule(
            isOvernight = false,
            startTime = "0800",
            endTime = "2200",
            day = 1
          ),
          Schedule(
            isOvernight = false,
            startTime = "0800",
            endTime = "2200",
            day = 2
          ),
          Schedule(
            isOvernight = false,
            startTime = "0800",
            endTime = "2200",
            day = 3
          ),
          Schedule(
            isOvernight = false,
            startTime = "0800",
            endTime = "2200",
            day = 4
          ),
          Schedule(
            isOvernight = false,
            startTime = "0800",
            endTime = "2200",
            day = 5
          ),
          Schedule(
            isOvernight = false,
            startTime = "0800",
            endTime = "2200",
            day = 6
          )
        ),
        hoursType = "REGULAR",
        isOpenNow = true
      )
    )
  )

  val businessList = listOf(
    businessOne
  )

}