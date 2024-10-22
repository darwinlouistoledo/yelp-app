package com.yelpbusiness.common_android.ext.aac

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * This is an extension function that calls [ViewModelProviders.of]
 * Gets the view model (activity scope)
 */
inline fun <reified VM : ViewModel> withViewModel(
  fragmentActivity: FragmentActivity,
  factory: ViewModelProvider.Factory,
  function: VM.() -> Unit = {}
): VM {
  val vm = ViewModelProvider(fragmentActivity.viewModelStore, factory)[VM::class.java]
  vm.function()
  return vm
}

/**
 * This is an extension function that calls [ViewModelProviders.of]
 * Gets the view model (fragment scope)
 */
inline fun <reified VM : ViewModel> withViewModel(
  fragment: Fragment,
  factory: ViewModelProvider.Factory,
  function: VM.() -> Unit = {}
): VM {
  val vm = ViewModelProvider(fragment.viewModelStore, factory)[VM::class.java]
  vm.function()
  return vm
}