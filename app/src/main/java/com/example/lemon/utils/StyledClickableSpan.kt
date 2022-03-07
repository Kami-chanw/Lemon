package com.example.lemon.utils

import android.content.Context
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.example.lemon.R

class StyledClickableSpan(context: Context, click : (View)->Unit ) : ClickableSpan() {
    private val textColor: Int = ContextCompat.getColor(context, R.color.super_link)
    private val clickListener = click
    override fun onClick(p0: View) {
        clickListener(p0)
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = textColor
        ds.isUnderlineText = false
    }
}