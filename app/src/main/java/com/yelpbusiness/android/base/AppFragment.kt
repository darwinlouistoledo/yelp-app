package com.yelpbusiness.android.base

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.github.ybq.android.spinkit.SpinKitView
import com.yelpbusiness.android.R
import com.yelpbusiness.android.di.factory.ViewModelFactory
import com.yelpbusiness.domain.manager.ErrorHandler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class AppFragment : Fragment() {

  @Inject
  lateinit var viewModelFactory: ViewModelFactory

  @Inject
  lateinit var errorHandler: ErrorHandler<Fragment>

  protected val disposeBag: CompositeDisposable by lazy { CompositeDisposable() }

  private var progressDialog: MaterialDialog? = null

  override fun onDestroy() {
    super.onDestroy()
    disposeBag.dispose()
  }

  fun showProgressDialog(title: String? = null) {
    if (progressDialog == null) {
      progressDialog = MaterialDialog(requireContext()).show {
        title?.let {
          title(text = it)
        }
        customView(
            view = SpinKitView(context, null, 0, R.style.spinkit_basic).apply {
              setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }
        )
        cancelable(false)
        cancelOnTouchOutside(false)
      }
    }
  }

  fun dismissProgressDialog() {
    progressDialog?.dismiss()
    progressDialog = null
  }

  fun showGenericError() {
    MaterialDialog(requireContext()).show {
      message(res = R.string.generic_error_msg)
      positiveButton(res = R.string.lbl_okay)
    }
  }

}