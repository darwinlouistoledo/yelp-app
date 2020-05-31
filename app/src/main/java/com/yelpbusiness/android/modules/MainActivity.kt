package com.yelpbusiness.android.modules

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.yelpbusiness.android.R
import com.yelpbusiness.android.databinding.ActivityMainBinding
import com.yelpbusiness.common_android.ext.databinding.withBinding
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private lateinit var navController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = withBinding(R.layout.activity_main) {
      navController = findNavController(R.id.nav_host_fragment)
    }
  }
}
