package com.yelpbusiness.android.testcases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.yelpbusiness.domain.exceptions.RequiredArgumentException
import com.yelpbusiness.domain.sealedclass.DataResult
import com.yelpbusiness.domain.usecase.BusinessUseCase
import io.reactivex.Observable
import org.amshove.kluent.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.net.UnknownHostException

@RunWith(MockitoJUnitRunner::class)
class BusinessUseCaseTest {

  @Test
  fun getAllBusinessesSuccessWithLatLonTest() {

    //GIVEN
    val mockBusinessUseCase = mock<BusinessUseCase>()

    doReturn(Observable.just(DataResult.Success(TestData.businessList)))
      .`when`(mockBusinessUseCase)
      .getBusinesses(
        lat = TestData.latitude,
        lon = TestData.longitude,
        location = null,
        categories = null,
        sort = null,
        term = null,
        clearCache = true
      )

    //WHEN
    val result = mockBusinessUseCase.getBusinesses(
      lat = TestData.latitude,
      lon = TestData.longitude,
      location = null,
      categories = null,
      sort = null,
      term = null,
      clearCache = true
    )
      .test()

    //THEN
    verify(mockBusinessUseCase).getBusinesses(
      lat = TestData.latitude,
      lon = TestData.longitude,
      location = null,
      categories = null,
      sort = null,
      term = null,
      clearCache = true
    )

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Success(TestData.businessList))

  }

  @Test
  fun getAllBusinessesSuccessWithLatLonTermTest() {

    //GIVEN
    val mockBusinessUseCase = mock<BusinessUseCase>()

    doReturn(Observable.just(DataResult.Success(TestData.businessList)))
      .`when`(mockBusinessUseCase)
      .getBusinesses(
        lat = TestData.latitude,
        lon = TestData.longitude,
        location = null,
        categories = null,
        sort = null,
        term = TestData.term,
        clearCache = true
      )

    //WHEN
    val result = mockBusinessUseCase.getBusinesses(
      lat = TestData.latitude,
      lon = TestData.longitude,
      location = null,
      categories = null,
      sort = null,
      term = TestData.term,
      clearCache = true
    )
      .test()

    //THEN
    verify(mockBusinessUseCase).getBusinesses(
      lat = TestData.latitude,
      lon = TestData.longitude,
      location = null,
      categories = null,
      sort = null,
      term = TestData.term,
      clearCache = true
    )

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Success(TestData.businessList))

  }

  @Test
  fun getAllBusinessesSuccessWithLatLonTermLocationTest() {

    //GIVEN
    val mockBusinessUseCase = mock<BusinessUseCase>()

    doReturn(Observable.just(DataResult.Success(TestData.businessList)))
      .`when`(mockBusinessUseCase)
      .getBusinesses(
        lat = TestData.latitude,
        lon = TestData.longitude,
        location = TestData.location,
        categories = null,
        sort = null,
        term = TestData.term,
        clearCache = true
      )

    //WHEN
    val result = mockBusinessUseCase.getBusinesses(
      lat = TestData.latitude,
      lon = TestData.longitude,
      location = TestData.location,
      categories = null,
      sort = null,
      term = TestData.term,
      clearCache = true
    )
      .test()

    //THEN
    verify(mockBusinessUseCase).getBusinesses(
      lat = TestData.latitude,
      lon = TestData.longitude,
      location = TestData.location,
      categories = null,
      sort = null,
      term = TestData.term,
      clearCache = true
    )

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Success(TestData.businessList))

  }

  @Test
  fun getAllBusinessesSuccessWithLatLonTermLocationCategoriesTest() {

    //GIVEN
    val mockBusinessUseCase = mock<BusinessUseCase>()

    doReturn(Observable.just(DataResult.Success(TestData.businessList)))
      .`when`(mockBusinessUseCase)
      .getBusinesses(
        lat = TestData.latitude,
        lon = TestData.longitude,
        location = TestData.location,
        categories = TestData.categories,
        sort = null,
        term = TestData.term,
        clearCache = true
      )

    //WHEN
    val result = mockBusinessUseCase.getBusinesses(
      lat = TestData.latitude,
      lon = TestData.longitude,
      location = TestData.location,
      categories = TestData.categories,
      sort = null,
      term = TestData.term,
      clearCache = true
    )
      .test()

    //THEN
    verify(mockBusinessUseCase).getBusinesses(
      lat = TestData.latitude,
      lon = TestData.longitude,
      location = TestData.location,
      categories = TestData.categories,
      sort = null,
      term = TestData.term,
      clearCache = true
    )

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Success(TestData.businessList))

  }

  @Test
  fun getAllBusinessesSuccessWithAllTest() {

    //GIVEN
    val mockBusinessUseCase = mock<BusinessUseCase>()

    doReturn(Observable.just(DataResult.Success(TestData.businessList)))
      .`when`(mockBusinessUseCase)
      .getBusinesses(
        lat = TestData.latitude,
        lon = TestData.longitude,
        location = TestData.location,
        categories = TestData.categories,
        sort = TestData.sort,
        term = TestData.term,
        clearCache = true
      )

    //WHEN
    val result = mockBusinessUseCase.getBusinesses(
      lat = TestData.latitude,
      lon = TestData.longitude,
      location = TestData.location,
      categories = TestData.categories,
      sort = TestData.sort,
      term = TestData.term,
      clearCache = true
    )
      .test()

    //THEN
    verify(mockBusinessUseCase).getBusinesses(
      lat = TestData.latitude,
      lon = TestData.longitude,
      location = TestData.location,
      categories = TestData.categories,
      sort = TestData.sort,
      term = TestData.term,
      clearCache = true
    )

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Success(TestData.businessList))

  }

  @Test
  fun getAllBusinessesLanLonRequiredTest() {
    //GIVEN
    val mockBusinessUseCase = mock<BusinessUseCase>()
    val throwable = RequiredArgumentException("lat and lon")

    doReturn(Observable.just(DataResult.Failed(throwable)))
      .`when`(mockBusinessUseCase)
      .getBusinesses(
        lat = null,
        lon = null,
        location = null,
        categories = null,
        sort = null,
        term = null,
        clearCache = true
      )

    //WHEN
    val result = mockBusinessUseCase.getBusinesses(
      lat = null,
      lon = null,
      location = null,
      categories = null,
      sort = null,
      term = null,
      clearCache = true
    )
      .test()

    //THEN
    verify(mockBusinessUseCase).getBusinesses(
      lat = null,
      lon = null,
      location = null,
      categories = null,
      sort = null,
      term = null,
      clearCache = true
    )

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Failed(throwable))
  }

  @Test
  fun getAllBusinessesFailedTest() {
    //GIVEN
    val mockBusinessUseCase = mock<BusinessUseCase>()
    val throwable = UnknownHostException()

    doReturn(Observable.just(DataResult.Failed(throwable)))
      .`when`(mockBusinessUseCase)
      .getBusinesses(
        lat = TestData.latitude,
        lon = TestData.longitude,
        location = null,
        categories = null,
        sort = null,
        term = null,
        clearCache = true
      )

    //WHEN
    val result = mockBusinessUseCase.getBusinesses(
      lat = TestData.latitude,
      lon = TestData.longitude,
      location = null,
      categories = null,
      sort = null,
      term = null,
      clearCache = true
    )
      .test()

    //THEN
    verify(mockBusinessUseCase).getBusinesses(
      lat = TestData.latitude,
      lon = TestData.longitude,
      location = null,
      categories = null,
      sort = null,
      term = null,
      clearCache = true
    )

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Failed(throwable))
  }

  @Test
  fun getBusinessDetailsSuccessTest() {
    //GIVEN
    val mockBusinessUseCase = mock<BusinessUseCase>()

    doReturn(Observable.just(DataResult.Success(TestData.businessOne)))
      .`when`(mockBusinessUseCase)
      .getBusiness(TestData.businessOne.id)

    //WHEN
    val result = mockBusinessUseCase.getBusiness(TestData.businessOne.id)
      .test()

    //THEN
    verify(mockBusinessUseCase).getBusiness(TestData.businessOne.id)

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Success(TestData.businessOne))

  }

  @Test
  fun getBusinessDetailsFailedTest() {

    //GIVEN
    val mockBusinessUseCase = mock<BusinessUseCase>()
    val throwable = UnknownHostException()

    doReturn(Observable.just(DataResult.Failed(throwable)))
      .`when`(mockBusinessUseCase)
      .getBusiness(TestData.businessOne.id)

    //WHEN
    val result = mockBusinessUseCase.getBusiness(TestData.businessOne.id)
      .test()

    //THEN
    verify(mockBusinessUseCase).getBusiness(TestData.businessOne.id)

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Failed(throwable))

  }

}