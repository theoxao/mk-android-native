package com.theoxao.maikan.model

import java.util.*

class Excerpt {

    var id: String? = null
    var userId: String? = null
    var refBook: String? = null
    var refPage: Int? = null
    var content: String? = null
    var images: MutableList<String> = arrayListOf()
    var createAt: Date? = null
}