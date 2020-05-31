# yelp-businesses-app
This application is about finding a Business using Yelp Api V3.

The project has four modules: **app**, **common_android**, **domain** and **data**

In developing this application, the primary technologies and libraries are used:
 - **Kotlin** - The primary programming language used.
 - **MVVMi Architecture**  - You can read more about this architecture in this [article](https://medium.com/@thereallukesimpson/clean-architecture-with-mvvmi-architecture-components-rxjava-8c5093337b43)
 - **RxJava/RxKotlin/RxAndroid** - For implementing a reactive approach of the application
 - **Retrofit2 & OkHTTP3** - For HTTP calls and consuming web services
 - **Realm Database** - For local persistence and caching of data.
 - **Dagger2**  - For dependency injection 
 - **Glide** - For fast image loading
 - **Timber** - For logging mechanism
 - **Repository Pattern** - For data management of Source of Truth with a local caching mechanism.
 - **Android Architecture Component** - LiveData, LifeCycle, ViewModel, Navigation, DataBinding
 - **Reactive Location Library** - An Rx library for location manager
 - **RxPermission** - An Rx library for requesting permissions 
 
## Features
 - **Loading Of Businesses Based On Your Current Location**
 - **Searching/Filtering Of Businesses Based On Term, Location or Categories**
 - **Sorting of the result by Distance**
 - **Sorting of the result by Rating**
 - **Viewing of the Business Details from the List**
 - **Map view of the result with pins and name info**
 - **Viewing of the Business Details from the map by tapping the pin and the name info window**
