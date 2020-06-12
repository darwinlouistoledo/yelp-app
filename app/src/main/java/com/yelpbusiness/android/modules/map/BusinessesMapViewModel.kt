package com.yelpbusiness.android.modules.map

import com.yelpbusiness.android.modules.map.BusinessesMapViewModel.Action
import com.yelpbusiness.android.modules.map.BusinessesMapViewModel.State
import com.yelpbusiness.common_android.base.mvi.MviAction
import com.yelpbusiness.common_android.base.mvi.MviChange
import com.yelpbusiness.common_android.base.mvi.MviState
import com.yelpbusiness.common_android.base.mvi.MviViewModel
import com.yelpbusiness.common_android.base.mvi.Reducer
import com.yelpbusiness.domain.common.SingleEvent
import com.yelpbusiness.domain.enums.BusinessSort
import com.yelpbusiness.domain.model.Business
import com.yelpbusiness.domain.model.LoadBusinessesQueryData
import com.yelpbusiness.domain.rx.SchedulerProvider
import com.yelpbusiness.domain.sealedclass.DataResult
import com.yelpbusiness.domain.usecase.BusinessUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class BusinessesMapViewModel @Inject constructor(
  private val businessUseCase: BusinessUseCase,
  private val schedulerProvider: SchedulerProvider
) : MviViewModel<Action, State>() {

  /**
   * These are the the actions that will be
   * invoked from the view to do an intent/process
   */
  sealed class Action : MviAction {
    data class LoadBusinesses(
      val lat: Float?,
      val lon: Float?,
      val term: String?,
      val location: String?,
      val categories: String?,
      val sort: String?
    ) : Action()

    object PopulateMap : Action()
  }

  /**
   * These are the changes that will be
   * invoked once the process has been finish and
   * will update the state
   */
  sealed class Change : MviChange {
    data class SetQueryData(val queryData: LoadBusinessesQueryData) : Change()
    object SetPopulateMap : Change()
    data class SetLoadingResult(val result: List<Business>) : Change()
    data class SetError(val e: Throwable) : Change()
  }

  /**
   * This data class will hold all the data needed for the view
   * to display and it is only the data that is known to the view.
   */
  data class State(
    val queryData: LoadBusinessesQueryData? = null,
    val businessList: List<Business> = emptyList(),
    val populateMap: SingleEvent<Boolean>? = null,
    val error: SingleEvent<Throwable>? = null
  ) : MviState

  override val initialState: State
    get() = State()

  private val reducer: Reducer<State, Change> = { state, change ->
    when (change) {
      is Change.SetQueryData -> state.copy(queryData = change.queryData)
      is Change.SetLoadingResult -> state.copy(
          businessList = change.result,
          populateMap = SingleEvent(true)
      )
      Change.SetPopulateMap -> state.copy(populateMap = SingleEvent(true))
      is Change.SetError -> state.copy(error = SingleEvent(change.e))
    }
  }

  init {

    val actionPopulate = actions.ofType<Action.PopulateMap>()
        .map { Change.SetPopulateMap }

    val actionLoad = actions.ofType<Action.LoadBusinesses>()
        .take(1)
        .switchMap {
          loadBusinessesObs(
              latitude = it.lat,
              longitude = it.lon,
              term = it.term,
              location = it.location,
              categories = it.categories,
              sortBy = it.sort
          )
              .startWith(
                  Change.SetQueryData(
                      LoadBusinessesQueryData(
                          lat = it.lat,
                          lon = it.lon,
                          term = it.term,
                          location = it.location,
                          categories = it.categories,
                          sort = it.sort
                      )
                  )
              )
        }

    val states = Observable.mergeArray(
        actionLoad,
        actionPopulate
    )
        .onErrorReturn { Change.SetError(it) }
        .scan(initialState, reducer)
        .distinctUntilChanged()

    subscribe(states)

  }

  private fun loadBusinessesObs(
    term: String?,
    location: String?,
    categories: String?,
    latitude: Float?,
    longitude: Float?,
    sortBy: String?
  ): Observable<Change> =
    businessUseCase.getBusinesses(
        term = term,
        location = location,
        categories = categories,
        lat = latitude,
        lon = longitude,
        sort = when {
          sortBy != null && sortBy.equals(BusinessSort.DISTANCE.name, true) -> BusinessSort.DISTANCE
          sortBy != null && sortBy.equals(BusinessSort.RATING.name, true) -> BusinessSort.RATING
          else -> null
        },
        clearCache = false
    )
        .map<Change> {
          when (it) {
            is DataResult.Success -> Change.SetLoadingResult(it.value)
            is DataResult.Failed -> Change.SetError(it.error)
          }
        }
        .onErrorReturn { Change.SetError(it) }
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
}