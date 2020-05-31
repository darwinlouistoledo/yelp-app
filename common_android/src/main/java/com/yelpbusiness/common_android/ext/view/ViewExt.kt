package com.yelpbusiness.common_android.ext.view

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 * Checks if view is visible
 */
fun View.isVisible(): Boolean = visibility == View.VISIBLE

/**
 * Checks if view is gone
 */
fun View.isGone(): Boolean = visibility == View.GONE

/**
 * Checks if view is invisible
 */
fun View.isInvisible(): Boolean = visibility == View.INVISIBLE

/**
 * Makes the view visible
 */
fun View.makeVisible() {
  visibility = View.VISIBLE
}

/**
 * Makes the view gone
 */
fun View.makeGone() {
  visibility = View.GONE
}

/**
 * Makes the view invisible
 */
fun View.makeInvisible() {
  visibility = View.INVISIBLE
}

/**
 * Makes this view visible if condition is true, invisible otherwise.
 */
fun View.makeVisibleIf(condition: Boolean) {
  when (condition) {
    true -> this.makeVisible()
    else -> this.makeInvisible()
  }
}

/**
 * Makes this view visible if condition is true, invisible otherwise.
 */
fun View.makeVisibleIf(condition: () -> Boolean) {
  this.makeVisibleIf(condition())
}

/**
 * Makes this view visible if condition is true, gone otherwise.
 */
fun View.makeVisibleOrGone(condition: Boolean) {
  when (condition) {
    true -> this.makeVisible()
    else -> this.makeGone()
  }
}

/**
 * Makes this view visible if condition is true, gone otherwise.
 */
fun View.makeVisibleOrGone(condition: () -> Boolean) {
  this.makeVisibleOrGone(condition())
}

/**
 * Make the view enable
 */
fun View.enable() {
  isEnabled = true
}

/**
 * Make the view disable
 */
fun View.disable() {
  isEnabled = false
}

fun View.setBackgroundTintList(@ColorRes color: Int) {
  backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, color))
}