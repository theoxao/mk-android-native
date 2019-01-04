package com.theoxao.maikan.functions

interface OnClick<T> {
    operator fun invoke(t: T)
}