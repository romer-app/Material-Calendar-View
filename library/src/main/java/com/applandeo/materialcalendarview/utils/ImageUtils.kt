package com.applandeo.materialcalendarview.utils

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import android.widget.ImageView

/**
 * This class is used to load event image in a day cell
 *
 *
 * Created by Mateusz Kornakiewicz on 23.05.2017.
 */

object ImageUtils {

    fun loadImage(imageView: ImageView, image: Any?) {
        if (image == null) {
            return
        }

        var drawable: Drawable? = null
        if (image is Drawable) {
            drawable = image
        } else if (image is Int) {
            drawable = ContextCompat.getDrawable(imageView.context, (image as Int?)!!)
        }

        if (drawable == null) {
            return
        }

        imageView.setImageDrawable(drawable)
    }
}