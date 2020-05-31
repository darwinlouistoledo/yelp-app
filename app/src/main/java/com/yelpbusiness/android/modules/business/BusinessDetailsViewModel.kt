package com.yelpbusiness.android.modules.business

import com.yelpbusiness.android.modules.business.BusinessDetailsViewModel.Action
import com.yelpbusiness.android.modules.business.BusinessDetailsViewModel.State
import com.yelpbusiness.common_android.base.mvi.MviAction
import com.yelpbusiness.common_android.base.mvi.MviChange
import com.yelpbusiness.common_android.base.mvi.MviState
import com.yelpbusiness.common_android.base.mvi.MviViewModel
import com.yelpbusiness.common_android.base.mvi.Reducer
import com.yelpbusiness.domain.common.SingleEvent
import com.yelpbusiness.domain.model.Business
import com.yelpbusiness.domain.rx.SchedulerProvider
import com.yelpbusiness.domain.sealedclass.DataResult
import com.yelpbusiness.domain.usecase.BusinessUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class BusinessDetailsViewModel @Inject constructor(
  private val businessUseCase: BusinessUseCase,
  private val schedulerProvider: SchedulerProvider
) : MviViewModel<Action, State>() {

  /**
   * These are the the actions that will be
   * invoked from the view to do an intent/process
   */
  sealed class Action : MviAction {
    data class LoadBusinessData(val businessId: String) : Action()
  }

  /**
   * These are the changes that will be
   * invoked once the process has been finish and
   * will update the state
   */
  sealed class Change : MviChange {
    data class BusinessData(val business: Business) : Change()
    data class Error(val throwable: Throwable) : Change()
    object ShowLoading: Change()
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
    val schedules = business?.hours?.firstOrNull()?.schedule?: emptyList()
    val isOpenNow = business?.hours?.firstOrNull()?.isOpenNow?:false
  }

  override val initialState: State
    get() = State()

  private val reducer: Reducer<State, Change> = { state, change ->
    when (change) {
      is Change.BusinessData -> state.copy(business = change.business, showLoading = false)
      is Change.Error -> state.copy(error = SingleEvent(change.throwable), showLoading = false)
      Change.ShowLoading -> state.copy(showLoading = true)
    }
  }

  init {
    val loadBusinessDataAction = actions.ofType<Action.LoadBusinessData>()
      .switchMap { loadBusinessObs(it.businessId) }

    val states = Observable.mergeArray(
      loadBusinessDataAction
    )
      .onErrorReturn { Change.Error(it) }
      .scan(initialState, reducer)
      .distinctUntilChanged()

    subscribe(states)
  }

  private fun loadBusinessObs(
    id: String
  ): Observable<Change> =
    businessUseCase.getBusiness(id)
      .map {
        when (it) {
          is DataResult.Success -> Change.BusinessData(it.value)
          is DataResult.Failed -> Change.Error(it.error)
        }
      }
      .startWith(Change.ShowLoading)
      .onErrorReturn { Change.Error(it) }
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())

}