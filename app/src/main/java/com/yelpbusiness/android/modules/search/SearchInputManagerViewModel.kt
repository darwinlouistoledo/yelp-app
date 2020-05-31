package com.yelpbusiness.android.modules.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yelpbusiness.domain.common.SingleEvent
import javax.inject.Inject

class SearchInputManagerViewModel @Inject constructor(): ViewModel(){

  private val _searchFilters = MutableLiveData<SingleEvent<Triple<String?,String?,String?>>>()

  val searchFilters: LiveData<SingleEvent<Triple<String?,String?,String?>>> = _searchFilters

  fun applySearchFilters(term: String?, location:String?, categories: String?)=
    _searchFilters.postValue(SingleEvent(Triple(term, location, categories)))

}