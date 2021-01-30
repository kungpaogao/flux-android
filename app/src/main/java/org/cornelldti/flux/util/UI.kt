package org.cornelldti.flux.util

import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.fragment.app.Fragment

fun Fragment.getAttr(@AttrRes attrRes: Int): Int {
    val value = TypedValue()
    requireContext().theme.resolveAttribute(attrRes, value, true)
    return TypedValue.complexToDimensionPixelSize(
        value.data, resources.displayMetrics
    )
}