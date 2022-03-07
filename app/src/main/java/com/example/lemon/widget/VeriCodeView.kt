package com.example.lemon.widget

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.getColorStateListOrThrow
import androidx.core.content.res.getDimensionOrThrow
import androidx.core.content.res.getDrawableOrThrow
import androidx.core.content.res.getIntegerOrThrow
import androidx.core.view.isVisible
import com.example.lemon.R


class VeriCodeView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var textWatcher: VeriCodeTextWatcher? = null
    private val items = ArrayList<VeriCodeItemView>()


    init {
        LayoutInflater.from(context).inflate(R.layout.view_veri_code, this)
        val inputText = findViewById<EditText>(R.id.et_veriCode_input)
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.VeriCodeView)
        val maxLength = typeArray.getIntegerOrThrow(R.styleable.VeriCodeView_android_maxLength)
        val textColor = typeArray.getColor(R.styleable.VeriCodeView_android_textColor, Color.BLACK)
        val textSize = typeArray.getDimensionOrThrow(R.styleable.VeriCodeView_android_textSize)
        val indicator = typeArray.getColorStateListOrThrow(R.styleable.VeriCodeView_indicator)
        val cursor = typeArray.getDrawableOrThrow(R.styleable.VeriCodeView_cursor)
        val realWidth =
            typeArray.getLayoutDimension(R.styleable.VeriCodeView_android_layout_width, 0/*dummy*/)
        var itemSpacing = typeArray.getDimensionPixelSize(R.styleable.VeriCodeView_itemSpacing, 0)
        val itemViewWidth = if (itemSpacing == 0) {
            itemSpacing = (realWidth - inputText.width) / (2 * maxLength - 1)
            itemSpacing
        } else (realWidth - inputText.width - (maxLength - 1) * itemSpacing) / maxLength
        val selectedColor = indicator.getColorForState(
            intArrayOf(
                android.R.attr.state_selected
            ), indicator.defaultColor
        )

        inputText.filters = arrayOf(InputFilter.LengthFilter(maxLength))


        for (i in 0 until maxLength)
            items.add(VeriCodeItemView(context, null).also {
                it.textColor = textColor
                it.textSize = textSize
                it.indicator.setBackgroundColor(indicator.defaultColor)
                it.cursor.background = cursor
                it.id = i
            })
        addView(items[0].also {
            it.cursor.isVisible = true
            it.indicator.setBackgroundColor(selectedColor)
        }, LayoutParams(itemViewWidth, LayoutParams.MATCH_PARENT).apply {
            leftToLeft = id
        })

        for (i in 1 until maxLength) {
            addView(items[i], LayoutParams(itemViewWidth, LayoutParams.MATCH_PARENT).apply {
                leftToRight = items[i - 1].id
                leftMargin = itemSpacing
            })
        }

        inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val input = findViewById<EditText>(R.id.et_veriCode_input).text.toString()
                val len = input.length
                for (i in 0 until len) {
                    items[i].let {
                        it.text = input.substring(i, i + 1)
                        it.cursor.isVisible = false
                        it.indicator.setBackgroundColor(selectedColor)
                    }
                }
                for (i in len until items.size)
                    items[i].let {
                        it.text = ""
                        it.cursor.isVisible = false
                        it.indicator.setBackgroundColor(indicator.defaultColor)
                    }
                if (len < maxLength)
                    items[len].let {
                        it.cursor.isVisible = true
                        it.indicator.setBackgroundColor(selectedColor)
                    }
                textWatcher?.onTextChanged(inputText.text.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })

        typeArray.recycle()
    }


    private fun showSoftKeyBoard() {
        val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        postDelayed(
            {
                manager.showSoftInput(findViewById<EditText>(R.id.et_veriCode_input), 0)
            },
            300
        )
    }

    fun setTextWatcher(textWatcher: VeriCodeTextWatcher?) {
        this.textWatcher = textWatcher
    }


    interface VeriCodeTextWatcher {
        fun onTextChanged(text: String?)
    }

}