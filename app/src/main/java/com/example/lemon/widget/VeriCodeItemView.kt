package com.example.lemon.widget


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.lemon.R


class VeriCodeItemView(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    val indicator: View
    val cursor: View

    init {
        LayoutInflater.from(context).inflate(R.layout.item_veri_code, this)
        cursor = findViewById(R.id.view_cursor)
        indicator = findViewById(R.id.view_indicator)
        cursor.isVisible = false

        setOnClickListener {
            postDelayed(
                {
                    findViewById<EditText>(R.id.et_veriCode_input).requestFocus()
                    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
                        findViewById<EditText>(R.id.et_veriCode_input), 0
                    )
                }, 300
            )
        }
    }

    var text
        get() = findViewById<TextView>(R.id.tv_number).text.toString()
        set(value) {
            findViewById<TextView>(R.id.tv_number).text = value
        }

    var textSize
        get() = findViewById<TextView>(R.id.tv_number).textSize
        set(value) {
            findViewById<TextView>(R.id.tv_number).textSize = value
        }

    var textColor
        get() = findViewById<TextView>(R.id.tv_number).currentTextColor
        set(value) {
            findViewById<TextView>(R.id.tv_number).setTextColor(value)
        }
}