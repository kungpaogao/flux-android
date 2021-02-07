package org.cornelldti.flux.util

import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Extension function for getting attribute value from `R.attr` resources
 */
fun Fragment.getAttr(@AttrRes attrRes: Int): Int {
    val value = TypedValue()
    requireContext().theme.resolveAttribute(attrRes, value, true)
    return TypedValue.complexToDimensionPixelSize(
        value.data, resources.displayMetrics
    )
}

/**
 * Extension function for getting color value from `R.color` resources
 */
fun Fragment.getColor(@ColorRes colorRes: Int): Int {
    return context?.let {
        ContextCompat.getColor(
            it,
            colorRes
        )
    } ?: Color.TRANSPARENT
}