package com.blackbelt.github.api.model

import com.google.gson.annotations.SerializedName

data class SearchResponseBody(
        @field:SerializedName("total_count")
        val totalCount: Int = 0,

        @field:SerializedName("incomplete_results")
        val incompleteResults: Boolean = false,

        @field:SerializedName("items")
        val items: List<SearchItemResponsBody> = ArrayList()
)