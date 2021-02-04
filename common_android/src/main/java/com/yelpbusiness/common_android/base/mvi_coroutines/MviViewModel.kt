package com.yelpbusiness.common_android.base.mvi_coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class MviViewModel<A : MviAction, S : MviState> : ViewModel() {

  protected abstract val initialState: S

  protected abstract fun handleAction(action: A)

  private val tag by lazy { javaClass.simpleName }

  private val _liveState by lazy {  MutableLiveData(initialState) }

  /**
   * You must observe this state LiveData in the View (Fragment or Activity)
   */
  val observableState: LiveData<S> = MediatorLiveData<S>().apply {
    addSource(_liveState) { data ->
      Timber.d("$tag received state: $data")
      value = data
    }
  }

  private val _action: MutableSharedFlow<A> = MutableSharedFlow()
  private val action = _action.asSharedFlow()

  init {
    subscribeActions()
  }

  /**
   * dispatch an Action
   */
  fun dispatch(action: A) {
    viewModelScope.launch { _action.emit(action) }
  }

  /**
   * Update the State data
   */
  protected fun updateState(reduce: S.() -> S) {
    val newState = _liveState.value?.reduce()
    _liveState.postValue(newState)
  }

  private fun subscribeActions(){
    viewModelScope.launch {
      action.collect {
        Timber.d("$tag received action: $it")
        handleAction(it)
      }
    }
  }

}