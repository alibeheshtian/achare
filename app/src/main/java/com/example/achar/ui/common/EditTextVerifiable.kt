package com.example.achar.ui.common

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import com.example.achar.R
import kotlinx.android.synthetic.main.custom_edittext.view.*


class EditTextVerifiable : LinearLayout, TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        setError(null)
        setIconState(if (s.isNullOrEmpty()) IconEnum.NORMALE else IconEnum.CHECKED)
    }

    private fun setIconState(state: IconEnum) {

        val icon = when (state) {
            IconEnum.ERROR -> R.drawable.ic_error
            IconEnum.NORMALE -> R.drawable.bg_circle
            IconEnum.CHECKED -> R.drawable.ic_check_circle
        }
        et_text.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
    }

    private fun setBackState(state: BackEnum) {
        val back = when (state) {
            BackEnum.ERROR -> R.drawable.bg_edt_error
            BackEnum.NORMALE -> R.drawable.bg_edt_unfocuse
            BackEnum.FOCUCE -> R.drawable.bg_edt_focuse
        }
        ll.setBackgroundResource(back)
    }


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
        initAttrs(attrs)
    }

    private fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 8f
        }
        View.inflate(context, R.layout.custom_edittext, this)

        et_text.addTextChangedListener(this)

        et_text.setOnFocusChangeListener { _, hasFocus ->
            setBackState(if (hasFocus) BackEnum.FOCUCE else BackEnum.NORMALE)
        }
    }

    fun getText(): Editable? {
        return et_text.text
    }

    fun setText(text: CharSequence) {
        et_text.setText(text)
    }

    fun getError(): CharSequence {
        return tv_error.text
    }

    fun setError(text: CharSequence?) {
        tv_error.text = text

        if (text.isNullOrEmpty() && et_text.hasFocus()) {
            setBackState(BackEnum.FOCUCE)
        } else if (text.isNullOrEmpty()) {
            setBackState(BackEnum.NORMALE)
        } else {
            setBackState(BackEnum.ERROR)
        }


        setIconState(if (text.isNullOrEmpty()) IconEnum.NORMALE else IconEnum.ERROR)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val ta: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.EditTextVerifiable)
        tv_label.text = ta.getString(R.styleable.EditTextVerifiable_label)
        et_text.setText(ta.getString(R.styleable.EditTextVerifiable_text))
        tv_error.text = ta.getString(R.styleable.EditTextVerifiable_error)
        ta.recycle()
    }

    private enum class BackEnum {
        ERROR, FOCUCE, NORMALE
    }

    private enum class IconEnum {
        ERROR, NORMALE, CHECKED
    }
}