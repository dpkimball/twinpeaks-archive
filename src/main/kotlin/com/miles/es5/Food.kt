package com.miles.es5

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "food")
data class Food(
    @Id
    var id : Long = 0,
    var name : String = "",
    var is_organic : Boolean = false
)
