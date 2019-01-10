package com.theoxao.maikan.model.enums

enum class TagFixed {

    ALL("全部", "all_books_fixed"),
    ALREADY_READ("已读", "already_read_fixed");

    val display: String
    val code: String

    constructor(display: String, code: String) {
        this.display = display
        this.code = code
    }
}