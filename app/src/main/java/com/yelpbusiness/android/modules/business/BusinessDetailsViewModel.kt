package com.yelpbusiness.android.modules.business

import androidx.lifecycle.viewModelScope
import com.yelpbusiness.android.modules.business.BusinessDetailsViewModel.Action
import com.yelpbusiness.android.modules.business.BusinessDetailsViewModel.State
import com.yelpbusiness.common_android.base.mvi_coroutines.MviAction
import com.yelpbusiness.common_android.base.mvi_coroutines.MviState
import com.yelpbusiness.common_android.base.mvi_coroutines.MviViewModel
import com.yelpbusiness.domain.common.SingleEvent
import com.yelpbusiness.domain.coroutines.DispatcherProvider
import com.yelpbusiness.domain.model.Business
import com.yelpbusiness.domain.sealedclass.DataResult
import com.yelpbusiness.domain.usecase.BusinessUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class BusinessDetailsViewModel @Inject constructor(
  private val businessUseCase: BusinessUseCase,
  private val dispatcherProvider: DispatcherProvider
) : MviViewModel<Action, State>() {

  /**
   * These are the the actions that will be
   * invoked from the view to do an intent/process
   */
  sealed class Action : MviAction {
    data class LoadBusinessData(val businessId: String) : Action()
  }

  /**
   * This data class will hold all the data needed for the view
   * to display and it is only the data that is known to the view.
   */
  data class State(
    val business: Business? = null,
    val showLoading: Boolean = false,
    val error: SingleEvent<Throwable>? = null
  ) : MviState {
    val schedules = business?.hours?.firstOrNull()?.schedule ?: emptyList()
    val isOpenNow = business?.hours?.firstOrNull()?.isOpenNow ?: false
  }

  override val initialState: State
    get() = State()

  override fun handleAction(action: Action) {
    when (action) {
      is Action.LoadBusinessData -> loadBusinessObs(action.businessId)
    }
  }

  private fun loadBusinessObs(
    id: String
  ) {
    viewModelScope.launch(dispatcherProvider.io()) {
      updateState { copy(showLoading = true) }
      val result =
        businessUseCase.getBusiness(id)
            .blockingLast()

      when (result) {
        is DataResult.Success -> updateState { copy(business = result.value, showLoading = false) }
        is DataResult.Failed -> updateState {
          copy(
              error = SingleEvent(result.error), showLoading = false
          )
        }
      }
    }
  }

}