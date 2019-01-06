package com.theoxao.maikan.model.enums

import com.theoxao.maikan.model.Book
import java.util.*

class ShelfBook {
    var id: String? = null
    var refBookId: String? = null
    var name: String? = null
    var author: String? = null
    var introduction: String? = null
    var isbn: String? = null
    var pageCount: String? = null
    var publisher: String? = null
    var cover: String? = null
    var state: Int? = null
    var returnDate: Date? = null
    var remark: String? = null
    var progress = 0
    var tag: String? = null
}