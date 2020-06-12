package com.yelpbusiness.android.modules.home

import com.yelpbusiness.android.modules.home.HomeViewModel.Action
import com.yelpbusiness.android.modules.home.HomeViewModel.State
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
import com.yelpbusiness.domain.sealedclass.LiveResult
import com.yelpbusiness.domain.sealedclass.LoadBusinessesQuery
import com.yelpbusiness.domain.usecase.BusinessUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class HomeViewModel @Inject constructor(
  private val businessUseCase: BusinessUseCase,
  private val schedulerProvider: SchedulerProvider
) : MviViewModel<Action, State>() {

  /**
   * These are the the actions that will be
   * invoked from the view to do an intent/process
   */
  sealed class Action : MviAction {
    data class SetSearchFilters(val filters: Triple<String?, String?, String?>) : Action()

    data class LoadBusinesses(
      val latitude: Float,
      val longitude: Float
    ) : Action()

    data class SetSort(val sortBy: String?) : Action()
  }

  /**
   * These are the changes that will be
   * invoked once the process has been finish and
   * will update the state
   */
  sealed class Change : MviChange {
    data class SetQueryData(val queryData: LoadBusinessesQueryData) : Change()
    data class SetLoadingResult(val result: LiveResult<List<Business>>) : Change()
    data class SetError(val e: Throwable) : Change()
    object ClearList : Change()
  }

  /**
   * This data class will hold all the data needed for the view
   * to display and it is only the data that is known to the view.
   */
  data class State(
    val queryData: LoadBusinessesQueryData? = null,
    val businessList: List<Business> = emptyList(),
    val loadingResult: SingleEvent<LiveResult<List<Business>>>? = null,
    val error: SingleEvent<Throwable>? = null
  ) : MviState {
    val isBySearchLocation = queryData?.location != null && queryData.location != ""

    val filterDisplay = mutableListOf<String>().apply {
      queryData?.location?.let {
        if (it.isNotEmpty())
          add("Nearby Location: $it")
      } ?: add("Nearby Your Location")

      queryData?.sort?.let {
        add("Sorted By $it")
      }

      queryData?.categories?.let {
        if (it.isNotEmpty())
          add("Categorized by $it")
      }
    }
  }

  override val initialState: State
    get() = State()

  private val reducer: Reducer<State, Change> = { state, change ->
    when (change) {
      is Change.SetLoadingResult -> state.copy(
          loadingResult = SingleEvent(change.result),
          businessList = when (change.result) {
            is LiveResult.Success -> change.result.value
            else -> emptyList()
          }
      )
      is Change.SetQueryData -> state.copy(queryData = change.queryData)
      is Change.SetError -> state.copy(error = SingleEvent(change.e))
      Change.ClearList -> state.copy(businessList = emptyList())
    }
  }

  init {

    val loadAction = Observable.mergeArray(
        actions.ofType<Action.LoadBusinesses>()
            .take(1)
            .filter {
              (it.latitude > 0f && it.longitude > 0f) &&
                  (it.latitude != observableState.value?.queryData?.lat &&
                      it.longitude != observableState.value?.queryData?.lon)
            }
            .map { LoadBusinessesQuery.LatLon(it.latitude, it.longitude) },
        actions.ofType<Action.SetSearchFilters>()
            .map { LoadBusinessesQuery.Filters(it.filters) },
        actions.ofType<Action.SetSort>()
            .map { LoadBusinessesQuery.Sort(it.sortBy) }
    )
        .distinctUntilChanged()
        .scan(LoadBusinessesQueryData()) { prev, curr ->
          when (curr) {
            is LoadBusinessesQuery.LatLon -> prev.copy(lat = curr.lat, lon = curr.lon)
            is LoadBusinessesQuery.Filters -> prev.copy(
                term = when {
                  curr.values.first?.isNotEmpty() == true -> curr.values.first
                  else -> null
                },
                location = when {
                  curr.values.second?.isNotEmpty() == true -> curr.values.second
                  else -> null
                },
                categories = when {
                  curr.values.third?.isNotEmpty() == true -> curr.values.third
                  else -> null
                }
            )
            is LoadBusinessesQuery.Sort -> prev.copy(sort = curr.value)
          }
        }
        .filter { (it.lat != null && it.lon != null) }
        .distinctUntilChanged()
        .switchMap {
          loadBusinessesObs(it.term, it.location, it.categories, it.lat, it.lon, it.sort)
              .startWith(
                  listOf(
                      Change.ClearList,
                      Change.SetLoadingResult(LiveResult.Loading),
                      Change.SetQueryData(it)
                  )
              )
        }

    val states = Observable.mergeArray(
        Observable.just(Change.SetLoadingResult(LiveResult.Loading)),
        loadAction
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
        clearCache = true
    )
        .map<Change> {
          when (it) {
            is DataResult.Success -> Change.SetLoadingResult(LiveResult.Success(it.value))
            is DataResult.Failed -> Change.SetLoadingResult(LiveResult.Failed(it.error))
          }
        }
        .onErrorReturn { Change.SetError(it) }
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
}