package com.miles.es5

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface FoodRepo : ElasticsearchRepository<Food, Long>
