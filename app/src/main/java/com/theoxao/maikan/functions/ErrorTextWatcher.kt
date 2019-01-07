package com.theoxao.maikan.functions

import android.text.Editable
import android.text.TextWatcher

interface ErrorTextWatcher : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }
}